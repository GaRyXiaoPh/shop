package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.mapper.SysSettingsMapper;
import cn.kt.mall.common.excel.DynamicHeader;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.management.admin.dao.AdminDAO;
import cn.kt.mall.management.admin.dao.FundOperationDAO;
import cn.kt.mall.management.admin.dao.TbCouponDAO;
import cn.kt.mall.management.admin.entity.AdminEntity;
import cn.kt.mall.management.admin.entity.CouponEntity;
import cn.kt.mall.management.admin.service.FundOperationService;
import cn.kt.mall.management.admin.vo.*;
import cn.kt.mall.management.admin.vo.CashRecordVO;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.mapper.ShopDAO;
import cn.kt.mall.shop.shop.vo.*;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 操作记录管理service实现类
 */
@Service
public class FundOperationServiceImpl implements FundOperationService {
    @Autowired
    private FundOperationDAO fundOperationDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ShopDAO shopDAO;
    @Autowired
    private AdminDAO adminDAO;
    @Autowired
    private TbCouponDAO tbCouponDAO;
    @Autowired
    private SysSettingsMapper settingsMapper;


    private static Logger logger = Logger.getLogger(FundOperationServiceImpl.class);

    /**
     * 查询POPC解冻记录
     *
     * @param type
     * @param beginTime
     * @param endTime
     * @param minNum
     * @param maxNum
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public CommonPageVO<UnfreezeLogVO> getPopcList(String type, String beginTime, String endTime, BigDecimal minNum, BigDecimal maxNum, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<UnfreezeLogVO> list = fundOperationDAO.getPopcList(type, beginTime, endTime, minNum, maxNum, rowBounds);
        for (UnfreezeLogVO unfreezeLogVO : list) {
            UserEntity userEntity = userDAO.getById(unfreezeLogVO.getUserId());
            if (userEntity != null) {
                unfreezeLogVO.setTrueName(userEntity.getTrueName());
                unfreezeLogVO.setMobile(userEntity.getUsername());
            } else unfreezeLogVO.setTrueName("系统中已无该用户信息");
        }
        PageInfo<UnfreezeLogVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    /**
     * 获取店铺销售记录列表
     *
     * @param shopType
     * @param beginTime
     * @param endTime
     * @param shopNo
     * @param shopName
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public CommonPageVO<ShopRespVO> getShopList(String shopType, String beginTime, String endTime, String shopNo, String shopName, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<ShopRespVO> list = fundOperationDAO.getShopList(shopType, shopNo, shopName, rowBounds);
        if (list.size() > 0) {
            BigDecimal allSalePerformance = fundOperationDAO.getAllPerformance();
            list.get(0).setAllSalePerformance(allSalePerformance);
        }
        for (ShopRespVO shopRespVO : list) {
            BigDecimal totalPerformance = fundOperationDAO.getPerformance(shopRespVO.getId(), beginTime, endTime);
            shopRespVO.setSalePerformance(totalPerformance);
        }
        PageInfo<ShopRespVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

 /*   @Override
    public CommonPageVO<ShopStatisticsVO> getShopPerformanceList(String startTime, String endTime, String shopName, String userName, int pageNo, int pageSize) {
        //查询所有店铺列表
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<ShopStatisticsVO> voList = shopDAO.getShopStatisticsList(null, startTime, endTime,null, rowBounds);
        for (ShopStatisticsVO vo : voList) {
            Map<String, Object> UserMap = new HashMap<String, Object>();
            UserMap = this.getUserPid(vo.getUserId(), UserMap);
            if (UserMap != null) {
                vo.setPersonCount(String.valueOf(UserMap.size()));
            }

        }
        PageInfo<ShopStatisticsVO> pageInfo = new PageInfo<>(voList);
        return CommonUtil.copyFromPageInfo(pageInfo, voList);
    }*/

    /**
     * @param pid     父ID
     * @param hashMap 数据集
     * @return
     */
    private Map<String, Object> getUserPid(String pid, Map<String, Object> hashMap) {
        //首次递归查询自身
        List<UserEntity> userEntityList = userDAO.getUserByPid(pid);
        if (userEntityList.size() > 0) {
            for (UserEntity userEntity : userEntityList) {
                hashMap.put(userEntity.getId(), userEntity);
                this.getUserPid(userEntity.getId(), hashMap);
            }
        }
        //根据子节点
        return hashMap;
    }

