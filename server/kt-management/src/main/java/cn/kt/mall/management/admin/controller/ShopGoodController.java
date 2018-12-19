package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.management.admin.service.PayTypeService;
import cn.kt.mall.management.admin.service.ShopGoodService;
import cn.kt.mall.management.admin.vo.PayTypeVO;
import cn.kt.mall.shop.good.service.GoodService;
import cn.kt.mall.shop.good.vo.GoodReqVO;
import cn.kt.mall.shop.good.vo.GoodRespVO;
import cn.kt.mall.shop.good.vo.GoodTypeVO;
import cn.kt.mall.shop.good.vo.GoodVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(description = "商品管理", tags = "shop-good-admin")
@RequestMapping("/shopGood/manage/")
@RestController
public class ShopGoodController {
    @Autowired
    private GoodService goodService;
    @Autowired
    private ShopGoodService shopGoodService;
    @Autowired
    private PayTypeService payTypeService;

    @ApiOperation(value = "商品数据列表", notes = "status:全部为null,0上传待审核，1审核通过，2审核不通过, 3商品下架")
    @GetMapping("searchGood")
    public CommonPageVO<GoodRespVO> listGood(
            @ApiParam("门店id, 不设置则查询平台商品") @RequestParam(name = "shopId", required = false) String shopId,
            @ApiParam("商品分类: well-sold:热销 boutique: 精品, 空字符或者不传： 所有类型") @RequestParam(name = "goodType", required = false) String goodType,
            @ApiParam("商品名称") @RequestParam(name = "goodName", required = false) String goodName,
            @ApiParam("商品状态:1为上架,3为下架") @RequestParam(name = "status", required = false) String status,
            @RequestParam("pageNo") int pageNo,
            @RequestParam("pageSize") int pageSize) {

        //平台商品的shopId固定是kt
        if (StringUtils.isEmpty(shopId)) {
            shopId = "kt";
        }

        return goodService.searchGoodss(shopId, goodName, goodType, status, 1, pageNo, pageSize);
    }

    @ApiOperation(value = "获取商品详情")
    @GetMapping("getGoodByGoodId")
    public GoodVO getGoodByGoodId(@RequestParam("goodId") String goodId) {
        //由于所调用接口被修改,重新定义方法并实现BYli---2018-07-01
        return goodService.getGoodById(goodId);
    }


    // 添加&修改商品
    @ApiOperation(value = "商品新增、修改接口", notes = "若id为空则表示新增，不为空则表示编辑")
    @PostMapping("addGoods")
    public Success addGoods(@RequestBody GoodReqVO goodReqVO) {
        goodService.addGoodss(goodReqVO);
        return Response.SUCCESS;
    }

    // 商品删除
    @ApiOperation(value = "删除商品", notes = "")
    @PostMapping("delGoods")
    public Success delGoods(@RequestParam("goodIds") String goodIds) {
        A.check(goodIds == null || goodIds.equals(""), "所选商品不能为空");
        goodService.delGoodss(goodIds);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "修改商品库存")
    @PostMapping("stock")
    public Success updateStock(@RequestBody String[] goodIds,
                               @ApiParam("库存调整数量， 负数代表减少") @RequestParam Integer stockDelta) {
        A.check(goodIds.length == 0, "商品列表是空");
        //管理后台是平台给商家增加库存，平台门店id是kt
        goodService.updateStock("kt", goodIds, stockDelta);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "修改平台商品库存")
    @PostMapping("ktStock")
    public Success updateKtStock(@RequestBody String goodId,
                                 @ApiParam("库存调整数量， 负数代表减少") @RequestParam(name = "stockDelta", required = true) Integer stockDelta) {
        A.check(org.apache.commons.lang3.StringUtils.isBlank(goodId), "参数错误");
        //管理后台是平台给商家增加库存，平台门店id是kt
        goodService.updateKtStock(goodId, stockDelta);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "调整商品分类(批量)")
    @GetMapping("adjustGoodType")
    public Success adjustGoodType(@ApiParam("商品分类id") @RequestParam(name = "id", required = false) String id,
                                  @ApiParam("商品id集合：使用,分隔") @RequestParam(name = "goodIds", required = false) String goodIds) {
        A.check(null == goodIds, "请选择商品");
        shopGoodService.adjustGoodType(id, goodIds);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "商品分类列表")
    @GetMapping("goodTypeList")
    public List<GoodTypeVO> goodTypeList(@ApiParam("商品分类名称") @RequestParam(name = "goodTypeName", required = false) String goodTypeName) {
        return shopGoodService.goodTypeList(goodTypeName);
    }

