package cn.kt.mall.management.logistics.controller;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.excel.ExcelGenerator;
import cn.kt.mall.common.excel.ExportToExcelUtil;
import cn.kt.mall.common.excel.ReportColumn;
import cn.kt.mall.common.excel.ReportData;
import cn.kt.mall.common.excel.ReportDefinition;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;

import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.management.admin.service.impl.UserOperatorLogServiceImpl;
import cn.kt.mall.management.jwt.Subject;
import cn.kt.mall.management.logistics.dao.LogisticsDAO;
import cn.kt.mall.management.logistics.dao.LogisticsExportDAO;
import cn.kt.mall.management.logistics.dao.ShopTransportDAO;
import cn.kt.mall.management.logistics.service.impl.ShopTradePatchServiceImpl;
import cn.kt.mall.management.logistics.vo.*;

import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.mapper.ShopDAO;

import com.github.pagehelper.PageInfo;
//import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Cleanup;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

@Api(description = "物流管理", tags = "management-logistic")
@RequestMapping("/logistic")
@RestController
public class LogisticsController {
    @Autowired
    private LogisticsDAO logisticsDAO;

    @Autowired
    private ShopTransportDAO shopTransportDAO;

    @Autowired
    private ShopDAO shopDAO;

    @Autowired
    private ShopTradePatchServiceImpl logisticsServiceImpl;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserOperatorLogServiceImpl userOperatorLogService;

    @Autowired
    private LogisticsExportDAO logisticsExportDAO;

    private static Logger logger = LoggerFactory.getLogger(LogisticsController.class);

    private static int poolSieze =10;

    public static ReentrantLock lock = new ReentrantLock();

    //导出的时候全局list
    private static List<LogisticsExcelVO> logisticsExcelVOList =Collections.synchronizedList(new ArrayList<LogisticsExcelVO>());
    //List MyStrList = Collections.synchronizedList(new ArrayList());
    ///定义原子类
    private static boolean  isBool = false;

    //商品详情所有数据对象
    public static ConcurrentHashMap<String,Map<String,List<ShopTradeItemVO>>> shopTradeItemMap = new ConcurrentHashMap<>();
    //线程池
    private static ExecutorService pool = Executors.newFixedThreadPool(poolSieze);

    private static CountDownLatch countDownLatch = null;


