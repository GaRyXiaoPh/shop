package cn.kt.mall.web.shop.controller.goods;

import java.util.ArrayList;
import java.util.List;

import cn.kt.mall.shop.coupon.service.CouponService;
import cn.kt.mall.web.shop.vo.GoodShelfVO;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.ShopAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.util.TreeNodeUtil;
import cn.kt.mall.common.vo.JsonTreeVO;
import cn.kt.mall.shop.good.constant.Constants;
import cn.kt.mall.shop.good.entity.GoodTypeEntity;
import cn.kt.mall.shop.good.service.GoodService;
import cn.kt.mall.shop.good.vo.GoodReqVO;
import cn.kt.mall.shop.good.vo.GoodRespVO;
import cn.kt.mall.shop.good.vo.GoodVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "商家端商品模块", tags = "online-shop-goods")
@RequestMapping("/shop/goods")
@RestController
public class GoodsController {

    @Autowired
    GoodService goodService;


    // 商城商品搜索
    @ApiOperation(value = "获取商品(商品搜索)", notes = "status:为空表示查询全部，0上传待审核，1审核通过，2审核不通过, 3商品下架")
    @PostMapping("searchGoods")
    @ResponseBody
    @ShopAuth
    // searchMode代表排序规则，1代表综合排序，2代表新品优先，3代表销量优先
    public CommonPageVO<GoodRespVO> searchGoods(@RequestParam(name = "goodName", required = false) String goodName,
                                                @RequestParam(name = "status", required = false) String status, @RequestParam("pageNo") int pageNo,
                                                @RequestParam("pageSize") int pageSize) {
        return goodService.searchGoods(SubjectUtil.getCurrentShop().getId(), goodName, null, status, 2, null, null,
                pageNo, pageSize);
    }

    // 商城商品详情
    @ApiOperation(value = "获取商品详情", notes = "")
    @GetMapping("getGoods")
    @ResponseBody
    @ShopAuth
    public GoodVO managerGoodByGoodId(@RequestParam("goodId") String goodId) {
        return goodService.managerGoodByGoodId(SubjectUtil.getCurrentShop().getId(), goodId);
    }


    // 商品上架
    @ApiOperation(value = "商品上架申请", notes = "")
    @GetMapping("upGoods")
    @ResponseBody
    @ShopAuth
    public Success upGoods(@RequestParam("goodId") String goodId) {
        goodService.modifyStatus(SubjectUtil.getCurrentShop().getId(), goodId, Constants.GoodStatus.GOOD_DOWN,
                Constants.GoodStatus.GOOD_PASS, null);
        return Response.SUCCESS;
    }

    // 商品下架
    @ApiOperation(value = "商品下架操作", notes = "")
    @GetMapping("downGoods")
    @ResponseBody
    @ShopAuth
    public Success downGoods(@RequestParam("goodId") String goodId) {
        goodService.modifyStatus(SubjectUtil.getCurrentShop().getId(), goodId, Constants.GoodStatus.GOOD_PASS,
                Constants.GoodStatus.GOOD_DOWN, null);
        return Response.SUCCESS;
    }

    // 商品删除
    @ApiOperation(value = "商品删除操作", notes = "")
    @GetMapping("delGoods")
    @ResponseBody
    @ShopAuth
    public Success delGoods(@RequestParam("goodId") String goodId) {
        goodService.delGoods(SubjectUtil.getCurrentShop().getId(), goodId);
        return Response.SUCCESS;
    }

/*
    @ApiOperation(value = "商品新增、修改接口", notes = "若id为空则表示新增，不为空则表示编辑")
    @PostMapping("addGoods")
    @ResponseBody
    @ShopAuth
    // 添加&修改商品
    public Success addGoods(@RequestBody GoodReqVO goodReqVO) {
        goodService.addGoods(SubjectUtil.getCurrentShop().getId(), goodReqVO);
        return Response.SUCCESS;
    }
*/

    /**
     * 为当前用户的下级添加商品
     *
     * @param goodReqVO
     * @return
     */
/*    @ApiOperation(value = "下级店铺商品新增、修改接口", notes = "若id为空则表示新增，不为空则表示编辑")
    @PostMapping("addOtherGoods")
    @ResponseBody
    @ShopAuth
    public Success addOtherGoods(@RequestBody GoodReqVO goodReqVO) {
        //验证是否登陆
        SubjectUtil.getCurrentShop();
        goodService.addGoods(goodReqVO.getShopId(), goodReqVO);
        return Response.SUCCESS;
    }*/