    /**
     * 获取自资金明细
     *
     * @param opreatorUser
     * @param beginTime
     * @param endTime
     * @param phone
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public CommonPageVO<CashRecordVO> getCashRecordList(String opreatorUser, String operationType, String beginTime, String endTime, String phone, String status, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<CashRecordVO> list = fundOperationDAO.getFundDetailList(opreatorUser, operationType, beginTime, endTime, phone, status, rowBounds);
        List<CashRecordVO> newList = new ArrayList<CashRecordVO>();
        for (CashRecordVO cashRecordVO : list) {
            cashRecordVO = getInfo(cashRecordVO);
        }
        PageInfo<CashRecordVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    /**
     * 获取系统操作日志
     *
     * @param account
     * @param startTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public CommonPageVO<UserOperatorLogVO> getOperationLog(String account, String startTime, String endTime, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<UserOperatorLogVO> list = fundOperationDAO.getOperationLog(account, startTime, endTime, rowBounds);
        for (UserOperatorLogVO UserOperatorLogVO : list) {
            if (StringUtils.isBlank(UserOperatorLogVO.getPosition())) {
                UserOperatorLogVO.setPosition("无");
            }
        }
        PageInfo<UserOperatorLogVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    @Override
    public List<UserOperatorLogVO> getOperatorLogList(UserOperatorLogReqVO userOperatorLogReqVO) {
        List<UserOperatorLogVO> list = new ArrayList<>();
        try {
            list = fundOperationDAO.getOperationLog(userOperatorLogReqVO);
            for (UserOperatorLogVO vo : list) {
                if (vo.getPosition() == null) {
                    vo.setPosition("无");
                }
            }
            return list;
        } catch (Exception e) {
            logger.info(e);
        }
        return list;
    }

    /**
     * 查询所有符合条件的资金变动列表 -- 导出
     *
     * @param cashRecordReqVO
     * @return
     */
    @Override
    public List<CashRecordVO> getFundOperationList(CashRecordReqVO cashRecordReqVO) {
        List<CashRecordVO> newList = new ArrayList<>();
        Map<String,String> userMap = new HashMap<>();
        Map<String,String> map = new HashMap<>();
        Map<String,String> adminMap = new HashMap<>();
        Map<String,String> operationMap = new HashMap<>();
        try {
            //1.获取资金操作记录列表
            List<CashRecordVO> list = fundOperationDAO.getFundDetailList(cashRecordReqVO);
            //2.获取被操作用户的相关信息
            List<UserEntity> userList = userDAO.getRechargeUserByUserId();
            for (UserEntity user : userList) {
                userMap.put(user.getId(), user.getTrueName() + user.getMobile());
            }
            //3.获取操作人用户的相关信息---用户表来源
            List<UserEntity> userLists = userDAO.getUserByUserId();
            for (UserEntity user : userLists) {
                map.put(user.getId(), user.getTrueName() + user.getMobile());
            }
            //4.获取操作人用户的相关信息---admin表来源
            List<AdminEntity> adminList = adminDAO.getAdminList();
            for (AdminEntity adminEntity : adminList){
                adminMap.put(adminEntity.getId(),adminEntity.getAccount());
            }
            //5.获取操作人类型
            List<ShopEntity> shopList = shopDAO.getAllShop();
            for(ShopEntity shopEntity : shopList){
                operationMap.put(shopEntity.getUserId(),shopEntity.getShopLevel());
            }
            for (CashRecordVO vo : list) {
                //vo = getInfo(vo);
                //翻译状态
                if (vo.getStatus().equals("0")) {
                    vo.setStatus("未审核");
                }
                if (vo.getStatus().equals("1")) {
                    vo.setStatus("已通过");
                }
                if (vo.getStatus().equals("2")) {
                    vo.setStatus("已拒绝");
                }
                if (vo.getOperationType().equals("1")) {
                    vo.setOperationType("充值");
                } else if (vo.getOperationType().equals("2")) {
                    vo.setOperationType("扣除");
                } else vo.setOperationType("未知");
                //翻译被操作人
                vo.setRechargeAll(userMap.get(vo.getRechargeUser()));
                //翻译操作人
                if(null != map.get(vo.getUserId())){
                    vo.setOpreatorAll(map.get(vo.getUserId()));
                    vo.setShopType(operationMap.get(vo.getUserId()));
                }else if(null != adminMap.get(vo.getUserId())){
                    vo.setOpreatorAll(adminMap.get(vo.getUserId()));
                    vo.setShopType("平台");
                }else {
                    vo.setOpreatorAll("未知");
                    vo.setShopType("未知");
                }

                newList.add(vo);
            }
            return newList;
        } catch (Exception e) {
            logger.info(e);
        }
        return newList;
    }


