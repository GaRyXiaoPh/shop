package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.excel.*;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.management.admin.entity.CouponEntity;
import cn.kt.mall.management.admin.service.FundOperationService;
import cn.kt.mall.management.admin.service.impl.CouponServiceImpl;
import cn.kt.mall.management.admin.vo.*;
import cn.kt.mall.management.admin.vo.CashRecordVO;
import cn.kt.mall.shop.shop.service.ShopService;
import cn.kt.mall.shop.shop.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * 资金管理 Controller
 * Created by xxx on 2018/06/20.
 */
@Api(description = "操作记录-资金记录", tags = "fund-operation")
@RequestMapping("/operation")
@RestController
public class FundOperationController {
    @Autowired
    private FundOperationService fundOperationService;
    @Autowired
    ShopService shopService;
    @Autowired
    private CouponServiceImpl couponService;

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FundOperationController.class);

    @ApiOperation(value = "Admin后台-获取POPC解冻列表", notes = "type:全部为null,0自然解冻，2消费解冻-----资金解冻记录")
    @PostMapping("searchPopc")
    public CommonPageVO<UnfreezeLogVO> searchPopc(@ApiParam("解冻类型--1为自然解冻,2为购买商品解冻") @RequestParam(name = "type", required = false) String type,
                                                  @ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
                                                  @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
                                                  @ApiParam("数量最小值") @RequestParam(name = "minNum", required = false) BigDecimal minNum,
                                                  @ApiParam("数量最大值") @RequestParam(name = "maxNum", required = false) BigDecimal maxNum,
                                                  @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return fundOperationService.getPopcList(type, beginTime, endTime, minNum, maxNum, pageNo, pageSize);
    }

 /*   @ApiOperation(value = "Admin后台-获取业绩列表")
    @PostMapping("searchPerformance")
    public CommonPageVO<ShopStatisticsVO> searchPerformance(@ApiParam("开始时间") @RequestParam(name = "startTime", required = false) Date startTime,
                                                            @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) Date endTime,
                                                            @ApiParam("店铺名称") @RequestParam(name = "shopName", required = false) String shopName,
                                                            @ApiParam("用户名称") @RequestParam(name = "userName", required = false) String userName,
                                                            @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {

        return fundOperationService.getShopPerformanceList(startTime, endTime, shopName, userName, pageNo, pageSize);
    }*/

  /*  @ApiOperation(value = "Admin后台-所有店铺销售记录列表")
    @PostMapping("searchShopSale")
    public CommonPageVO<ShopRespVO> searchShopSale(@ApiParam("店铺类型-- 2:形象店  3:旗舰店") @RequestParam(name = "shopType", required = false) String shopType,
                                                   @ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
                                                   @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
                                                   @ApiParam("门店编号") @RequestParam(name = "shopNo", required = false) String shopNo,
                                                   @ApiParam("店铺名称") @RequestParam(name = "shopName", required = false) String shopName,
                                                   @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return fundOperationService.getShopList(shopType, beginTime, endTime, shopNo, shopName, pageNo, pageSize);

    }*/

    @ApiOperation(value = "Admin后台-店铺业绩合并接口")
    @PostMapping("searchShopStatistics")
    public CommonPageVO<ShopStatisticsVO> searchShopStatistics(@ApiParam("店铺类型-- 2:零售店 3:批发店") @RequestParam(name = "shopType", required = false) String shopType,
                                                   @ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
                                                   @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
                                                   @ApiParam("店铺ID") @RequestParam(name = "shopNo", required = false) String shopNo,
                                                   @ApiParam("店铺名称") @RequestParam(name = "shopName", required = false) String shopName,
                                                   @ApiParam("用户名称") @RequestParam(name = "userName", required = false) String userName,
                                                   @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return shopService.getShopStatisticss(shopType, beginTime, endTime, shopNo, shopName,userName, pageNo, pageSize);

    }

    @ApiOperation(value = "Admin后台-单个店铺销售记录")
    @GetMapping("shop-shopSalesRecord")
    public CommonPageVO<ShopSalesVO> shopSalesRecord(@RequestParam(value = "shopId", required = true) String shopId,
                                                     @RequestParam(value = "startTime", required = false) String startTime,
                                                     @RequestParam(value = "endTime", required = false) String endTime,
                                                     @RequestParam(value = "pageNo") int pageNo,
                                                     @RequestParam(value = "pageSize") int pageSize) {

        return fundOperationService.getShopSalesRecord(shopId, startTime, endTime, pageNo, pageSize);
    }

    @ApiOperation(value = "Admin后台-商品销售记录列表")
    @PostMapping("searchGoodsSale")
    public CommonPageVO<ShopTradeGoodSalesVO> searchGoodsSale(@ApiParam("店铺类型--2:零售店  3:批发店") @RequestParam(name = "shopType", required = false) String shopType,
                                                              @ApiParam("开始时间") @RequestParam(name = "startTime", required = false) String startTime,
                                                              @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
                                                              @ApiParam("商品名称") @RequestParam(name = "skuName", required = false) String skuName,
                                                              @ApiParam("店铺名称") @RequestParam(name = "shopName", required = false) String shopName,
                                                              @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {

        return shopService.getShopGoodsCountSale(shopType, shopName, skuName, startTime, endTime, pageNo, pageSize);
    }

    @ApiOperation(value = "Admin后台-资金记录列表")
    @PostMapping("getCashRecordLog")
    public CommonPageVO<CashRecordVO> getCashRecordLog(@ApiParam("操作人") @RequestParam(name = "opreatorUser", required = false) String opreatorUser,
                                                       @ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
                                                       @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
                                                       @ApiParam("手机号码(对象)") @RequestParam(name = "phone", required = false) String phone,
                                                       @ApiParam("状态") @RequestParam(name = "status", required = false) String status,
                                                       @ApiParam("操作类型") @RequestParam(name = "operationType", required = false) String operationType,
                                                       @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize){

        return fundOperationService.getCashRecordList(opreatorUser,operationType,beginTime,endTime,phone,status,pageNo,pageSize);
    }

    @ApiOperation(value = "Admin后台-系统操作日志")
    @PostMapping("searchOperationLog")
    public CommonPageVO<UserOperatorLogVO> searchOperationLog(@ApiParam("开始时间") @RequestParam(name = "startTime", required = false) String startTime,
                                                              @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
                                                              @ApiParam("登录账号") @RequestParam(name = "account", required = false) String account,
                                                              @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {

        return fundOperationService.getOperationLog(account, startTime, endTime, pageNo, pageSize);
    }

    @ApiOperation(value = "Admin后台-历史盈利数据")
    @PostMapping("searchHistoryProfit")
    public CommonPageVO<HistoryProfitVO> searchHistoryProfit(@ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
                                                             @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
                                                             @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {

        return fundOperationService.getHistoryProfit(beginTime, endTime, pageNo, pageSize);
    }
    @ApiOperation(value = "Admin后台-获得历史盈利数据表头数据")
    @PostMapping("searchHead")
    public List<HeadVO> searchHead() {

        return fundOperationService.searchHead();
    }
    @ApiOperation(value = "Admin后台-优惠券转让记录")
    @PostMapping("searchCouponTransfer")
    public CommonPageVO<CouponTransferVO> searchCouponTransfer(@ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
                                                              @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
                                                              @ApiParam("转出账户") @RequestParam(name = "rollOutAccount", required = false) String rollOutAccount,
                                                              @ApiParam("转入账户") @RequestParam(name = "rollInAccount", required = false) String rollInAccount,
                                                             @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {

        return fundOperationService.getCouponTransfer(beginTime, endTime, rollOutAccount,rollInAccount, pageNo, pageSize);
    }

    @ApiOperation(value = "Admin后台-赠送记录")
    @PostMapping("searchGivingRecord")
    public CommonPageVO<CouponVO> searchGivingRecord(@ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
                                                               @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
                                                               @ApiParam("用户") @RequestParam(name = "iphone", required = false) String iphone,
                                                               @ApiParam("优惠券名称") @RequestParam(name = "ids", required = false) List<String> ids,
                                                               @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {

        return fundOperationService.getGivingRecord(beginTime, endTime, iphone,ids, pageNo, pageSize);
    }


    /****************************以下为导出相关信息代码**************************/
    @ApiOperation(value = "导出店铺销售记录")
    @GetMapping("exportShopStatistics")
    @ResponseBody
    public void exportShopStatisticsList(
            HttpServletResponse response, HttpServletRequest request,
            @ApiParam("店铺类型-- 2:零售店 3:批发店") @RequestParam(name = "shopType", required = false) String shopType,
            @ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam("店铺ID") @RequestParam(name = "shopNo", required = false) String shopNo,
            @ApiParam("店铺名称") @RequestParam(name = "shopName", required = false) String shopName,
            @ApiParam("用户名称") @RequestParam(name = "userName", required = false) String userName,
            @RequestParam(name = "excelName", required = false) String excelName
    ) throws IOException, NoSuchFieldException, IllegalAccessException {
        ShopStatisticsReqVO shopStatisticsReqVO = new ShopStatisticsReqVO(
                StringUtils.isBlank(beginTime) ? null : beginTime,
                StringUtils.isBlank(endTime) ? null : endTime,
                StringUtils.isBlank(shopType) ? null : shopType,
                StringUtils.isBlank(shopNo) ? null : shopNo,
                StringUtils.isBlank(shopName) ? null : shopName,
                StringUtils.isBlank(userName) ? null : userName);
        logger.info("导出店铺销售记录名"+excelName);

        ExportToExcelUtil<ShopStatisticsVO> excelUtil = new ExportToExcelUtil<ShopStatisticsVO>();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response,"店铺销售记录表");
            String[] headers = { "店铺类型","店铺ID","店长名称","店铺名称","团队增长","团队总人数","销售业绩"};
            String[] columns = { "shopLevel","shopNo","name","shopName","teamCount","personCount","totalPrice"};
            List<ShopStatisticsVO> list = shopService.getStatisticsList(shopStatisticsReqVO);

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
    @ApiOperation(value = "导出历史盈利记录")
    @GetMapping("exportHistoryProfit")
    @ResponseBody
    public void exportHistoryProfitList(
            HttpServletResponse response,
            @ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
            @RequestParam(name = "excelName", required = false) String excelName
    ) throws IOException, NoSuchFieldException, IllegalAccessException {
        HistoryProfitReqVO historyProfitReqVO = new HistoryProfitReqVO(
                StringUtils.isBlank(beginTime) ? null : beginTime,
                StringUtils.isBlank(endTime) ? null : endTime);
        logger.info("导出历史盈利记录名"+excelName);
        //response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(excelName, "utf-8") + ".xls");

        List<HistoryProfitVO> list = fundOperationService.getHistoryProfitList(historyProfitReqVO);


        ExcelGenerator generator = new ExcelGenerator();

        ReportDefinition definition = new ReportDefinition(excelName);
        definition.addColumn(new ReportColumn("时间","createTime"));
        definition.addColumn(new ReportColumn("收入总信用金","incomeTotalCredit"));
        definition.addColumn(new ReportColumn("收入总优惠券","incomeTotalCoupon"));

        if (!list.isEmpty()) {
            HistoryProfitVO firstRow = list.get(0);
            if (firstRow instanceof DynamicRow) {
                DynamicRow row = (DynamicRow)firstRow;
                List<DynamicHeader> headers = row.getDataNameValue();
                for (DynamicHeader h : headers) {
                    definition.addColumn(new ReportColumn(h.getHeadName(),null));
                  /*  definition.addColumn(new ReportColumn("赠送总优惠券","giveTotalCoupon"));
                    //游戏积分不要了
                    //definition.addColumn(new ReportColumn("赠送总游戏积分","giveTotalGame"));
                    definition.addColumn(new ReportColumn("赠送总债权","giveTotalIbot"));
                    definition.addColumn(new ReportColumn("赠送总保险","giveTotalnsurance"));*/
                }
            }
        }



        ReportData reportData = new ReportData(definition, list);
        generator.addSheet(reportData);


        generator.write(response.getOutputStream());

        response.getOutputStream().flush();

    }
    @ApiOperation(value = "导出商品销售记录")
    @GetMapping("exportGoodsSale")
    @ResponseBody
    public void exportGoodsSaleList(
            HttpServletResponse response,HttpServletRequest request,
            @ApiParam("店铺类型--2:零售店  3:批发店") @RequestParam(name = "shopType", required = false) String shopType,
            @ApiParam("开始时间") @RequestParam(name = "startTime", required = false) String startTime,
            @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam("商品名称") @RequestParam(name = "skuName", required = false) String skuName,
            @ApiParam("店铺名称") @RequestParam(name = "shopName", required = false) String shopName,
            @RequestParam(name = "excelName", required = false) String excelName
    ) throws IOException, NoSuchFieldException, IllegalAccessException {
        ShopTradeGoodSalesReqVO shopTradeGoodSalesReqVO = new ShopTradeGoodSalesReqVO(
                StringUtils.isBlank(startTime) ? null : startTime,
                StringUtils.isBlank(endTime) ? null : endTime,
                StringUtils.isBlank(shopType) ? null : shopType,
                StringUtils.isBlank(skuName) ? null : skuName,
                StringUtils.isBlank(shopName) ? null : shopName);
        logger.info("导出商品销售记录"+excelName);

        ExportToExcelUtil<ShopTradeGoodSalesVO> excelUtil = new ExportToExcelUtil<ShopTradeGoodSalesVO>();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response,"商品销售记录表");
            String[] headers = { "销售时间","商品","店铺类型","店铺名称","销售数量","销售价值"};
            String[] columns = { "createTime","skuName","shopLevel","shopName","goodCount","price"};
            List<ShopTradeGoodSalesVO> list = shopService.getShopTradeGoodSalesList(shopTradeGoodSalesReqVO);

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
    @ApiOperation(value = "导出系统日志记录")
    @GetMapping("exportOperatorLog")
    @ResponseBody
    public void exportOperatorLogList(
            HttpServletResponse response,HttpServletRequest request,
            @ApiParam("开始时间") @RequestParam(name = "startTime", required = false) String startTime,
            @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam("登录账号") @RequestParam(name = "account", required = false) String account,
            @RequestParam(name = "excelName", required = false) String excelName
    ) throws IOException, NoSuchFieldException, IllegalAccessException {
        UserOperatorLogReqVO userOperatorLogReqVO = new UserOperatorLogReqVO(
                StringUtils.isBlank(startTime) ? null : startTime,
                StringUtils.isBlank(endTime) ? null : endTime,
                StringUtils.isBlank(account) ? null : account);
        logger.info("导出系统日志记录"+excelName);

        ExportToExcelUtil<UserOperatorLogVO> excelUtil = new ExportToExcelUtil<UserOperatorLogVO>();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response,"系统日志表");
            String[] headers = { "操作时间","登录账号","职位","操作过程"};
            String[] columns = { "opratorTime","account","position","operatorName"};
            List<UserOperatorLogVO> list = fundOperationService.getOperatorLogList(userOperatorLogReqVO);

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
    @ApiOperation(value = "导出赠送记录")
    @GetMapping("exportGiving")
    @ResponseBody
    public void exportGivingList(
            HttpServletResponse response,HttpServletRequest request,
            @ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam("用户") @RequestParam(name = "iphone", required = false) String iphone,
            @ApiParam("优惠券名称") @RequestParam(name = "ids", required = false) List<String> ids,
            @RequestParam(name = "excelName", required = false) String excelName
    ) throws IOException, NoSuchFieldException, IllegalAccessException {
        CouponGiveReqVO couponGiveReqVO = new CouponGiveReqVO(
                StringUtils.isBlank(beginTime) ? null : beginTime,
                StringUtils.isBlank(endTime) ? null : endTime,
                StringUtils.isBlank(iphone) ? null : iphone,
                CollectionUtils.isEmpty(ids)? null : ids);
        logger.info("导出赠送记录"+excelName);

        ExportToExcelUtil<CouponVO> excelUtil = new ExportToExcelUtil<CouponVO>();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response,"赠送记录表");
            String[] headers = { "时间","用户","优惠券名称","赠送数量"};
            String[] columns = { "createTime","trueName","couponName","amount"};
            List<CouponVO> list = fundOperationService.getGivingList(couponGiveReqVO);

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

    @ApiOperation(value = "导出资金记录")
    @GetMapping("exportCash")
    @ResponseBody
    public void exportCashList(
            HttpServletResponse response,HttpServletRequest request,
            @ApiParam("操作人") @RequestParam(name = "opreatorUser", required = false) String opreatorUser,
            @ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam("手机号码(对象)") @RequestParam(name = "phone", required = false) String phone,
            @ApiParam("状态") @RequestParam(name = "status", required = false) String status,
            @ApiParam("操作类型") @RequestParam(name = "operationType", required = false) String operationType,
            @RequestParam(name = "excelName", required = false) String excelName
    ) throws IOException, NoSuchFieldException, IllegalAccessException {
        CashRecordReqVO cashRecordReqVO = new CashRecordReqVO(
                StringUtils.isBlank(beginTime) ? null : beginTime,
                StringUtils.isBlank(endTime) ? null : endTime,
                StringUtils.isBlank(operationType) ? null : operationType,
                StringUtils.isBlank(opreatorUser) ? null : opreatorUser,
                StringUtils.isBlank(status) ? null : status,
                StringUtils.isBlank(phone) ? null : phone);

        logger.info("导出资金记录表名"+excelName);

        ExportToExcelUtil<CashRecordVO> excelUtil = new ExportToExcelUtil<CashRecordVO>();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response,"资金记录表");
            String[] headers = { "操作时间","操作类型","到账账户","数量","操作人","操作人类型","备注","状态"};
            String[] columns = { "createTime","operationType","rechargeAll","rechargeAmount","opreatorAll","shopType","remarks","status"};
            List<CashRecordVO> list = fundOperationService.getFundOperationList(cashRecordReqVO);

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

    @ApiOperation(value = "导出优惠券转让记录")
    @GetMapping("exportCoupon")
    @ResponseBody
    public void exexportCouponList(
            HttpServletResponse response,HttpServletRequest request,
            @ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam("转出账户") @RequestParam(name = "rollOutAccount", required = false) String rollOutAccount,
            @ApiParam("转入账户") @RequestParam(name = "rollInAccount", required = false) String rollInAccount,
            @RequestParam(name = "excelName", required = false) String excelName
    ) throws IOException, NoSuchFieldException, IllegalAccessException {
        CouponTransferReqVO couponTransferReqVO = new CouponTransferReqVO(
                StringUtils.isBlank(beginTime) ? null : beginTime,
                StringUtils.isBlank(endTime) ? null : endTime,
                StringUtils.isBlank(rollOutAccount) ? null : rollOutAccount,
                StringUtils.isBlank(rollInAccount) ? null : rollInAccount);
        logger.info("导出优惠券转让记录"+excelName);

        ExportToExcelUtil<CouponTransferVO> excelUtil = new ExportToExcelUtil<CouponTransferVO>();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response,"优惠券转让表");
            String[] headers = { "转让时间","转出账户","到账账户","转让数量","转让关系","转让比例","手续费"};
            String[] columns = { "createTime","trueNameAll","nameAll","amount","relation","feeRatio","fee"};
            List<CouponTransferVO> list = fundOperationService.getCouponList(couponTransferReqVO);

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


}
