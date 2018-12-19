package cn.kt.mall.web.shop.controller.shop;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.kt.mall.common.excel.*;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.wallet.vo.CashRecordVO;
import cn.kt.mall.shop.shop.vo.*;
import cn.kt.mall.shop.trade.vo.TradeManageRespVO;
import io.swagger.annotations.ApiParam;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.ShopAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.service.ShopService;
import cn.kt.mall.shop.shop.service.TradeCommentService;
import cn.kt.mall.shop.trade.constant.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(value = "商家端店铺模块", tags = "online-shop-shop")
@RequestMapping("/shop/shop")
@RestController
public class ShopController {

    @Autowired
    ShopService shopService;
    @Autowired
    TradeCommentService tradeCommentService;

    @ApiOperation(value = "获取该店铺认证数据", notes = "")
    @GetMapping("authData")
    @ResponseBody
    @ShopAuth
    public AuthDataVO getAuthData() {
        return shopService.getShopAuthData(SubjectUtil.getCurrent().getId(), SubjectUtil.getCurrentShop().getId());
    }

    // 获取店铺评论列表
    @ApiOperation(value = "获取店铺评论列表")
    @GetMapping("comment")
    @ResponseBody
    @ShopAuth
    public PageVO<ShopCommentVO> getShopComment(
            @RequestParam(value = "searchName", required = false) String searchName,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam("pageNo") int pageNo,
            @RequestParam("pageSize") int pageSize) {
        return tradeCommentService.getShopComment(SubjectUtil.getCurrentShop().getId(), searchName, startTime, endTime, pageNo, pageSize);
    }

    // 免邮设置
    @ApiOperation(value = "免邮设置")
    @GetMapping("setFeightRate")
    @ResponseBody
    @ShopAuth
    public Success setFeightRate(@RequestParam("feightRate") double feightRate) {
        ShopEntity shopEntity = new ShopEntity();
        shopEntity.setId(SubjectUtil.getCurrentShop().getId());
        shopEntity.setFeightRate(new BigDecimal(feightRate).setScale(Constants.SCALE, RoundingMode.FLOOR));
        shopService.updateSelective(shopEntity);
        return Response.SUCCESS;
    }

    /**
     * 商城店铺管理分页
     */
    @ApiOperation(value = "商城店铺管理分页", notes = "")
    @GetMapping("shopPage")
    @ResponseBody
    @ShopAuth
    public CommonPageVO<ShopVO> shopPage(@RequestParam(name = "shopNo", required = false) String shopNo,
                                         @RequestParam(name = "name", required = false) String name,
                                         @RequestParam(name = "shopName", required = false) String shopName,
                                         @RequestParam("pageNo") int pageNo,
                                         @RequestParam("pageSize") int pageSize) {

        return shopService.getShopListByPid(SubjectUtil.getCurrent().getId(), name, shopName, shopNo, pageNo, pageSize);
    }


    @ApiOperation(value = "商城后台-店铺详情")
    @GetMapping("shop-details")
    public ShopVO getShopById(@ApiParam("店铺id") @RequestParam(required = false) String shopId) {
        ShopVO shopVO = shopService.getShopByShopId(shopId, null);
        ShopVO vo = shopService.getShopIdGoodCountAndAchievement(shopId);
        if (vo != null) {
            shopVO.setGoodCounts(vo.getGoodCounts());
        }
        return shopVO;

    }

    @ApiOperation(value = "商城后台-销售记录")
    @GetMapping("shop-shopSalesRecord")
    @ShopAuth
    public CommonPageVO<ShopSalesVO> shopSalesRecord(@RequestParam(value = "myId", required = false) String myId,
                                                     @ApiParam("店铺id") @RequestParam(value = "shopId", required = false) String shopId,
                                                     @RequestParam(value = "startTime", required = false) String startTime,
                                                     @RequestParam(value = "endTime", required = false) String endTime,
                                                     @RequestParam(value = "pageNo", required = false) int pageNo,
                                                     @RequestParam(value = "pageSize", required = false) int pageSize) {
        return shopService.getShopSalesRecord(myId, shopId, startTime, endTime, pageNo, pageSize);
    }