    public CashRecordVO getInfo(CashRecordVO cashRecordVO) {
        //翻译操作类型
        if (cashRecordVO.getOperationType().equals("1")) {
            cashRecordVO.setOperationType("充值");
        } else if (cashRecordVO.getOperationType().equals("2")) {
            cashRecordVO.setOperationType("扣除");
        } else cashRecordVO.setOperationType("未知");
        UserEntity user = userDAO.getById(cashRecordVO.getRechargeUser());
        if (user != null) {
            if(user.getTrueName() != null){
                cashRecordVO.setTrueName(user.getTrueName());
            }
            if(user.getMobile() != null){
                cashRecordVO.setPhone(user.getMobile());
            }
        } else {
            cashRecordVO.setTrueName("未知");
        }
        //获取操作人信息
        UserEntity userEntity = userDAO.getById(cashRecordVO.getUserId());
        List<ShopEntity> shop = shopDAO.getMyShopEntity(cashRecordVO.getUserId());
        if (userEntity != null) {
            if (userEntity.getTrueName() != null) {
                //cashRecordVO.setTrueName(userEntity.getTrueName());
                cashRecordVO.setOpreatorAll(userEntity.getTrueName());
            }
            if (userEntity.getMobile() != null) {
                cashRecordVO.setLoginName(userEntity.getMobile());
            }

            //cashRecordVO.setLoginName(userEntity.getMobile());
            //添加操作人店铺信息
            if (shop != null && shop.size() > 0) {
                if (shop.get(0).getShopLevel().equals("2")) {
                    cashRecordVO.setShopType("零售店");
                } else if (shop.get(0).getShopLevel().equals("3")) {
                    cashRecordVO.setShopType("批发店");
                } else cashRecordVO.setShopType("未知");
            }
        } else {
            AdminEntity adminEntity = adminDAO.getById(cashRecordVO.getUserId());
            if (adminEntity != null) {
                cashRecordVO.setOpreatorUser(adminEntity.getAccount());
                cashRecordVO.setOpreatorAll(adminEntity.getAccount());
                cashRecordVO.setShopType("平台");
            } else {
                cashRecordVO.setOpreatorUser("未知");
                cashRecordVO.setOpreatorAll("未知");
                //cashRecordVO.setLoginName("无此账号");
            }
        }
        return cashRecordVO;
    }