    @ApiOperation(value = "管理后台商品物流信息")
    @PostMapping("getLogisticsVOList")
    @ResponseBody
    public CommonPageVO<LogisticsGoodVO> getLogisticsVOList(@RequestBody LogisticsRquestVO logisticsRquestVO,
                                                            @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {

      try {
          //总数据列表对象
          List<LogisticsGoodVO> logisticsGoodVOList = new ArrayList<LogisticsGoodVO>();
          RowBounds rowBounds = new RowBounds(pageNo, pageSize);
          /**
           * 根据条件查询出所有的内部订单信息
           */

          List<CurrentTimeVO> currentTimeVoList = new ArrayList<CurrentTimeVO>();
          if (logisticsRquestVO != null) {
              if (logisticsRquestVO.getStatus() == "" || logisticsRquestVO.equals("")) {
                  currentTimeVoList = logisticsDAO.getCurrentTimeVOList(logisticsRquestVO, rowBounds);
              } else if (logisticsRquestVO.getStatus().equals("1")) {
                  currentTimeVoList = logisticsDAO.getInteriorNoListNoSend(logisticsRquestVO, rowBounds);
              } else if (logisticsRquestVO.getStatus().equals("2")) {
                  currentTimeVoList = logisticsDAO.getInteriorNoListSended(logisticsRquestVO, rowBounds);
              } else if (logisticsRquestVO.getStatus().equals("3")) {
                  currentTimeVoList = logisticsDAO.getInteriorNoListFinish(logisticsRquestVO, rowBounds);
              } else if (logisticsRquestVO.getStatus().equals("4")) {
                  currentTimeVoList = logisticsDAO.getInteriorNoListPartSend(logisticsRquestVO, rowBounds);
              }
          }
          if(currentTimeVoList!=null&&currentTimeVoList.size()>0) {
              List<InneriornoVO> inneriornoVOList = new ArrayList<InneriornoVO>();
              CurrentTimeResqVO currentTimeResqVO = new CurrentTimeResqVO();
              List<Long> currentListStr = new ArrayList<Long>();
              if (currentTimeVoList != null && currentTimeVoList.size() > 0) {
                  for (CurrentTimeVO vodate : currentTimeVoList) {
                      currentListStr.add(vodate.getCurrentTime());
                  }
              }
              currentTimeResqVO.setCurrentTimeVOList(currentListStr);
              inneriornoVOList = logisticsDAO.getInteriorNoList(currentTimeResqVO);

              List<String> noList = new ArrayList<>();
              if (inneriornoVOList != null && inneriornoVOList.size() > 0) {
                  for (InneriornoVO data : inneriornoVOList) {
                      noList.add(data.getInteriorNo());
                  }
              }
              //查询的结果不为空
              if (noList != null && noList.size() > 0) {
                  InneriornoResqVO inneriornoResqVO = new InneriornoResqVO();
                  inneriornoResqVO.setInneriornoList(noList);
                  if (logisticsRquestVO.getEndTime() != null && logisticsRquestVO.getGoodNames().size() > 0) {
                      inneriornoResqVO.setGoodNames(logisticsRquestVO.getGoodNames());
                  }
                  List<LogisticsVO> logisticsList = logisticsDAO.getLogisticsVOList(inneriornoResqVO);
                  //将相同的下单号的订单放入到map中，目的是为了循环map进行数据库查询，可以减少对数据库的查询
                  Map<String, List<LogisticsVO>> logisticsMap = new HashMap<String, List<LogisticsVO>>();
                  //构造订单的基本信息
                  Map<String, LogisticsBaseInfoVO> baseInfoMap = new HashMap<>();
                  if (logisticsList != null && logisticsList.size() > 0) {
                      for (String innerNo : noList) {//循环不重复的所有内部订单
                          //
                          for (LogisticsVO logisticsVO : logisticsList) {//循环重复的订单编号
                              String reciveAddress = "";
                              if (logisticsVO.getProvinceValue() != null) {
                                  reciveAddress = reciveAddress + logisticsVO.getProvinceValue();
                                  ;
                              }
                              if (logisticsVO.getCityValue() != null) {
                                  reciveAddress = reciveAddress + logisticsVO.getCityValue();
                              }
                              if (logisticsVO.getCountyValue() != null) {
                                  reciveAddress = reciveAddress + logisticsVO.getCountyValue();
                              }


                              LogisticsBaseInfoVO basseInfoVO = new LogisticsBaseInfoVO();

                              if (logisticsVO.getInteriorNo().equals(innerNo)) {
                                  //判断该总订单是不是全部发货，还是未发货，还是部分发货
                                  //获取商品总数量

                                  if (logisticsMap.containsKey(innerNo)) {
                                      List<LogisticsVO> logisticsVOList = logisticsMap.get(innerNo);
                                      logisticsVOList.add(logisticsVO);
                                      logisticsMap.put(innerNo, logisticsVOList);
                                  } else {
                                      List<LogisticsVO> logisticsVOList = new ArrayList<LogisticsVO>();


                                      String sendStatus = "0";
                                      if (logisticsRquestVO.getStatus() != null && logisticsRquestVO.getStatus() != "") {


                                          if (logisticsRquestVO.getStatus().equals("1")) {
                                              sendStatus = "1";
                                          } else if (logisticsRquestVO.getStatus().equals("2")) {
                                              sendStatus = "2";
                                          } else if (logisticsRquestVO.getStatus().equals("3")) {
                                              sendStatus = "3";
                                          } else if (logisticsRquestVO.getStatus().equals("4")) {
                                              //这一步需要做更近一步判断
                                              //部分发货，需要区分出哪个订单部分发货
                                              sendStatus = "4";

                                          }
                                          basseInfoVO.setStatus(sendStatus);//订单状态
                                      } else {
                                          int totaGoodNum = logisticsDAO.getGoodNumByinteriorNo(logisticsVO.getInteriorNo());
                                          int noSendGoodNum = logisticsDAO.getNoSendGoodNumByinteriorNo(logisticsVO.getInteriorNo());
                                          if (totaGoodNum == noSendGoodNum) {
                                              sendStatus = "1";
                                          } else if (noSendGoodNum == 0) {
                                              sendStatus = "2";
                                              //再判断时候全部完成
                                              int finishedNum = logisticsDAO.getNoSendGoodSended(logisticsVO.getInteriorNo());
                                              if (finishedNum == totaGoodNum) {
                                                  sendStatus = "3";
                                              }

                                          } else if (totaGoodNum > noSendGoodNum && noSendGoodNum > 0) {
                                              sendStatus = "4";
                                          }
                                          basseInfoVO.setStatus(sendStatus);//订单状态

                                      }
                                      logisticsVOList.add(logisticsVO);
                                      basseInfoVO.setInteriorNo(innerNo);//内部订单号
                                      basseInfoVO.setCreateTime(logisticsVO.getCreateTime());//创建时间

                                      basseInfoVO.setBuyUserId(logisticsVO.getBuyUserId()); //购买者的用户Id
                                      basseInfoVO.setRecvName(logisticsVO.getRecvName());//收货人名字


                                      basseInfoVO.setDetailAddress(reciveAddress + " " + logisticsVO.getDetailAddress()); //收货地址
                                      basseInfoVO.setRecvMobile(logisticsVO.getRecvMobile()); //收货人电话
                                      basseInfoVO.setShopNo(logisticsVO.getShopNo());
                                      basseInfoVO.setShopId(logisticsVO.getShopId());
                                      basseInfoVO.setTradeId(logisticsVO.getTradeId());
                                      basseInfoVO.setShopMobile(logisticsVO.getShopMobile());

                                      baseInfoMap.put(innerNo, basseInfoVO);
                                      logisticsMap.put(innerNo, logisticsVOList);
                                  }
                              }
                          }
                      }
                  }
                  //对map进行判断

                  if (logisticsMap != null && logisticsMap.size() > 0) {
                      //map的长度是店的数量
                      for (String keyStr : logisticsMap.keySet()) {
                          Set<String> shopIdSetss = new HashSet<>();
                          LogisticsGoodVO logisticsGoodVO = new LogisticsGoodVO();
                          List<LogisticsVO> logisticsVOList = logisticsMap.get(keyStr);

                          //此位置构造订单的基本信息
                          LogisticsBaseInfoVO logisticsBaseInfoVO = baseInfoMap.get(keyStr);
                          if (logisticsVOList != null && logisticsVOList.size() > 0) {
                              logisticsGoodVO.setCreateTime(logisticsBaseInfoVO.getCreateTime());

                              List<LogisticsGoodDetailVO> goodDetailList = new ArrayList<LogisticsGoodDetailVO>();
                              Map<String, LogisticsVO> logisticsVOMap = new HashMap<>();
                              for (LogisticsVO logisticsVO : logisticsVOList) {
                                  shopIdSetss.add(logisticsVO.getShopId());
                                  logisticsVOMap.put(logisticsVO.getShopId(), logisticsVO);
                              }
                              logisticsVOMap.forEach((k, v) -> {
                                  List<ShopTradeItemVO> tradeItemList = logisticsDAO.getShopTradItemList(v.getTradeId(), k);

                                  if (tradeItemList != null && tradeItemList.size() > 0) {
                                      for (ShopTradeItemVO itemVO : tradeItemList) {
                                          LogisticsGoodDetailVO goodDetailVO = new LogisticsGoodDetailVO();
                                          goodDetailVO.setCreateTime(v.getCreateTime());//下单时间
                                          goodDetailVO.setTradeId(v.getTradeId());//订单Id
                                          goodDetailVO.setShopId(v.getShopId());//店铺id
                                          goodDetailVO.setShopName(v.getShopName());//商店名称
                                          goodDetailVO.setUserId(v.getUserId());//店长Id
                                          goodDetailVO.setTradeNo(v.getTradeNo());
                                          goodDetailVO.setSendStatus(itemVO.getGoodStatus());//("订单状态,状态(0:未发货 1.已发货,2已经完成")
                                          goodDetailVO.setBuyNum(itemVO.getBuyNum());//购买数量
                                          goodDetailVO.setBuyPrice(itemVO.getBuyPrice());//购买价格
                                          goodDetailVO.setGoodName(itemVO.getGoodName());//商品名称
                                          goodDetailVO.setUserId(v.getUserId());

                                          goodDetailVO.setRecvName(v.getRecvName());
                                          goodDetailVO.setShopNo(v.getShopNo());
                                          goodDetailVO.setRecvMobile(v.getRecvMobile());
                                          goodDetailVO.setShopMobile(v.getShopMobile());
                                          goodDetailVO.setGoodId(itemVO.getGoodId());

                                          goodDetailVO.setInteriorNo(keyStr);

                                          String reciveAddress = "";
                                          if (v.getProvinceValue() != null) {
                                              reciveAddress = reciveAddress + v.getProvinceValue();
                                              ;
                                          }
                                          if (v.getCityValue() != null) {
                                              reciveAddress = reciveAddress + v.getCityValue();
                                          }
                                          if (v.getCountyValue() != null) {
                                              reciveAddress = reciveAddress + v.getCountyValue();
                                          }

                                          goodDetailVO.setDetailAddress(reciveAddress + v.getDetailAddress());//收货地址
                                          goodDetailList.add(goodDetailVO);
                                      }
                                  }
                                  logisticsGoodVO.setGoodDetailList(goodDetailList);

                                  ShopEntity shopEntity = shopDAO.getShopEntityByShopId(k);
                                  if (shopEntity != null) {
                                      logisticsBaseInfoVO.setShopId(k);
                                      UserEntity userEntity = userDAO.getById(shopEntity.getUserId());
                                      if (userEntity != null) {
                                          logisticsBaseInfoVO.setShopUserName(userEntity.getTrueName());
                                      }
                                  }
                              });
                          }
                          String shopUserNames = "";
                          String shopNos = "";
                          if (shopIdSetss != null && shopIdSetss.size() > 1) {

                              for (String shopId : shopIdSetss) {
                                  ShopUserBaseInfoVO shopUserBaseInfoVO = logisticsDAO.getShopUserBaseInfo(shopId);
                                  if (shopUserBaseInfoVO != null) {
                                      shopUserNames = shopUserNames + shopUserBaseInfoVO.getTrueName() +
                                              "(" + shopUserBaseInfoVO.getMobile() + "),";
                                      shopNos = shopNos + shopUserBaseInfoVO.getShopNo() + ",";
                                  }
                              }
                              logisticsBaseInfoVO.setShopUserName(shopUserNames);
                              logisticsBaseInfoVO.setShopNo(shopNos);
                          }

                          logisticsGoodVO.setLogisticsBaseVO(logisticsBaseInfoVO);
                          logisticsGoodVOList.add(logisticsGoodVO);
                      }
                  }
              }


              Comparator<LogisticsGoodVO> comparator = (logisticsGoodVO, logisticsGoodVO2) -> {
                  Long l1 = logisticsGoodVO.getLogisticsBaseVO().getCreateTime().getTime();
                  Long l2 = logisticsGoodVO2.getLogisticsBaseVO().getCreateTime().getTime();
                  if (l1 > l2) {
                      return -1;
                  }else if (l1 < l2) {
                      return 1;
                  } else {
                      return 0;
                  }
              };
              logisticsGoodVOList.sort(comparator);


              PageInfo<CurrentTimeVO> pageInfo = new PageInfo<>(currentTimeVoList);
              return CommonUtil.copyFromPageInfo(pageInfo, logisticsGoodVOList);
          }else{
              return CommonUtil.copyFromPageInfo(new PageInfo<>(new ArrayList<CurrentTimeVO>()), new ArrayList<LogisticsGoodVO>());
          }
     }catch (Exception e){
          return CommonUtil.copyFromPageInfo(new PageInfo<>(new ArrayList<CurrentTimeVO>()), new ArrayList<LogisticsGoodVO>());
      }
    }



    @ApiOperation(value = "商品物流订单详情", notes = "")
    @GetMapping("getLogisticsDetail")
    @ResponseBody
    public LogisticsDetailVO getLogisticsDetailVOList(@RequestParam(value = "interiorNo") String interiorNo) {
        A.check(interiorNo == null, "订单编号不能为空");
        LogisticsDetailVO logisticsDetail = new LogisticsDetailVO();
        //获取订单的基本信息
        List<LogisticsVO> logisticsVOList = logisticsDAO.getLogisticsVOByInteriorNo(interiorNo);
        //获取该订单的所有的商店,对相同的商店进行去重，因为同一个订单内，每一个商店的基本信息已经固定,
        //之后还需要对订单编号和物流信息进行拼接
        //商店Id 物流编号 物流名称
        Map<String,Map<String,String>> shopTransporMap = new HashMap<>();
        Map<String,LogisticsVO> shopBaseInfoMap = new HashMap<>();
        if(logisticsVOList!=null&&logisticsVOList.size()>0){
            for(LogisticsVO vo:logisticsVOList) {
                List<ShopTransportVO> shopTransportVOList = shopTransportDAO.getShopTranSportByShopIdAndTradeId(vo.getShopId(), vo.getTradeNo());
                if(shopTransportVOList!=null&&shopTransportVOList.size()>0){
                    for(ShopTransportVO transportVO:shopTransportVOList){
                        if(shopTransporMap.containsKey(transportVO.getShopId())){
                            Map<String,String> shopTransporNameMap =shopTransporMap.get(transportVO.getShopId());
                            if(!shopTransporNameMap.containsKey(transportVO.getTransportNo())){
                                shopTransporNameMap.put(transportVO.getTransportNo(),transportVO.getLogisticsName());
                                shopTransporMap.put(transportVO.getShopId(),shopTransporNameMap);
                            }
                        }else{
                            Map<String,String> shopTransporNameMap = new HashMap<String,String>();
                            shopTransporNameMap.put(transportVO.getTransportNo(),transportVO.getLogisticsName());
                            shopTransporMap.put(transportVO.getShopId(),shopTransporNameMap);
                        }
                    }
                }
                shopBaseInfoMap.put(vo.getShopId(),vo);
            }
        }

        //基础信息构造

        List<LogisticsVO> baseLogisticsList = new ArrayList<>();
        if(shopBaseInfoMap!=null&&shopBaseInfoMap.size()>0){
            for(String shopIdkey:shopBaseInfoMap.keySet()){
                LogisticsVO logisticsVO = shopBaseInfoMap.get(shopIdkey);
                String reciveAddress = "";
                if(logisticsVO.getProvinceValue()!=null) {
                    reciveAddress = reciveAddress+logisticsVO.getProvinceValue(); ;
                }
                if(logisticsVO.getCityValue()!=null){
                    reciveAddress = reciveAddress+logisticsVO.getCityValue();
                }
                if(logisticsVO.getCountyValue()!=null){
                    reciveAddress = reciveAddress+logisticsVO.getCountyValue();
                }
                String logisticInfo = "";
                if(shopTransporMap!=null&&shopTransporMap.size()>0){
                    Map<String,String> shopTransporNameMap = shopTransporMap.get(shopIdkey);
                    if(shopTransporNameMap!=null&&shopTransporNameMap.size()>0){
                        for(String tranNO:shopTransporNameMap.keySet()){
                            logisticInfo =logisticInfo+tranNO+"("+shopTransporNameMap.get(tranNO)+"),";
                        }
                    }
                }
                logisticsVO.setDetailAddress(reciveAddress+logisticsVO.getDetailAddress());
                logisticsVO.setLogisticsNo(logisticInfo);
                baseLogisticsList.add(logisticsVO);
            }
        }

        //====================================================================================//
        List<ShopTradeItemVO> shopTradeItemList = new ArrayList<ShopTradeItemVO>();
        if (baseLogisticsList != null && baseLogisticsList.size() > 0) {
            for (LogisticsVO logisticsVO : baseLogisticsList) {
                //获取订单的详情，获取某个订单的订单详情
                List<ShopTradeItemVO> tradeItemList = logisticsDAO.getShopTradItemList(logisticsVO.getTradeId(), logisticsVO.getShopId());
                if (tradeItemList != null && tradeItemList.size() > 0) {
                    //查询出物流信息
                    List<ShopTransportVO> shopTransportVOList = shopTransportDAO.getShopTranSportByShopIdAndTradeId(logisticsVO.getShopId(), logisticsVO.getTradeNo());
                    for (ShopTradeItemVO itemVO : tradeItemList) {
                        //首先构造商品目前的基本信息
                        if (itemVO.getGoodStatus() == 2||itemVO.getGoodStatus()==1) {
                            //说明商品有发货信息，这里需要对信息进行获取
                            if(shopTransportVOList!=null&&shopTransportVOList.size()>0){
                                for(ShopTransportVO transportVO:shopTransportVOList){
                                    if(itemVO.getGoodId().equals(transportVO.getGoodId())
                                            &&itemVO.getShopId().equals(transportVO.getShopId())
                                            &&logisticsVO.getTradeNo().equals(transportVO.getTradeNo())){
                                        itemVO.setLogisticsName(transportVO.getLogisticsName());
                                        itemVO.setLogisticsNo(transportVO.getTransportNo());
                                    }
                                }
                            }
                        }
                        ShopGoodInfoVO shopGoodInfoVO = logisticsDAO.getGoodTypeNameByGoodId(itemVO.getGoodId());
                        itemVO.setGoodType(shopGoodInfoVO.getGoodType());
                        itemVO.setUnit(shopGoodInfoVO.getUnit());
                        itemVO.setTradeNo(logisticsVO.getTradeNo());
                        //查询优惠券列表
                        List<CoupNamesVO> coupNamesVOList = logisticsDAO.getCoupNameListByGoodId(itemVO.getGoodId());
                        List<String> couponNameList = new ArrayList<>();
                        if(coupNamesVOList!=null&&coupNamesVOList.size()>0) {
                            for (CoupNamesVO vo:coupNamesVOList) {
                                couponNameList.add(vo.getCouponName());
                            }
                        }
                        itemVO.setCoupNameList(couponNameList);
                        shopTradeItemList.add(itemVO);
                    }
                }
            }
        }
        logisticsDetail.setLogisticsVOList(baseLogisticsList);
        logisticsDetail.setShopTradeItemList(shopTradeItemList);
        return logisticsDetail;
    }


    /**
     * 合并单
     */
    //
    @ApiOperation(value = "商品部分合单发货操作")
    @PostMapping("mergeGoodTrade")
    @ResponseBody
    public Success mergeGoodTrade(@RequestBody MergeTradeBaseInfoVO baseInfoVO) {
        A.check(baseInfoVO == null, "操作异常");
        A.check(baseInfoVO.getTradeReqsVOList() == null||baseInfoVO.getTradeReqsVOList().size()==0, "请选择商品");
        Success success = logisticsServiceImpl.mergeGoodTrade(baseInfoVO);
        userOperatorLogService.addUserOperatorLog(Subject.getCurrent().getId(),4,"发货");
        return success;
    }


    @ApiOperation(value = "物品批量合单发货", notes = "")
    @PostMapping("patchSendLogistics")
    @ResponseBody
    public Success patchSendLogistics(@RequestBody PatchSendBaseVO patchSend){
        return logisticsServiceImpl.patchSendLogistics(patchSend);
    }


    @ApiOperation(value = "获取商家物品物流信息excel")
    @PostMapping("logisticsExport1")
    @ResponseBody
    public void logisticsExport(@ApiParam(value = "excel导出请求数据")@RequestBody LogisticsExportVO logisticsExportVO,
                                HttpServletResponse response) throws IOException, NoSuchFieldException, IllegalAccessException  {
        String excelName =logisticsExportVO.getExcelName();
        List<LogisticsIdsVO> logisticsIdsVOS = logisticsExportVO.getLogisticsIdsVOS();
        A.check(StringUtils.isBlank(excelName),"请传入excel名称");
        //A.check(Collections.isEmpty(logisticsIdsVOS),"请勾选需要导出的订单商品");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");// // 指定文件的保存类型。
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(excelName, "utf-8") + ".xls");

        List<LogisticsExcelVO> logisticsExcelVOList = logisticsDAO.getLogisticsExcelVOList(logisticsIdsVOS);
        for (int i = 0; i < logisticsExcelVOList.size(); i++) {
            LogisticsExcelVO logisticsExcelVO = logisticsExcelVOList.get(i);
            logisticsExcelVO.setIndex(i+1);
            String detailAddress = logisticsExcelVO.getProvinceValue()+logisticsExcelVO.getCityValue()+logisticsExcelVO.getCountyValue()+logisticsExcelVO.getDetailAddress();
            logisticsExcelVO.setDetailAddress(detailAddress);
        }
        ExcelGenerator generator = new ExcelGenerator();
        ReportDefinition definition = new ReportDefinition(excelName);
        definition.addColumn(new ReportColumn("序号", "index"));
        definition.addColumn(new ReportColumn("下单时间", "createTime"));
        definition.addColumn(new ReportColumn("订单编号", "tradeNo"));
        definition.addColumn(new ReportColumn("店铺ID", "shopNo"));
        definition.addColumn(new ReportColumn("订单商品", "goodName"));
        definition.addColumn(new ReportColumn("商品数量", "buyNum"));
        definition.addColumn(new ReportColumn("收货人", "recvName"));
        definition.addColumn(new ReportColumn("配送地址", "detailAddress"));
        definition.addColumn(new ReportColumn("收货人电话", "recvMobile"));
        definition.addColumn(new ReportColumn("物流名称", "transportNo"));
        definition.addColumn(new ReportColumn("物流编号", "transportCompany"));
        ReportData reportData = new ReportData(definition, logisticsExcelVOList);
        generator.addSheet(reportData);
        @Cleanup OutputStream outputStream = response.getOutputStream();
        generator.write(outputStream);
    }