    @ApiOperation(value = "商城后台-商品销售记录")
    @GetMapping("shop-getShopGoodsCountSales")
    @ShopAuth
    public CommonPageVO<ShopTradeGoodSalesVO> getShopGoodsCountSales(
            @RequestParam(value = "shopId", required = false) String shopId,
            @RequestParam(value = "goodName", required = false) String goodName,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "pageNo") int pageNo,
            @RequestParam(value = "pageSize") int pageSize) {
        return shopService.getShopGoodsCountSales(shopId, goodName, startTime, endTime, pageNo, pageSize);


    }

    /**
     * @param myId
     * @param startTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "商城后台-业绩统计")
    @GetMapping("shop-getShopStatisticsList")
    @ShopAuth
    public CommonPageVO<ShopStatisticsVO> getShopStatisticsList(@RequestParam(value = "myId", required = false) String myId,
                                                                @RequestParam(value = "startTime", required = false) String startTime,
                                                                @RequestParam(value = "mobile", required = false) String mobile,
                                                                @RequestParam(value = "endTime", required = false) String endTime,
                                                                @RequestParam(value = "pageNo") int pageNo,
                                                                @RequestParam(value = "pageSize") int pageSize) {
        return shopService.getShopStatisticsList(myId, startTime, endTime, mobile, pageNo, pageSize);

    }


    @ApiOperation(value = "商鋪后台-资金记录列表")
    @GetMapping("getCashRecordLog")
    public CommonPageVO<CashRecordVO> getCashRecordLog(
            @ApiParam("开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam("结束时间") @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam("手机号码(对象)") @RequestParam(name = "phone", required = false) String phone,
            @ApiParam("操作人手机号") @RequestParam(name = "operatorUserPhone", required = false) String operatorUserPhone,
            @ApiParam("状态") @RequestParam(name = "status", required = false) String status,
            @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {

        return shopService.getFundDetailListByShop(SubjectUtil.getCurrent().getId(), operatorUserPhone, beginTime, endTime, phone, status, pageNo, pageSize);
    }


    //导出店铺管理信息
    @ApiOperation(value = "导出商铺列表数据")
    @ShopAuth
//    @IgnoreJwtAuth
    @GetMapping("exportShop")
    public void exportShop(@RequestParam(name = "shopNo", required = false) String shopNo,
                           @RequestParam(name = "name", required = false) String name,
                           @RequestParam(name = "excelName", required = true) String excelName,
                           @RequestParam(name = "shopName", required = false) String shopName, HttpServletResponse response, HttpServletRequest request)
            throws IOException, NoSuchFieldException, IllegalAccessException {
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");// // 指定文件的保存类型。
//        response.setCharacterEncoding("utf-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(excelName, "utf-8") + ".xls");
//        List<ShopVO> list = shopService.getShopListByPidAndExport(SubjectUtil.getCurrent().getId(), name, shopName, shopNo);
//        List<ShopVO> shopList = new ArrayList<>();
//        if (list != null && list.size() > 0) {
//            for (ShopVO shopVO : list) {
//                if (shopVO.getShopLevel().equals("2")) {
//                    shopVO.setShopLevel("零售店");
//                } else if (shopVO.getShopLevel().equals("3")) {
//                    shopVO.setShopLevel("批发店");
//                }
//                shopList.add(shopVO);
//            }
//        }
//
//        ExcelGenerator generator = new ExcelGenerator();
//        ReportDefinition definition = new ReportDefinition(excelName);
//        definition.addColumn(new ReportColumn("开店时间", "createTime"));
//        definition.addColumn(new ReportColumn("店铺ID", "shopNo"));
//        definition.addColumn(new ReportColumn("店长名称", "name"));
//        definition.addColumn(new ReportColumn("店铺名称", "shopName"));
//        definition.addColumn(new ReportColumn("上架商品数量", "goodCounts"));
//        definition.addColumn(new ReportColumn("店铺类型", "shopLevel"));
//        definition.addColumn(new ReportColumn("信用金", "point"));
//        definition.addColumn(new ReportColumn("销售业绩", "incomeLem"));
//        ReportData reportData = new ReportData(definition, shopList);
//        generator.addSheet(reportData);
//
//        generator.write(response.getOutputStream());
//        response.getOutputStream().flush();
//        response.getOutputStream().close();

        ExportToExcelUtil<ShopVO> excelUtil = new ExportToExcelUtil<ShopVO>();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response, "商铺管理导出");
            String[] headers = {"开店时间", "店铺ID", "店长名称", "店铺名称", "上架商品数量", "店铺类型", "信用金", "销售业绩"};
            String[] columns = {"createTime", "shopNo", "name", "shopName", "goodCounts", "shopLevel", "point", "incomeLem"};
            List<ShopVO> list = shopService.getShopListByPidAndExport(SubjectUtil.getCurrent().getId(), name, shopName, shopNo);
            List<ShopVO> shopList = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (ShopVO shopVO : list) {
                    if (shopVO.getShopLevel().equals("2")) {
                        shopVO.setShopLevel("零售店");
                    } else if (shopVO.getShopLevel().equals("3")) {
                        shopVO.setShopLevel("批发店");
                    }
                    if (shopVO.getPoint().compareTo(new BigDecimal(0E-8)) == 0) {
                        shopVO.setPoint(BigDecimal.ZERO);
                    }
                    if (shopVO.getIncomeLem().compareTo(new BigDecimal(0E-8)) == 0) {
                        shopVO.setIncomeLem(BigDecimal.ZERO);
                    }
                    shopList.add(shopVO);
                }
            }
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


    //查询店铺总收入
    @ApiOperation(value = "查询店铺总收入")
    @PostMapping("getShopSalesAndPointAndCoupon")
    public ShopSalesAndTimeVO getShopSalesAndPointAndCoupon(@ApiParam("店铺id") @RequestParam String shopId
    ) {
        return shopService.getShopSalesAndPointAndCoupon(shopId);
    }


}