    @Override
    public CommonPageVO<ShopSalesVO> getShopSalesRecord(String shopId, String startTime, String endTime, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        ShopEntity shopEntity = shopDAO.getShopEntityByShopId(shopId);
        A.check(shopEntity ==null,"获取店铺信息出错");
        List<ShopSalesVO> list = fundOperationDAO.shopSalesRecord(shopId, startTime, endTime, rowBounds);
        //Map<String, Object> UserMap = new HashMap<String, Object>();
        //UserMap = this.getUserPid(shopEntity.getUserId(), UserMap);
        BigDecimal totalPrice = new BigDecimal(0);
        for (ShopSalesVO shopSalesVO : list) {
            UserEntity userEntity = userDAO.getUserByIdAndPid(shopSalesVO.getBuyUserId(),shopEntity.getUserId());
            //UserEntity userEntity = (UserEntity) UserMap.get(shopSalesVO.getBuyUserId());
            if (userEntity != null) {
                shopSalesVO.setRelationship("下级");
            } else {
                shopSalesVO.setRelationship("无");
            }
            totalPrice = totalPrice.add(shopSalesVO.getBaseTotal());
        }
        for (ShopSalesVO shopSalesVO : list) {
            shopSalesVO.setTotalPrice(totalPrice);
        }
        //查询关系
        PageInfo<ShopSalesVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    @Override
    public CommonPageVO<CouponTransferVO> getCouponTransfer(String beginTime, String endTime, String rollOutAccount, String rollInAccount, int pageNo, int pageSize) {
        List<CouponTransferVO> newList = new ArrayList<>();
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<CouponTransferVO> list = fundOperationDAO.getCouponTransfer(rollInAccount, rollOutAccount, beginTime, endTime, rowBounds);
        for (CouponTransferVO vo : list) {
            vo = getCouponInfo(vo);
            newList.add(vo);
        }

        PageInfo<CouponTransferVO> pageInfo = new PageInfo<>(newList);
        return CommonUtil.copyFromPageInfo(pageInfo, newList);
    }

    /**
     * 查询优惠券转让记录列表 -- 导出
     *
     * @return
     */
    @Override
    public List<CouponTransferVO> getCouponList(CouponTransferReqVO couponTransferReqVO) {
        List<CouponTransferVO> newList = new ArrayList<>();
        Map<String,String> userMap = new HashMap<>();
        Map<String,String> map = new HashMap<>();
        try {
            //1.获取转出用户的相关信息
            List<UserEntity> userList = userDAO.getTransferOutByUserId();
            for (UserEntity user : userList) {
                userMap.put(user.getId(), user.getTrueName() + user.getMobile());
            }
            //2.获取转入用户的相关信息
            List<UserEntity> userLists = userDAO.getTransferInByUserId();
            for (UserEntity user : userLists) {
                map.put(user.getId(), user.getTrueName() + user.getMobile());
            }
            //3.查询优惠券转让记录
            List<CouponTransferVO> list = fundOperationDAO.getCouponTransfer(couponTransferReqVO);
            //4.获取转让利率
            List<SysSettings> lists = settingsMapper.getCouponParam();
            Map<String, Object> mapRatio = new HashMap<String, Object>();
            for (SysSettings sysSettings : lists) {
                mapRatio.put(sysSettings.getCode(), sysSettings.getLabel());
            }

            for (CouponTransferVO vo : list) {
                //vo = getCouponInfo(vo);
                vo.setTrueNameAll(userMap.get(vo.getRollOutUserId()));
                vo.setNameAll(map.get(vo.getRollInUserId()));
                if (vo.getRelation().equals("1")) {
                    vo.setRelation("同站");
                    vo.setFeeRatio(new BigDecimal(mapRatio.get("inside_ratio").toString()));
                } else if (vo.getRelation().equals("2")) {
                    vo.setRelation("异站");
                    vo.setFeeRatio(new BigDecimal(mapRatio.get("outside_ratio").toString()));
                } else vo.setRelation("未知");
                newList.add(vo);
            }
            return newList;
        } catch (Exception e) {
            logger.info(e);
        }
        return newList;
    }


    public CouponTransferVO getCouponInfo(CouponTransferVO vo) {
        UserEntity user = userDAO.getById(vo.getRollInUserId());
        if (user != null) {
            vo.setName(user.getTrueName());
            vo.setPhone(user.getMobile());
            if (user.getTrueName() != null) {
                vo.setNameAll(user.getTrueName());
                if (user.getMobile() != null) {
                    vo.setNameAll(user.getTrueName() + user.getMobile());
                }
            } else {
                vo.setNameAll(user.getMobile());
            }
        }
        user = userDAO.getById(vo.getRollOutUserId());
        if (user != null) {
            vo.setTrueName(user.getTrueName());
            vo.setMobile(user.getMobile());
            if (user.getTrueName() != null) {
                vo.setTrueNameAll(user.getTrueName());
                if (user.getMobile() != null) {
                    vo.setTrueNameAll(user.getTrueName() + user.getMobile());
                }
            } else {
                vo.setTrueNameAll(user.getMobile());
            }
        }
        //获取转让利率
        List<SysSettings> lists = settingsMapper.getCouponParam();
        Map<String, Object> map = new HashMap<String, Object>();
        for (SysSettings sysSettings : lists) {
            map.put(sysSettings.getCode(), sysSettings.getLabel());
        }
        if (vo.getRelation().equals("1")) {
            vo.setRelation("同站");
            vo.setFeeRatio(new BigDecimal(map.get("inside_ratio").toString()));
        } else if (vo.getRelation().equals("2")) {
            vo.setRelation("异站");
            vo.setFeeRatio(new BigDecimal(map.get("outside_ratio").toString()));
        } else vo.setRelation("关系不确定");

        return vo;
    }

    /**
     * 查询赠送记录
     *
     * @param beginTime
     * @param endTime
     * @param iphone
     * @param ids
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public CommonPageVO<CouponVO> getGivingRecord(String beginTime, String endTime, String iphone, List<String> ids, int pageNo, int pageSize) {
        List<CouponVO> newList = new ArrayList<>();
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<CouponVO> list = fundOperationDAO.getGivingRecord(beginTime, endTime, iphone, ids, rowBounds);
        PageInfo<CouponVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    /**
     * 导出赠送记录
     *
     * @param couponGiveReqVO
     * @return
     */
    @Override
    public List<CouponVO> getGivingList(CouponGiveReqVO couponGiveReqVO) {
        List<CouponVO> newList = new ArrayList<>();
        try {
            List<CouponVO> list = fundOperationDAO.getGivingRecord(couponGiveReqVO);
            for (CouponVO vo : list) {
                if (vo != null) {
                    if (vo.getTrueName() != null) {
                        vo.setTrueName(vo.getTrueName());
                        if (vo.getMobile() != null) {
                            vo.setTrueName(vo.getTrueName() + vo.getMobile());
                        }
                    } else {
                        if (vo.getMobile() != null) {
                            vo.setTrueName(vo.getMobile());
                        }
                    }
                }
               // vo.setTrueName(vo.getTrueName() + vo.getMobile());
                newList.add(vo);
            }
            return newList;
        } catch (Exception e) {
            logger.info(e);
        }
        return newList;
    }

    /**
     * 获取历史盈利数据表头
     *
     * @return
     */
    public List<HeadVO> searchHead() {
        List<CouponEntity> list = tbCouponDAO.getCouponsList();
        List<HeadVO> headList = new ArrayList<HeadVO>();
        HeadVO headVO = null;
       /* headVO = new HeadVO();
        headVO.setHeadName("总信用金");
        headVO.setHeadValue("收入总信用金");
        headList.add(headVO);
        headVO = new HeadVO();
        headVO.setHeadName("总优惠券");
        headVO.setHeadValue("收入总优惠券");
        headList.add(headVO);*/
        for (CouponEntity couponEntity : list) {
            headVO = new HeadVO();
            headVO.setHeadName(couponEntity.getCouponName());
            headVO.setHeadValue("赠送总" + couponEntity.getCouponName());
            headList.add(headVO);
        }
        return headList;
    }

    /**
     * 获取历史盈利数据
     *
     * @param beginTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    public CommonPageVO<HistoryProfitVO> getHistoryProfit(String beginTime, String endTime, int pageNo, int pageSize) {
        //获取时间列表
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<HistoryProfitVO> timeList = fundOperationDAO.getTradeTimeList(beginTime, endTime, rowBounds);

        for (HistoryProfitVO vo : timeList) {
            //根据时间获取收入相关数据
            List<HistoryProfitVO> listIn = fundOperationDAO.getInHistoryProfit(vo.getCreateTime());
            for (HistoryProfitVO voOne : listIn) {
                vo.setIncomeTotalCoupon(voOne.getIncomeTotalCoupon());
                vo.setIncomeTotalCredit(voOne.getIncomeTotalCredit());
            }
            //根据时间获取赠送相关数据
            List<HistoryProfitVO> listOut = fundOperationDAO.getOutHistoryProfit(vo.getCreateTime());
            //查询表头名称
            List<CouponEntity> list = tbCouponDAO.getCouponsList();
            HeadDataVO headDataVO = null;
            List<HeadDataVO> headList = new ArrayList<HeadDataVO>();
            for (HistoryProfitVO voTwo : listOut) {
                headDataVO = new HeadDataVO();
                headDataVO.setHeadName(voTwo.getCouponName());
                headDataVO.setHeadvalue(voTwo.getRechargeNum());
                /*if (voTwo.getCouponName().equals("优惠券")) {
                    //优惠券来源有两处，1.tb_user_coupon_log 2.tb_user_return_coupon_log
                    BigDecimal couponTotal = fundOperationDAO.getCouponCount(vo.getCreateTime());
                    headDataVO.setHeadvalue(couponTotal.add(voTwo.getRechargeNum()));
                } else headDataVO.setHeadvalue(voTwo.getRechargeNum());*/
                headList.add(headDataVO);
            }
            if (headList.size() > 0) {
                headList = constructorList(headList);
            }