    /**
     * @return
     */

    // 获取商品分类
    @ApiOperation(value = "获取所有商品分类树形结构--NEW", notes = "")
    @GetMapping("listAllType")
    @ShopAuth
    @ResponseBody
    public List<JsonTreeVO> listAllType() {
        List<GoodTypeEntity> source = goodService.listAllType();
        List<JsonTreeVO> result = new ArrayList<>();
        if (source != null) {
            result = new ArrayList<>(source.size());
            for (GoodTypeEntity goodTypeEntity : source) {
                JsonTreeVO jsonTreeVO = new JsonTreeVO();
                jsonTreeVO.setId(goodTypeEntity.getId());
                jsonTreeVO.setImg(goodTypeEntity.getImg());
                jsonTreeVO.setPid(goodTypeEntity.getParentId());
                jsonTreeVO.setText(goodTypeEntity.getName());
                result.add(jsonTreeVO);
            }
            return TreeNodeUtil.getParentNode(result);
        }
        return result;
    }


    /**
     * 操作当前登陆人下级店铺商品库存
     *
     * @param goodIds
     * @param stockDelta
     * @return
     */
    @ApiOperation(value = "操作当前登陆人下级店铺商品库存", notes = "")
    @PostMapping("addGoodsByStock")
    @ResponseBody
    @ShopAuth
    public Success addGoodsByStock(@RequestBody String[] goodIds,
                                   @RequestParam(name = "stockDelta") Integer stockDelta
    ) {
        goodService.updateStock(SubjectUtil.getCurrentShop().getId(), goodIds, stockDelta);
        return Response.SUCCESS;
    }

//    /**
//     * 为登陆人添加下级商品
//     */
//    @ApiOperation(value = "为登陆人添加下级商品", notes = "")
//    @PostMapping("addGoodsToShop")
//    @ResponseBody
//    @ShopAuth
//    public Success addGoodsToShop(@RequestParam(name = "shopId", required = false) String shopId,
//                                  @RequestBody List<String> goodIds) {
//        goodService.addGoodsToShop(shopId, goodIds);
//        return Response.SUCCESS;
//    }

//    /**
//     * 为下级删除商品
//     */
//    @ApiOperation(value = "为下级删除商品", notes = "")
//    @PostMapping("delGoodss")
//    @ResponseBody
//    @ShopAuth
//    public Success delGoodss(@RequestBody String[] goodsIds) {
//        goodService.delGoodss(goodsIds);
//        return Response.SUCCESS;
//    }


    /**
     * 商城后台-店铺管理 店铺详情-商品 NEW-2018-6-14
     *
     * @param shopId
     * @param goodType
     * @param goodName
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "商品数据列表", notes = "status:全部为null,0上传待审核，1审核通过，2审核不通过, 3商品下架")
    @GetMapping("listGoodByShop")
    @ShopAuth
    public CommonPageVO<GoodRespVO> listGoodByShop(
            @ApiParam("门店id, 不设置则查询平台商品") @RequestParam(name = "shopId", required = false) String shopId,
            @ApiParam("商品分类: well-sold:热销 boutique: 精品, 空字符或者不传： 所有类型") @RequestParam(name = "goodType", required = false) String goodType,
            @ApiParam("商品名称") @RequestParam(name = "goodName", required = false) String goodName,
            @ApiParam("商品状态") @RequestParam(name = "status", required = false) String status,
            @RequestParam("pageNo") int pageNo,
            @RequestParam("pageSize") int pageSize) {


        return goodService.searchGoodByShopId(shopId, goodName, goodType, null, 1, null, null, status, pageNo, pageSize);
    }

    @ApiOperation(value = "NEW-商品下架", notes = "")
    @PostMapping("lowerFrame")
    @ResponseBody
    @ShopAuth
    //商铺商品下架
    public Success lowerFrame(@RequestBody GoodShelfVO goodShelfVO) {
        goodService.upGoodShelf(goodShelfVO.getShopId(), goodShelfVO.getGoodId(), goodShelfVO.getGoodStatus());
        return Response.SUCCESS;
    }

    @ApiOperation(value = "NEW-商品上架", notes = "")
    @PostMapping("upGoodShelf")
    @ResponseBody
    @ShopAuth
    //商铺商品上架
    public Success upGoodShelf(@RequestBody GoodShelfVO goodShelfVO) {
        goodService.upGoodShelf(goodShelfVO.getShopId(), goodShelfVO.getGoodId(), goodShelfVO.getGoodStatus());
        return Response.SUCCESS;
    }
}