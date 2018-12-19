package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.excel.*;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Result;
import cn.kt.mall.common.http.Success;

import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.management.admin.entity.CouponEntity;
import cn.kt.mall.management.admin.entity.CouponTimeConfigEntity;
import cn.kt.mall.management.admin.service.FundOperationService;
import cn.kt.mall.management.admin.service.impl.CouponServiceImpl;

import cn.kt.mall.management.admin.service.impl.CouponTimeConfigServiceImpl;
import cn.kt.mall.management.admin.vo.CouponTransferVO;
import cn.kt.mall.management.admin.vo.ExtractVO;
import cn.kt.mall.management.admin.vo.UserStatementVO;
import cn.kt.mall.management.admin.vo.coupon.CouponVo;
import cn.kt.mall.shop.coupon.mapper.CouponsDAO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(description = "最新优惠券接口优惠券管理", tags = "coupon-management")
@RequestMapping("/tbcoupon")
@RestController
public class CouponController {

    @Autowired
    private CouponServiceImpl couponService;
    @Autowired
    private CouponTimeConfigServiceImpl couponTimeConfigService;
    @Autowired
    private CouponsDAO couponsDAO;
    @Autowired
    private FundOperationService fundOperationService;
    @Autowired
    private SysSettingsService sysSettingsService;

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CouponController.class);
    @ApiOperation(value = "获取优惠券列表")
    @GetMapping("getCouponList")
    @ResponseBody
    @IgnoreJwtAuth
    public List<CouponEntity> getConponList(){
        List<CouponEntity>  couponList = couponService.getCouponsList();

        return couponList;
    }

    @ApiOperation(value = "添加/修改优惠券")
    @PostMapping("addOrUpdateCoupon")
    @ResponseBody
    public Success addOrUpdateCoupons(@RequestBody
                                              CouponVo couponVo) {

        A.check(couponVo == null, "未接收到添加参数");
        A.check(couponVo.getRatio() == null, "奖励比例不能为空");
        //A.check(couponVo.getSendDays() == null, "请输入发放天数");
        if(org.apache.commons.lang3.StringUtils.isEmpty(couponVo.getId())){
            //增加
            CouponEntity couponEntity = new CouponEntity();
            couponEntity.setId(IDUtil.getUUID());
            couponEntity.setCouponName(couponVo.getCouponName());
            couponEntity.setCreateTime(new Date());
            couponEntity.setRatio(couponVo.getRatio());
            couponEntity.setCouponType(couponVo.getCouponType());
            couponEntity.setIsSend(0);
            couponEntity.setIsDocking(0);
            int i = couponService.addCoupon(couponEntity);
        }else{
            //修改
            int i=couponService.updateCouponById(couponVo.getCouponName(),
                    couponVo.getRatio(), couponVo.getCouponType(),couponVo.getId(),couponVo.getSendDays(),
                    couponVo.getIsSend(),couponVo.getIsDocking());

        }
        return Response.SUCCESS;
    }


    @ApiOperation(value = "查看优惠券配置详情")
    @GetMapping("getCouponDetailById")
    @ResponseBody
    public Result<CouponVo> getCouponDetailById(@RequestParam("id") String id){
        CouponVo couponVo = new CouponVo();
        CouponEntity couponEntity = couponService.getCouponEntityById(id);
        A.check(couponEntity == null,"该优惠券不存在");
        couponVo.setCouponName(couponEntity.getCouponName());
        couponVo.setCouponNum(couponEntity.getCouponNum());
        couponVo.setId(couponEntity.getId());
        couponVo.setRatio(couponEntity.getRatio());
        couponVo.setSendDays(couponEntity.getSendDays());
        couponVo.setIsDocking(couponEntity.getIsDocking());
        //还需要查询时间列表
        if(couponEntity.getIsSend()==1) {
            List<CouponTimeConfigEntity> timeConfigEntities = couponTimeConfigService.getCouponTimeConfigList();
            if (timeConfigEntities != null && timeConfigEntities.size() > 0) {
                List<String> dateStrList = new ArrayList<String>();
                for (CouponTimeConfigEntity data : timeConfigEntities) {
                    String timeStr = getDateStr(data.getNotSendTime());
                    dateStrList.add(timeStr);
                }
                couponVo.setNotSendDateList(dateStrList);
            }
        }
        return Response.result(couponVo);
    }

    @ApiOperation(value = "删除优惠券")
    @GetMapping("delCouponByIds")
    @ResponseBody
    public Success delCouponByIds(@RequestParam("ids") String ids){
        A.check(ids == null, "请选择要删除的目标");
        A.check(ids == null || ids.equals(""), "入参异常");
        String[] idArray = ids.split(",");
        for (int i = 0; i < idArray.length; i++) {
            int count = couponsDAO.getIsOrNotByGoods(idArray[i]);
            A.check(count != 0, "删除失败,优惠券已被使用不能删除");
        }
        List<String> idsList = null;
        if (idArray.length > 0) {
            idsList = Arrays.asList(idArray);
        }
        int i = couponService.deleteCouponByIds(idsList);
        A.check(i <= 0 || i != idArray.length , "删除失败,数据库显示数据与页面显示");

        return Response.SUCCESS;
    }



   /* //更新不可发送时间的信息配置
    public void  addCouponTimeConfigEntity(List<String> dateList,String couponId){
      for(String dataStr:dateList){
          Date date = getDate(dataStr);
          CouponTimeConfigEntity  couponTimeConfigEntity = new CouponTimeConfigEntity();
          couponTimeConfigEntity.setId(IDUtil.getUUID().toString());
          couponTimeConfigEntity.setNotSendTime(date);
          couponTimeConfigService.addCouponTimeConfig(couponTimeConfigEntity);
      }
    }
*/

    /**
     * 字符串转为时间
     */
    public Date getDate(String dateStr){
        SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd" );
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }


    /**
     * 时间转为字符串
     * @param date
     * @return
     */
    public String getDateStr(Date date){
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
        return sdf.format(date);
    }

    /*************start***************优惠券管理相关接口6.29BYli*****************************/
    @ApiOperation(value = "Admin后台-优惠券转让记录")
    @PostMapping("searchCouponTransfer")
    public CommonPageVO<CouponTransferVO> searchCouponTransfer(@ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
                                                               @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
                                                               @ApiParam("转出账户") @RequestParam(name = "rollOutAccount", required = false) String rollOutAccount,
                                                               @ApiParam("转入账户") @RequestParam(name = "rollInAccount", required = false) String rollInAccount,
                                                               @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {

        return fundOperationService.getCouponTransfer(beginTime, endTime, rollOutAccount,rollInAccount, pageNo, pageSize);
    }

    // 编辑优惠券转让配置
    @ApiOperation(value = "编辑优惠券转让配置")
    @PostMapping("updateCouponConfig")
    public Success updateCouponConfig(@RequestBody List<SysSettings> sysSettings) {
        A.check(sysSettings.size() != 4, "接受参数错误,优惠券配置不能为空");
        sysSettingsService.updateCouponConfig(sysSettings);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "Admin后台-查询消费返优惠券列表")
    @PostMapping("searchConsumerReturnCoupon")
    public CommonPageVO<UserStatementVO> searchConsumerReturnCoupon(@ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
                                                                    @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
                                                                    @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {

        return couponService.getReturnCoupon(beginTime, endTime, pageNo, pageSize);
    }
    // 编辑消费返优惠券配置
    @ApiOperation(value = "编辑消费返优惠券与提取优惠券手续费配置")
    @PostMapping("updateConfig")
    public Success updateConfig(@RequestBody List<SysSettings> sysSettings) {
        //A.check(sysSettings.size() != 1, "接受参数错误,优惠券配置不能为空");
        sysSettingsService.updateCouponConfig(sysSettings);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "Admin后台-彩票/游戏积分提取列表")
    @PostMapping("searchExtractList")
    public CommonPageVO<ExtractVO> searchExtractList(@ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
                                                     @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
                                                     @ApiParam("账户") @RequestParam(name = "mobile", required = false) String mobile,
                                                     @ApiParam("优惠券类型,传优惠券类型id") @RequestParam(name = "type", required = false) String type,
                                                     @ApiParam("状态") @RequestParam(name = "status", required = false) String status,
                                                     @ApiParam("彩票积分操作类型") @RequestParam(name = "operateType", required = false) String operateType,
                                                     @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {

        return couponService.getExtractList(beginTime, endTime, mobile, type, status, operateType,pageNo, pageSize);
    }
    @ApiOperation(value = "根据是否可与第三方对接获取优惠券列表", notes = "0.不可与第三方对接 1.可以与第三方对接(彩票游戏积分) 2.其他")
    @GetMapping("getCouponListByIsDocking")
    @ResponseBody
    public List<CouponEntity> getCouponListByIsDocking(@ApiParam("是否与第三方对接：暂时接口参数传1即可") @RequestParam(name = "isDocking", required = false) String isDocking){
        List<CouponEntity>  couponList = couponService.getCouponListByIsDocking(isDocking);
        System.out.print(couponList.size()+"----------------------------------");
        return couponList;
    }

    /**
     * 提取审核
     * @param
     * @return
     */
    @ApiOperation(value = "提取审核")
    @PostMapping("passExtract")
    public Success passExtract(@ApiParam("选中的id字符串,以逗号分隔")@RequestParam("ids") String ids,
                               @ApiParam("1:通过,2:拒绝")@RequestParam("status") String status) {
        A.check(StringUtils.isBlank(ids),"请选择需要审核的数据");
        couponService.passExtract(ids,status);
        return Response.SUCCESS;
    }

    /**
     * 导出优惠券提取记录
     */
/*    @ApiOperation(value = "导出优惠券提取记录")
    @GetMapping("exportExtract")
    @ResponseBody
    public void exexportExtractList(
            HttpServletResponse response,
            @ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam("账户") @RequestParam(name = "mobile", required = false) String mobile,
            @ApiParam("优惠券类型,传优惠券类型id") @RequestParam(name = "type", required = false) String type,
            @ApiParam("状态") @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "excelName", required = true) String excelName
    ) throws IOException, NoSuchFieldException, IllegalAccessException {
        ExtractVO extractVO = new ExtractVO(
                StringUtils.isBlank(beginTime) ? null : beginTime,
                StringUtils.isBlank(endTime) ? null : endTime,
                StringUtils.isBlank(mobile) ? null : mobile,
                StringUtils.isBlank(type) ? null : type,
                StringUtils.isBlank(status) ? null : status);
        logger.info("导出优惠券提取记录"+excelName);
        //response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(excelName, "utf-8") + ".xls");

        List<ExtractVO> list = couponService.getExportExtractList(extractVO);


        //ExcelGeneratorSheet generator = new ExcelGeneratorSheet();
        ExcelGenerator generator = new ExcelGenerator();
        ReportDefinition definition = new ReportDefinition(excelName);
        definition.addColumn(new ReportColumn("操作日期","createTime"));
        definition.addColumn(new ReportColumn("提取账户","trueName"));
        definition.addColumn(new ReportColumn("到账账户","arriveAccount"));
        definition.addColumn(new ReportColumn("优惠券类型","couponName"));
        definition.addColumn(new ReportColumn("提取数量","amount"));
        definition.addColumn(new ReportColumn("提取手续费比例（%）","ratio"));
        definition.addColumn(new ReportColumn("状态","status"));
        ReportData reportData = new ReportData(definition, list);
        generator.addSheet(reportData);


        generator.write(response.getOutputStream());

        response.getOutputStream().flush();

    }*/
    @ApiOperation(value = "导出优惠券提取记录")
    @GetMapping("exportExtract")
    @ResponseBody
    @IgnoreJwtAuth
    public void exexportExtractList(
            HttpServletResponse response, HttpServletRequest request,
            @ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam("账户") @RequestParam(name = "mobile", required = false) String mobile,
            @ApiParam("优惠券类型,传优惠券类型id") @RequestParam(name = "type", required = false) String type,
            @ApiParam("状态") @RequestParam(name = "status", required = false) String status,
            @ApiParam("彩票积分操作类型") @RequestParam(name = "operateType", required = false) String operateType,
            @RequestParam(name = "excelName", required = false) String excelName
    ) throws IOException, NoSuchFieldException, IllegalAccessException {
        ExtractVO extractVO = new ExtractVO(
                StringUtils.isBlank(beginTime) ? null : beginTime,
                StringUtils.isBlank(endTime) ? null : endTime,
                StringUtils.isBlank(mobile) ? null : mobile,
                StringUtils.isBlank(type) ? null : type,
                StringUtils.isBlank(status) ? null : status,
                StringUtils.isBlank(operateType) ? null : operateType);
        logger.info("导出优惠券提取记录" + excelName);

        ExportToExcelUtil<ExtractVO> excelUtil = new ExportToExcelUtil<ExtractVO>();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response,"彩票积分提现表");
            String[] headers = { "操作日期","提取账户","到账账户","优惠券类型","提取数量","提取手续费比例（%）","提取类型","状态"};
            String[] columns = { "createTime","trueName","arriveAccount","couponName","amount","ratio","operateType","status"};
            List<ExtractVO> list = couponService.getExportExtractList(extractVO);

            excelUtil.exportExcel( headers, columns, list, out, request, "yyyy-MM-dd HH:mm:ss");
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    /***************************end******************************/
}