            List<DynamicHeader> headers = new ArrayList<>();
            for (HeadDataVO v : headList) {
                headers.add(v);

            }
            vo.setDataNameValue(headers);
        }
        PageInfo<HistoryProfitVO> pageInfo = new PageInfo<>(timeList);
        return CommonUtil.copyFromPageInfo(pageInfo, timeList);
    }

    private List<HeadDataVO> constructorList(List<HeadDataVO> headList) {
        //查询表头名称
        List<CouponEntity> list = tbCouponDAO.getCouponsList();
        List<String> strings = new ArrayList<String>();
        List<String> stringss = new ArrayList<String>();
        for (CouponEntity couponEntity : list) {
            strings.add(couponEntity.getCouponName());

        }
        for (HeadDataVO headDataVO : headList) {
            stringss.add(headDataVO.getHeadName());
        }
        strings.removeAll(stringss);
        for (int i = 0; i < strings.size(); i++) {
            HeadDataVO vo = new HeadDataVO();
            vo.setHeadName(strings.get(i));
            vo.setHeadvalue(new BigDecimal(0));
            headList.add(vo);
        }
        return headList;
    }

    public List<HistoryProfitVO> getHistoryProfitList(HistoryProfitReqVO historyProfitReqVO) {
        List<HistoryProfitVO> newList = new ArrayList<>();
        try {
            List<HistoryProfitVO> timeList = fundOperationDAO.getTradeTimeList(historyProfitReqVO);
            for (HistoryProfitVO vo : timeList) {
                //根据时间获取收入相关数据
                List<HistoryProfitVO> listIn = fundOperationDAO.getInHistoryProfit(vo.getCreateTime());
                for (HistoryProfitVO voOne : listIn) {
                    vo.setIncomeTotalCoupon(voOne.getIncomeTotalCoupon());
                    vo.setIncomeTotalCredit(voOne.getIncomeTotalCredit());
                }
                //根据时间获取赠送相关数据
                List<HistoryProfitVO> listOut = fundOperationDAO.getOutHistoryProfit(vo.getCreateTime());
               /* for (HistoryProfitVO voTwo : listOut) {
                    if (voTwo.getCouponName().equals("优惠券")) {
                        vo.setGiveTotalCoupon(voTwo.getRechargeNum());
                        //类型1为优惠券，来源有两处，1.tb_user_coupon_log 2.tb_user_return_coupon_log
                        *//*BigDecimal couponTotal = fundOperationDAO.getCouponCount(vo.getCreateTime());
                        if (couponTotal != null) {
                            if (vo.getGiveTotalCoupon() == null) {
                                vo.setGiveTotalCoupon(couponTotal);
                            } else vo.setGiveTotalCoupon(vo.getGiveTotalCoupon().add(couponTotal));
                        }*//*
                    } else if (voTwo.getCouponName().equals("彩票积分")) {
                        vo.setGiveTotalLottery(voTwo.getRechargeNum());
                    } else if (voTwo.getCouponName().equals("债权积分")) {
                        vo.setGiveTotalIbot(voTwo.getRechargeNum());
                    }
                    *//*else if (voTwo.getCouponName().equals("游戏积分")) {
                        vo.setGiveTotalGame(voTwo.getRechargeNum());
                    } *//*
                    else if (voTwo.getCouponName().equals("保险积分")) {
                        vo.setGiveTotalnsurance(voTwo.getRechargeNum());
                    }
                }
                newList.add(vo);
            }
            return newList;*/
                //查询表头名称
                List<CouponEntity> list = tbCouponDAO.getCouponsList();
                HeadDataVO headDataVO = null;
                List<HeadDataVO> headList = new ArrayList<HeadDataVO>();
                for (HistoryProfitVO voTwo: listOut) {
                    headDataVO = new HeadDataVO();
                    headDataVO.setHeadName(voTwo.getCouponName());
                   /* if(voTwo.getCouponName().equals("优惠券")){
                        //优惠券来源有两处，1.tb_user_coupon_log 2.tb_user_return_coupon_log
                        BigDecimal couponTotal = fundOperationDAO.getCouponCount(vo.getCreateTime());
                        headDataVO.setHeadvalue(couponTotal.add(voTwo.getRechargeNum()));
                    }else */
                        headDataVO.setHeadvalue(voTwo.getRechargeNum());
                    headList.add(headDataVO);
                }
                if(headList.size() > 0){
                    headList=  constructorList(headList);
                }
                List<DynamicHeader> headers = new ArrayList<>();
                for (HeadDataVO v :headList) {
                    v.setHeadName("赠送总" + v.getHeadName());
                    headers.add(v);
                }
                vo.setDataNameValue(headers);
                newList.add(vo);
            }
            return newList;
        } catch (Exception e) {
            logger.info(e);
        }
        return newList;
    }
