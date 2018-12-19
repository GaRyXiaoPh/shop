package cn.kt.mall.web.shop.controller.shop;


import cn.kt.mall.common.excel.*;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.wallet.vo.CashRecordVO;
import cn.kt.mall.shop.shop.service.ShopService;

import cn.kt.mall.shop.shop.vo.ShopSalesVO;
import cn.kt.mall.shop.shop.vo.ShopStatisticsVO;
import cn.kt.mall.shop.shop.vo.ShopTradeGoodSalesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Api(value = "商家端店铺导出模块", tags = "online-shop-shop-export")
@RequestMapping("/shop/export")
@RestController
public class ShopExportController {

    @Autowired
    ShopService shopService;

    @ApiOperation(value = "商城后台-商品销售记录导出")
    @GetMapping("getShopGoodsCountSalesExport")
    public void getShopGoodsCountSalesExport(
            @RequestParam(value = "shopId", required = false) String shopId,
            @RequestParam(value = "goodName", required = false) String goodName,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(name = "excelName", required = true) String excelName, HttpServletResponse response, HttpServletRequest request) throws IOException, NoSuchFieldException, IllegalAccessException {

//        response.setContentType("application/vnd.ms-excel;charset=utf-8");// // 指定文件的保存类型。
//        response.setCharacterEncoding("utf-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(excelName, "utf-8") + ".xls");
//        List<ShopTradeGoodSalesVO> shopList = shopService.getShopGoodsCountSalesExport(shopId, goodName, startTime, endTime);
//        ExcelGenerator generator = new ExcelGenerator();
//        ReportDefinition definition = new ReportDefinition(excelName);
//        definition.addColumn(new ReportColumn("销售时间", "createTime"));
//        definition.addColumn(new ReportColumn("商品", "skuName"));
//        definition.addColumn(new ReportColumn("销售数量", "goodCount"));
//        definition.addColumn(new ReportColumn("销售价值", "price"));
//        ReportData reportData = new ReportData(definition, shopList);
//        generator.addSheet(reportData);
//        generator.write(response.getOutputStream());
//        response.getOutputStream().flush();
//        response.getOutputStream().close();


        ExportToExcelUtil<ShopTradeGoodSalesVO> excelUtil = new ExportToExcelUtil<ShopTradeGoodSalesVO>();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response, "商品销售记录导出");
            String[] headers = {"销售时间", "商品", "销售数量", "销售价值"};
            String[] columns = {"createTime", "skuName", "goodCount", "price"};
            List<ShopTradeGoodSalesVO> shopList = shopService.getShopGoodsCountSalesExport(shopId, goodName, startTime, endTime);
            excelUtil.exportExcel(headers, columns, shopList, out, request, "yyyy-MM-dd hh:mm:ss");
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