    @ApiOperation(value = "获取商家物品物流信息excel")
    @IgnoreJwtAuth
    @PostMapping("logisticsExport")
    @ResponseBody
    public void logisticsExport(@ApiParam(value = "excel导出请求数据")@RequestBody LogisticsRquestVO logisticsRquestVO,
                                HttpServletResponse response, HttpServletRequest request
                                )  {
        if(isBool){
            logger.info("导出繁忙中，请稍后再试");
            return;
        }
        isBool=true;
       // logisticsExcelVOList = new ArrayList<>();
        ExportToExcelUtil<LogisticsExcelVO> excelUtil = new ExportToExcelUtil<LogisticsExcelVO>();

        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response, "物流商品信息");
            //, "物流名称", "物流编号"
            String[] headers = {"序号", "下单时间", "订单编号", "店铺ID", "订单商品", "商品数量", "收货人", "配送地址", "收货人电话"};
            String[] columns = {"index", "createTime", "tradeNo", "shopNo", "goodName", "buyNum", "recvName", "detailAddress", "recvMobile"};

            List<LogisticsExcelVO> logisticsExcelVOList = new ArrayList<>();


            /**
             * 根据条件查询出所有的内部订单信息
             */

            List<CurrentTimeVO> currentTimeVoList = new ArrayList<CurrentTimeVO>();
           // currentTimeVoList = logisticsExportDAO.getCurrentTimeVOList(logisticsRquestVO);