/*    public List<HistoryProfitVO> getHistoryProfitList(HistoryProfitReqVO historyProfitReqVO){
        List<HistoryProfitVO> newList = new ArrayList<>();
        try {
            List<HistoryProfitVO> timeList = fundOperationDAO.getTradeTimeList(historyProfitReqVO);
            for (HistoryProfitVO vo: timeList) {
                //根据时间获取收入相关数据
                List<HistoryProfitVO> listIn = fundOperationDAO.getInHistoryProfit(vo.getCreateTime());
                for (HistoryProfitVO voOne: listIn) {
                    vo.setIncomeTotalCoupon(voOne.getIncomeTotalCoupon());
                    vo.setIncomeTotalCredit(voOne.getIncomeTotalCredit());
                }
                //根据时间获取赠送相关数据
                List<HistoryProfitVO> listOut = fundOperationDAO.getOutHistoryProfit(vo.getCreateTime());
                //查询表头名称
                List<CouponEntity> list = tbCouponDAO.getCouponsList();
                HeadDataVO headDataVO = null;
                List<HeadDataVO> headList = new ArrayList<HeadDataVO>();
                for (HistoryProfitVO voTwo: listOut) {
                    headDataVO = new HeadDataVO();
                    headDataVO.setHeadName(voTwo.getCouponName());
                    if(voTwo.getCouponName().equals("优惠券")){
                        //优惠券来源有两处，1.tb_user_coupon_log 2.tb_user_return_coupon_log
                        BigDecimal couponTotal = fundOperationDAO.getCouponCount(vo.getCreateTime());
                        headDataVO.setHeadvalue(couponTotal.add(voTwo.getRechargeNum()));
                    }else headDataVO.setHeadvalue(voTwo.getRechargeNum());
                    headList.add(headDataVO);
                }
                if(headList.size() > 0){
                    headList=  constructorList(headList);
                }
                vo.setDataNameValue(headList);
                newList.add(vo);
            }
            return newList;
        }catch (Exception e){
            logger.info(e);
        }
        return newList;
    }*/
   /* public List<HistoryProfitVO> getHistoryProfitList(HistoryProfitReqVO historyProfitReqVO){
        List<HistoryProfitVO> newList = new ArrayList<>();
        try {
            List<HistoryProfitVO> timeList = fundOperationDAO.getTradeTimeList(historyProfitReqVO);
            for (HistoryProfitVO vo: timeList) {
                //根据时间获取收入相关数据
                List<HistoryProfitVO> listIn = fundOperationDAO.getInHistoryProfit(vo.getCreateTime());
                for (HistoryProfitVO voOne: listIn) {
                    vo.setIncomeTotalCoupon(voOne.getIncomeTotalCoupon());
                    vo.setIncomeTotalCredit(voOne.getIncomeTotalCredit());
                }
                //根据时间获取赠送相关数据
                List<HistoryProfitVO> listOut = fundOperationDAO.getOutHistoryProfit(vo.getCreateTime());
                //查询表头名称
                List<CouponEntity> list = tbCouponDAO.getCouponsList();
                HeadDataVO headDataVO = null;
                List<HeadDataVO> headList = new ArrayList<HeadDataVO>();
                for (HistoryProfitVO voTwo: listOut) {
                    for (CouponEntity couponEntity: list) {
                        if(voTwo.getCouponName().equals(couponEntity.getCouponName())){
                            headDataVO = new HeadDataVO();
                            if(voTwo.getCouponName().equals("优惠券")){
                                //优惠券来源有两处，1.tb_user_coupon_log 2.tb_user_return_coupon_log
                                BigDecimal couponTotal = fundOperationDAO.getCouponCount(vo.getCreateTime());
                                if(couponTotal != null){
                                    headDataVO.setHeadName(couponEntity.getCouponName());
                                    headDataVO.setHeadvalue(voTwo.getRechargeNum().add(couponTotal));
                                }else {
                                    headDataVO.setHeadName(couponEntity.getCouponName());
                                    headDataVO.setHeadvalue(voTwo.getRechargeNum());
                                }
                            }else {
                                headDataVO.setHeadName(couponEntity.getCouponName());
                                headDataVO.setHeadvalue(voTwo.getRechargeNum());
                            }
                            headList.add(headDataVO);
                            vo.setDataNameValue(headList);
                        }
                    }
                }
                newList.add(vo);
            }
            return newList;
        }catch (Exception e){
            logger.info(e);
        }
        return newList;
    }*/
}