    //店铺销售记录导出
    @ApiOperation(value = "商城后台-店铺销售记录导出")
    @GetMapping("shopSalesRecordByShopExport")
    public void shopSalesRecordByShopExport(@RequestParam(value = "myId", required = false) String myId,
                                            @ApiParam("店铺id") @RequestParam(value = "shopId", required = false) String shopId,
                                            @RequestParam(value = "startTime", required = false) String startTime,
                                            @RequestParam(value = "endTime", required = false) String endTime,
                                            @RequestParam(name = "excelName", required = true) String excelName, HttpServletResponse response, HttpServletRequest request
    ) throws IOException, NoSuchFieldException, IllegalAccessException {


        ExportToExcelUtil<ShopSalesVO> excelUtil = new ExportToExcelUtil<ShopSalesVO>();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response, "店铺销售记录导出");
            String[] headers = {"下单时间", "下单人", "下单人电话", "订单金额", "关系"};
            String[] columns = {"createTime", "buyUserName", "moblie", "baseTotal", "relationship"};
            List<ShopSalesVO> voList = shopService.shopSalesRecordByShopExport(myId, shopId, startTime, endTime);
            excelUtil.exportExcel(headers, columns, voList, out, request, "yyyy-MM-dd hh:mm:ss");
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
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");// // 指定文件的保存类型。
//        response.setCharacterEncoding("utf-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(excelName, "utf-8") + ".xls");
//        List<ShopSalesVO> voList = shopService.shopSalesRecordByShopExport(myId, shopId, startTime, endTime);
//        ExcelGenerator generator = new ExcelGenerator();
//        ReportDefinition definition = new ReportDefinition(excelName);
//        definition.addColumn(new ReportColumn("下单时间", "createTime"));
//        definition.addColumn(new ReportColumn("下单人", "buyUserName"));
//        definition.addColumn(new ReportColumn("下单人电话", "moblie"));
//        definition.addColumn(new ReportColumn("订单金额", "baseTotal"));
//        definition.addColumn(new ReportColumn("关系", "relationship"));
//        ReportData reportData = new ReportData(definition, voList);
//        generator.addSheet(reportData);
//        generator.write(response.getOutputStream());
//        response.getOutputStream().flush();
//        response.getOutputStream().close();
    }
    //业绩统计导出

    /**
     * @param myId
     * @param startTime
     * @param endTime
     * @return
     */
    @ApiOperation(value = "商城后台-业绩统计")
    @GetMapping("shop-getShopStatisticsList")
    public void getShopStatisticsListByShopExportt(@RequestParam(value = "myId", required = false) String myId,
                                                   @RequestParam(value = "startTime", required = false) String startTime,
                                                   @RequestParam(value = "mobile", required = false) String mobile,
                                                   @RequestParam(value = "endTime", required = false) String endTime,
                                                   @RequestParam(name = "excelName", required = true) String excelName, HttpServletResponse response, HttpServletRequest request) throws IOException, NoSuchFieldException, IllegalAccessException {
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");// // 指定文件的保存类型。
//        response.setCharacterEncoding("utf-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(excelName, "utf-8") + ".xls");
//        List<ShopStatisticsVO> voList = shopService.getShopStatisticsListByShopExportt(myId, startTime, endTime, mobile);
//        ExcelGenerator generator = new ExcelGenerator();
//        ReportDefinition definition = new ReportDefinition(excelName);
//        definition.addColumn(new ReportColumn("店铺名称", "shopName"));
//        definition.addColumn(new ReportColumn("用户名称", "name"));
//        definition.addColumn(new ReportColumn("用户手机号", "mobile"));
//        definition.addColumn(new ReportColumn("团队增长", "teamCount"));
//        definition.addColumn(new ReportColumn("团队总人数", "personCount"));
//        definition.addColumn(new ReportColumn("现金收入元", "cnySum"));
//        definition.addColumn(new ReportColumn("优惠卷收入", "popcSum"));
//        definition.addColumn(new ReportColumn("总金额", "totalPrice"));
//        ReportData reportData = new ReportData(definition, voList);
//        generator.addSheet(reportData);
//        generator.write(response.getOutputStream());
//        response.getOutputStream().flush();
//        response.getOutputStream().close();

        ExportToExcelUtil<ShopStatisticsVO> excelUtil = new ExportToExcelUtil<ShopStatisticsVO>();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response, "业绩统计导出");
            String[] headers = {"店铺名称", "用户名称", "用户手机号", "团队增长", "团队总人数", "现金收入元", "优惠卷收入", "总金额"};
            String[] columns = {"shopName", "name", "mobile", "teamCount", "personCount", "cnySum", "popcSum", "totalPrice"};
            List<ShopStatisticsVO> voList = shopService.getShopStatisticsListByShopExportt(myId, startTime, endTime, mobile);
            excelUtil.exportExcel(headers, columns, voList, out, request, "yyyy-MM-dd hh:mm:ss");
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
    //商鋪后台-资金记录列表

    @ApiOperation(value = "商鋪后台-资金记录导出")
    @GetMapping("getFundDetailListByShopExport")
    public void getFundDetailListByShopExport(
            @ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam("手机号码(对象)") @RequestParam(name = "phone", required = false) String phone,
            @ApiParam("操作人手机号") @RequestParam(name = "operatorUserPhone", required = false) String operatorUserPhone,
            @ApiParam("状态") @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "excelName", required = true) String excelName, HttpServletResponse response, HttpServletRequest request) throws IOException, NoSuchFieldException, IllegalAccessException {
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");// // 指定文件的保存类型。
//        response.setCharacterEncoding("utf-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(excelName, "utf-8") + ".xls");
//        List<CashRecordVO> volist = shopService.getFundDetailListByShopExport(SubjectUtil.getCurrent().getId(), operatorUserPhone, beginTime, endTime, phone, status);
//        for (CashRecordVO vo : volist) {
//            if (vo.getOperationType().equals("1")) {
//                vo.setOperationType("充值");
//            } else if (vo.getOperationType().equals("2")) {
//                vo.setOperationType("扣除");
//            }
//            if (vo.getStatus() == 0) {
//                vo.setStatusExplain("未审核");
//            } else if (vo.getStatus() == 1) {
//                vo.setStatusExplain("通过");
//            } else {
//                vo.setStatusExplain("拒绝");
//            }
//        }
//        ExcelGenerator generator = new ExcelGenerator();
//        ReportDefinition definition = new ReportDefinition(excelName);
//        definition.addColumn(new ReportColumn("操作时间", "createTime"));
//        definition.addColumn(new ReportColumn("操作类型", "operationType"));
//        definition.addColumn(new ReportColumn("到账账号", "trueName"));
//        definition.addColumn(new ReportColumn("到账手机号", "phone"));
//        definition.addColumn(new ReportColumn("数量", "rechargeAmount"));
//        definition.addColumn(new ReportColumn("操作人", "opreatorUser"));
//        definition.addColumn(new ReportColumn("操作人手机号", "loginName"));
//        definition.addColumn(new ReportColumn("备注", "remarks"));
//        definition.addColumn(new ReportColumn("状态", "statusExplain"));
//        ReportData reportData = new ReportData(definition, volist);
//        generator.addSheet(reportData);
//        generator.write(response.getOutputStream());
//        response.getOutputStream().flush();
//        response.getOutputStream().close();

        ExportToExcelUtil<CashRecordVO> excelUtil = new ExportToExcelUtil<CashRecordVO>();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response, "资金记录导出");
            String[] headers = {"操作时间", "操作类型", "到账账号", "到账手机号", "数量", "操作人", "操作人手机号", "备注", "状态"};
            String[] columns = {"createTime", "operationType", "trueName", "phone", "rechargeAmount", "opreatorUser", "loginName", "remarks", "statusExplain"};
            List<CashRecordVO> volist = shopService.getFundDetailListByShopExport(SubjectUtil.getCurrent().getId(), operatorUserPhone, beginTime, endTime, phone, status);
            for (CashRecordVO vo : volist) {
                if (vo.getOperationType().equals("1")) {
                    vo.setOperationType("充值");
                } else if (vo.getOperationType().equals("2")) {
                    vo.setOperationType("扣除");
                }
                if (vo.getStatus() == 0) {
                    vo.setStatusExplain("未审核");
                } else if (vo.getStatus() == 1) {
                    vo.setStatusExplain("通过");
                } else {
                    vo.setStatusExplain("拒绝");
                }
            }
            excelUtil.exportExcel(headers, columns, volist, out, request, "yyyy-MM-dd hh:mm:ss");
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