    @ApiOperation(value = "添加商品分类")
    @GetMapping("addGoodType")
    public Success addGoodType(@ApiParam("商品分类名称") @RequestParam(name = "goodTypeName", required = false) String goodTypeName) {
        A.check((null == goodTypeName || goodTypeName.trim().equals("")), "请输入商品分类");
        shopGoodService.addGoodType(goodTypeName);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "修改商品分类")
    @GetMapping("editGoodType")
    public Success editGoodType(@ApiParam("商品分类id") @RequestParam(name = "goodTypeId") String goodTypeId,
                                @ApiParam("商品分类名称") @RequestParam(name = "goodTypeName", required = false) String goodTypeName) {
        A.check((null == goodTypeName || goodTypeName.trim().equals("")), "请输入商品分类");
        shopGoodService.editGoodType(goodTypeId, goodTypeName);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "支付方式查询")
    @GetMapping("payTypeList")
    public List<PayTypeVO> payTypeList(@ApiParam("商品Id") @RequestParam(name = "goodId", required = false) String goodId) {
        return payTypeService.payTypeList(goodId);
    }

    @ApiOperation(value = "支付比例修改")
    @GetMapping("updatePayRatio")
    public Success updatePayRatio(@ApiParam("支付方式id") @RequestParam(name = "payTypeId", required = false) String payTypeId,
                                  @ApiParam("余额比例值(%)") @RequestParam(name = "balanceRatio", required = false) String balanceRatio,
                                  @ApiParam("其它比例值(%)") @RequestParam(name = "otherRatio", required = false) String otherRatio) {
        payTypeService.updatePayRatio(payTypeId, balanceRatio, otherRatio);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "商品上架&下架(批量)")
    @GetMapping("editGoodState")
    public Success editGoodState(@ApiParam("标识符(1:上架 2:下架)") @RequestParam(name = "flag", required = false) int flag,
                                 @ApiParam("商品id集合：使用,分隔") @RequestParam(name = "goodIds", required = false) String goodIds) {
        if (flag == 1) {
            // 商品上架
            shopGoodService.goodOnline(goodIds);
        } else {
            // 商品下架
            shopGoodService.goodOffline(goodIds);
        }
        return Response.SUCCESS;
    }

    @ApiOperation(value = "商品支付方式修改(批量)")
    @GetMapping("editGoodPayType")
    public Success editGoodPayType(@ApiParam("支付方式id:1余额,2余额+优惠券") @RequestParam(name = "payTypeId", required = false) String payTypeId,
                                   @ApiParam("商品id集合：使用,分隔") @RequestParam(name = "goodIds", required = false) String goodIds) {
        shopGoodService.editGoodPayType(payTypeId, goodIds);
        return Response.SUCCESS;
    }

//    @ApiOperation(value = "批量添加测试")
//    @GetMapping("insertByBatchGoodShopCenter")
//    public Success insertByBatchGoodShopCenter() {
//        List<GoodEntity> list = goodService.getGoodsByKtList("kt");
//        goodService.insertByBatchGoodShopCenter(list);
//        return Response.SUCCESS;
//    }

    @ApiOperation(value = "查询支付方式--信用金所占比例")
    @GetMapping("getPayRatio")
    public List<PayTypeVO> getPayRatio() {
        return payTypeService.getPayRatio();
    }

}