                if (logisticsRquestVO != null) {
                    if (logisticsRquestVO.getStatus() == "" || logisticsRquestVO.equals("")) {
                        currentTimeVoList = logisticsExportDAO.getCurrentTimeVOList(logisticsRquestVO);
                    } else if (logisticsRquestVO.getStatus().equals("1")) {
                        currentTimeVoList = logisticsExportDAO.getInteriorNoListNoSend(logisticsRquestVO);
                    } else if (logisticsRquestVO.getStatus().equals("2")) {
                        currentTimeVoList = logisticsExportDAO.getInteriorNoListSended(logisticsRquestVO);
                    } else if (logisticsRquestVO.getStatus().equals("3")) {
                        currentTimeVoList = logisticsExportDAO.getInteriorNoListFinish(logisticsRquestVO);
                    } else if (logisticsRquestVO.getStatus().equals("4")) {
                        currentTimeVoList = logisticsExportDAO.getInteriorNoListPartSend(logisticsRquestVO);
                    }
                }

            if(currentTimeVoList!=null&&currentTimeVoList.size()>0) {
                List<InneriornoVO> inneriornoVOList = new ArrayList<InneriornoVO>();
                CurrentTimeResqVO currentTimeResqVO = new CurrentTimeResqVO();
                List<Long> currentListStr = new ArrayList<Long>();
                if (currentTimeVoList != null && currentTimeVoList.size() > 0) {
                    for (CurrentTimeVO vodate : currentTimeVoList) {
                        currentListStr.add(vodate.getCurrentTime());
                    }
                }
                currentTimeResqVO.setCurrentTimeVOList(currentListStr);
                inneriornoVOList = logisticsDAO.getInteriorNoList(currentTimeResqVO);
                currentTimeResqVO=null;
                List<String> noList = new ArrayList<>();
                if (inneriornoVOList != null && inneriornoVOList.size() > 0) {
                    for (InneriornoVO data : inneriornoVOList) {
                        noList.add(data.getInteriorNo());
                    }
                }
                //查询的结果不为空
                if (noList != null && noList.size() > 0) {
                    InneriornoResqVO inneriornoResqVO = new InneriornoResqVO();
                    inneriornoResqVO.setInneriornoList(noList);
                    if (logisticsRquestVO.getEndTime() != null && logisticsRquestVO.getGoodNames().size() > 0) {
                        inneriornoResqVO.setGoodNames(logisticsRquestVO.getGoodNames());
                    }
                    System.out.println("===================================================");
                    List<LogisticsVO> logisticsList = logisticsExportDAO.getLogisticsVOList(inneriornoResqVO);
                    inneriornoResqVO=null;
                    //将相同的下单号的订单放入到map中，目的是为了循环map进行数据库查询，可以减少对数据库的查询
                    System.out.println("===================================================1");

                    if (logisticsList != null && logisticsList.size() > 0) {
                        //map的长度是店的数量
                        int countNum =0;
                        //此位置构造订单的基本信息
                        if (logisticsList != null && logisticsList.size() > 0) {
                            //for(int i=0;i<10000;i++){
                           for(LogisticsVO v:logisticsList) {
                              //  LogisticsVO v = logisticsList.get(i);
                                //计数器
                                LogisticsExcelVO excelVO = new LogisticsExcelVO();
                                //序号
                                //("下单时间")
                                excelVO.setCreateTime(v.getCreateTime());
                                //订单编号
                                excelVO.setTradeNo(v.getTradeNo());
                                //店铺编号',
                                excelVO.setShopNo(v.getShopNo());
                                //购买数量
                                excelVO.setBuyNum(v.getBuyNum());
                                excelVO.setGoodName(v.getGoodName());
                                //收货人名字
                                excelVO.setRecvName(v.getRecvName());
                                //收货人电话
                                excelVO.setRecvMobile(v.getRecvMobile());

                                String reciveAddress = "";
                                if (v.getProvinceValue() != null) {
                                    reciveAddress = reciveAddress + v.getProvinceValue();
                                }
                                if (v.getCityValue() != null) {
                                    reciveAddress = reciveAddress + v.getCityValue();
                                }
                                if (v.getCountyValue() != null) {
                                    reciveAddress = reciveAddress + v.getCountyValue();
                                }
                                //收货地址
                                excelVO.setDetailAddress(reciveAddress + v.getDetailAddress());

                               // excelVO.setTransportCompany(itemVO.getLogisticsName());
                               // excelVO.setTransportNo(itemVO.getLogisticsNo());
                                logisticsExcelVOList.add(excelVO);


                            }
                        }
                    }
                }
            }

            logger.info("logisticsExcelVOList1.size()=",logisticsExcelVOList.size());
            Comparator<LogisticsExcelVO> comparator = (LogisticsExcelVO, LogisticsExcelVO2) -> {
                Long l1 = LogisticsExcelVO.getCreateTime().getTime();
                Long l2 = LogisticsExcelVO2.getCreateTime().getTime();

                if (l1 > l2) {
                    return -1;
                } else if (l1 < l2) {
                    return 1;
                } else {
                    return 0;
                }
            };
            logisticsExcelVOList.sort(comparator);
            List<LogisticsExcelVO> logisticsExcelVOList1 = new ArrayList<>();
            int countNum=0;
            for(LogisticsExcelVO vo:logisticsExcelVOList){
                countNum++;
                vo.setIndex(countNum);
                logisticsExcelVOList1.add(vo);
            }


            logger.info("========="+logisticsExcelVOList.size());
            excelUtil.exportExcel(headers, columns, logisticsExcelVOList1, out, request, "yyyy-MM-dd hh:mm:ss");
        }catch (Exception e){
            logger.error("订单导出异常",e);
        }finally {
            try {
                isBool=false;
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @ApiOperation(value = "获取商家物品物流信息excel")
    @IgnoreJwtAuth
    @PostMapping("logisticsExports")
    @ResponseBody
    public void logisticsExports(@ApiParam(value = "excel导出请求数据")@RequestBody LogisticsRquestVO logisticsRquestVO,
                                HttpServletResponse response, HttpServletRequest request
    )  {
        if(isBool){
            logger.info("导出繁忙中，请稍后再试");
            return;
        }
        isBool=true;
        logisticsExcelVOList = new ArrayList<>();
        ExportToExcelUtil<LogisticsExcelVO> excelUtil = new ExportToExcelUtil<LogisticsExcelVO>();

        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response, "物流商品信息");
            //, "物流名称", "物流编号"
            String[] headers = {"序号", "下单时间", "订单编号", "店铺ID", "订单商品", "商品数量", "收货人", "配送地址", "收货人电话"};
            String[] columns = {"index", "createTime", "tradeNo", "shopNo", "goodName", "buyNum", "recvName", "detailAddress", "recvMobile"};

            //List<LogisticsExcelVO> logisticsExcelVOList = new ArrayList<>();


            /**
             * 根据条件查询出所有的内部订单信息
             */

            List<CurrentTimeVO> currentTimeVoList = new ArrayList<CurrentTimeVO>();
             currentTimeVoList = logisticsExportDAO.getCurrentTimeVOList(logisticsRquestVO);
            if(2<1) {
            if (logisticsRquestVO != null) {
                if (logisticsRquestVO.getStatus() == "" || logisticsRquestVO.equals("")) {
                    currentTimeVoList = logisticsExportDAO.getCurrentTimeVOList(logisticsRquestVO);
                } if (logisticsRquestVO.getStatus().equals("1")) {
                    currentTimeVoList = logisticsExportDAO.getInteriorNoListNoSend(logisticsRquestVO);
                } if (logisticsRquestVO.getStatus().equals("2")) {
                    currentTimeVoList = logisticsExportDAO.getInteriorNoListSended(logisticsRquestVO);
                }  if (logisticsRquestVO.getStatus().equals("3")) {
                    currentTimeVoList = logisticsExportDAO.getInteriorNoListFinish(logisticsRquestVO);
                } if (logisticsRquestVO.getStatus().equals("4")) {
                    currentTimeVoList = logisticsExportDAO.getInteriorNoListPartSend(logisticsRquestVO);
                }
            }
            }
            if(currentTimeVoList!=null&&currentTimeVoList.size()>0) {
                List<InneriornoVO> inneriornoVOList = new ArrayList<InneriornoVO>();
                CurrentTimeResqVO currentTimeResqVO = new CurrentTimeResqVO();
                List<Long> currentListStr = new ArrayList<Long>();
                if (currentTimeVoList != null && currentTimeVoList.size() > 0) {
                    for (CurrentTimeVO vodate : currentTimeVoList) {
                        currentListStr.add(vodate.getCurrentTime());
                    }
                }
                currentTimeResqVO.setCurrentTimeVOList(currentListStr);
                inneriornoVOList = logisticsDAO.getInteriorNoList(currentTimeResqVO);
                currentTimeResqVO=null;
                List<String> noList = new ArrayList<>();
                if (inneriornoVOList != null && inneriornoVOList.size() > 0) {
                    for (InneriornoVO data : inneriornoVOList) {
                        noList.add(data.getInteriorNo());
                    }
                }
                //查询的结果不为空
                if (noList != null && noList.size() > 0) {
                    InneriornoResqVO inneriornoResqVO = new InneriornoResqVO();
                    inneriornoResqVO.setInneriornoList(noList);
                    if (logisticsRquestVO.getEndTime() != null && logisticsRquestVO.getGoodNames().size() > 0) {
                        inneriornoResqVO.setGoodNames(logisticsRquestVO.getGoodNames());
                    }
                    System.out.println("===================================================");
                    List<LogisticsVO> logisticsList = logisticsExportDAO.getLogisticsVOList(inneriornoResqVO);
                    inneriornoResqVO=null;
                    //将相同的下单号的订单放入到map中，目的是为了循环map进行数据库查询，可以减少对数据库的查询
                    System.out.println("===================================================1");
                    // Map<String, List<LogisticsVO>> logisticsMap = new HashMap<String, List<LogisticsVO>>();
                    //构造订单的基本信息
                    List<ShopTradeItemVO> tradeItemAll = logisticsExportDAO.getgetShopTradItemListAll();
                    if(tradeItemAll!=null&&tradeItemAll.size()>0){
                        int workNum = (tradeItemAll.size() + 999) / 1000;
                        ItemRun run = null;
                        countDownLatch = new CountDownLatch(workNum);
                        int count = 0;
                        for(ShopTradeItemVO itemVO:tradeItemAll){
                            if(count%1000==0){
                                if(run!=null){
                                    pool.execute(run);
                                    run=null;
                                }
                                run = new ItemRun();
                                run.countDownLatch = countDownLatch;
                            }
                            count++;
                            run.ls.add(itemVO);
                        }
                        if (run != null) {
                            pool.execute(run);
                        }
                        try {
                            countDownLatch.await();
                        } catch (InterruptedException ex) {
                            logger.warn("", ex);
                        }
                    }

                    logger.info("======订单详情完成=====");



                    //map的长度是店的数量
                    int countNum =0;
                    //此位置构造订单的基本信息
                    if (logisticsList != null && logisticsList.size() > 0) {
                        int workNum = (tradeItemAll.size() + 999) / 1000;
                        ExcelRun run = null;
                        countDownLatch = new CountDownLatch(workNum);
                        int count = 0;
                        for(LogisticsVO logisticsVO:logisticsList){
                            if(count%1000==0){
                                if(run!=null){
                                    pool.execute(run);
                                    run=null;
                                }
                                run = new ExcelRun();
                                run.countDownLatch = countDownLatch;
                            }
                            count++;
                            run.ls.add(logisticsVO);
                        }
                        if (run != null) {
                            pool.execute(run);
                        }
                        try {
                            countDownLatch.await();
                        } catch (InterruptedException ex) {
                            logger.warn("", ex);
                        }
                    }
                }
            }
            logger.info("gouz数据完成===================="+logisticsExcelVOList.size());
            logisticsExcelVOList.removeAll(Collections.singleton(null));
            logger.info("gouz数据完成2===================="+logisticsExcelVOList.size());
            Comparator<LogisticsExcelVO> comparator = (logisticsExcelVO, logisticsExcelVO2) -> {
                Long l1 = logisticsExcelVO.getCreateTime().getTime();
                Long l2 = logisticsExcelVO2.getCreateTime().getTime();
                if (l1 > l2) {
                    return -1;
                } if (l1 < l2) {
                    return 1;
                } else {
                    return 0;
                }
            };
            logisticsExcelVOList.sort(comparator);
            List<LogisticsExcelVO> logisticsExcelVOList1 = new ArrayList<>();
            int countNum=0;
            for(LogisticsExcelVO vo:logisticsExcelVOList){
                countNum++;
                vo.setIndex(countNum);
                logisticsExcelVOList1.add(vo);
            }
            logger.info("========="+logisticsExcelVOList.size());

            excelUtil.exportExcel(headers, columns, logisticsExcelVOList1, out, request, "yyyy-MM-dd hh:mm:ss");
        }catch (Exception e){
            logger.error("订单导出异常",e);
        }finally {
            try {
                isBool=false;
                logisticsExcelVOList = Collections.synchronizedList(new ArrayList<LogisticsExcelVO>());
                shopTradeItemMap.clear();
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //多线程构造订单信息
    public static class ItemRun implements Runnable{
        private static Logger logger = LoggerFactory.getLogger(Runnable.class);
        public List<ShopTradeItemVO> ls = new ArrayList<>(1000);
        public CountDownLatch countDownLatch;

        public void run(){
            try{

                long start = System.currentTimeMillis();
                addMap(ls);
               /* for(ShopTradeItemVO itemVO:ls){
                    if(shopTradeItemMap.containsKey(itemVO.getShopId())){
                        Map<String,List<ShopTradeItemVO>> numMap = shopTradeItemMap.get(itemVO.getShopId());
                        List<ShopTradeItemVO> list = numMap.get(itemVO.getTradeId());
                        if(list==null){
                            list=new ArrayList<ShopTradeItemVO>();
                        }
                        ShopTradeItemVO shopTradeItemVO = new ShopTradeItemVO();
                        shopTradeItemVO.setBuyNum(itemVO.getBuyNum());
                        shopTradeItemVO.setGoodName(itemVO.getGoodName());
                        list.add(shopTradeItemVO);
                        numMap.put(itemVO.getTradeId(),list);
                        shopTradeItemMap.put(itemVO.getShopId(),numMap);
                        continue;
                    }else{
                        Map<String,List<ShopTradeItemVO>> numMap = new HashMap<>();
                        List<ShopTradeItemVO> list = new ArrayList<>();
                        ShopTradeItemVO shopTradeItemVO = new ShopTradeItemVO();
                        shopTradeItemVO.setBuyNum(itemVO.getBuyNum());
                        shopTradeItemVO.setGoodName(itemVO.getGoodName());
                        list.add(shopTradeItemVO);
                        numMap.put(itemVO.getTradeId(),list);
                        shopTradeItemMap.put(itemVO.getShopId(),numMap);
                        continue;
                    }
                }*/
                countDownLatch.countDown();

                logger.info("trade detai run,size=" + ls.size() + ",time(ms)=" + (System.currentTimeMillis() -start));
            }catch (Exception e) {
                logger.info("trade detail mgoap Exception", e);
            }
        }
    }


    public static void addMap(List<ShopTradeItemVO> ls){
        lock.lock();
        try{
            for(ShopTradeItemVO itemVO:ls){
                if(shopTradeItemMap.containsKey(itemVO.getShopId())){
                    Map<String,List<ShopTradeItemVO>> numMap = shopTradeItemMap.get(itemVO.getShopId());

                    if(numMap.containsKey(itemVO.getTradeId())){
                        List<ShopTradeItemVO> list = numMap.get(itemVO.getTradeId());
                        ShopTradeItemVO shopTradeItemVO = new ShopTradeItemVO();
                        shopTradeItemVO.setBuyNum(itemVO.getBuyNum());
                        shopTradeItemVO.setGoodName(itemVO.getGoodName());
                        list.add(shopTradeItemVO);
                        numMap.put(itemVO.getTradeId(),list);
                        shopTradeItemMap.put(itemVO.getShopId(),numMap);
                    }else{
                        List<ShopTradeItemVO> list = new ArrayList<>();
                        ShopTradeItemVO shopTradeItemVO = new ShopTradeItemVO();
                        shopTradeItemVO.setBuyNum(itemVO.getBuyNum());
                        shopTradeItemVO.setGoodName(itemVO.getGoodName());
                        list.add(shopTradeItemVO);
                        numMap.put(itemVO.getTradeId(),list);
                        shopTradeItemMap.put(itemVO.getShopId(),numMap);
                    }
                }else{
                    Map<String,List<ShopTradeItemVO>> numMap = new HashMap<>();
                    List<ShopTradeItemVO> list = new ArrayList<>();
                    ShopTradeItemVO shopTradeItemVO = new ShopTradeItemVO();
                    shopTradeItemVO.setBuyNum(itemVO.getBuyNum());
                    shopTradeItemVO.setGoodName(itemVO.getGoodName());
                    list.add(shopTradeItemVO);
                    numMap.put(itemVO.getTradeId(),list);
                    shopTradeItemMap.put(itemVO.getShopId(),numMap);

                }
            }

        }finally {
            lock.unlock();
        }
    }




    //多线程构造将要导出的数据信息
    public static class ExcelRun implements Runnable{

        private static Logger logger = LoggerFactory.getLogger(ExcelRun.class);

        public List<LogisticsVO> ls = new ArrayList<LogisticsVO>(1000);
        public CountDownLatch countDownLatch;

        public void run() {
            try {

                /*long start = System.currentTimeMillis();
                for(LogisticsVO v:ls) {
                    List<ShopTradeItemVO> itemVOList = shopTradeItemMap.get(v.getShopId()).get(v.getTradeId());
                    if(itemVOList==null||itemVOList.size()==0){
                        continue;
                    }
                    for (ShopTradeItemVO itemVO : itemVOList) {

                        //计数器
                        LogisticsExcelVO excelVO = new LogisticsExcelVO();
                        //("下单时间")
                        excelVO.setCreateTime(v.getCreateTime());
                        //订单编号
                        excelVO.setTradeNo(v.getTradeNo());
                        //店铺编号',
                        excelVO.setShopNo(v.getShopNo());
                        //购买数量
                        excelVO.setBuyNum(itemVO.getBuyNum());
                        excelVO.setGoodName(itemVO.getGoodName());
                        //收货人名字
                        excelVO.setRecvName(v.getRecvName());
                        //收货人电话
                        excelVO.setRecvMobile(v.getRecvMobile());

                        String reciveAddress = "";
                        if (v.getProvinceValue() != null) {
                            reciveAddress = reciveAddress.concat(v.getProvinceValue());
                        }
                        if (v.getCityValue() != null) {
                            reciveAddress = reciveAddress.concat(v.getCityValue());
                        }
                        if (v.getCountyValue() != null) {
                            reciveAddress = reciveAddress.concat(v.getCountyValue());
                        }
                        //收货地址
                        excelVO.setDetailAddress(reciveAddress.concat(v.getDetailAddress()));

                        // excelVO.setTransportCompany(itemVO.getLogisticsName());
                        // excelVO.setTransportNo(itemVO.getLogisticsNo());
                        if(excelVO==null){
                            continue;
                        }
                        logisticsExcelVOList.add(excelVO);

                    }
                }*/
                addList(ls);
                countDownLatch.countDown();

               // logger.info("logic run,size=" + ls.size() + ",time(ms)=" + (System.currentTimeMillis() -start));

            } catch (Exception e) {
                logger.error("excel to construct run Excepion==", e);
            }
        }
    }

    public static void addList(List<LogisticsVO> ls){
        lock.lock();
        try{
            long start = System.currentTimeMillis();
            for(LogisticsVO v:ls) {
                List<ShopTradeItemVO> itemVOList = shopTradeItemMap.get(v.getShopId()).get(v.getTradeId());
                if(itemVOList!=null&&itemVOList.size()>0){
                    for (ShopTradeItemVO itemVO : itemVOList) {

                        //计数器
                        LogisticsExcelVO excelVO = new LogisticsExcelVO();
                        //("下单时间")
                        excelVO.setCreateTime(v.getCreateTime());
                        //订单编号
                        excelVO.setTradeNo(v.getTradeNo());
                        //店铺编号',
                        excelVO.setShopNo(v.getShopNo());
                        //购买数量
                        excelVO.setBuyNum(itemVO.getBuyNum());
                        excelVO.setGoodName(itemVO.getGoodName());
                        //收货人名字
                        excelVO.setRecvName(v.getRecvName());
                        //收货人电话
                        excelVO.setRecvMobile(v.getRecvMobile());

                        String reciveAddress = "";
                        if (v.getProvinceValue() != null) {
                            reciveAddress = reciveAddress.concat(v.getProvinceValue());
                        }
                        if (v.getCityValue() != null) {
                            reciveAddress = reciveAddress.concat(v.getCityValue());
                        }
                        if (v.getCountyValue() != null) {
                            reciveAddress = reciveAddress.concat(v.getCountyValue());
                        }
                        //收货地址
                        excelVO.setDetailAddress(reciveAddress.concat(v.getDetailAddress()));

                        // excelVO.setTransportCompany(itemVO.getLogisticsName());
                        // excelVO.setTransportNo(itemVO.getLogisticsNo());
                        if (excelVO == null) {
                            continue;
                        }
                        logisticsExcelVOList.add(excelVO);
                    }
                }
            }
        }finally {
            lock.unlock();
        }

    }

    //线程池处理数据
    public static void main(String args[]){
        String id="yuegquegqwuegqweuw";
        System.out.println(id.hashCode());
        AtomicBoolean  isBool =new AtomicBoolean(false);
        System.out.println(isBool.get());
        isBool.set(true);
        System.out.println(isBool.get());
    }

}
