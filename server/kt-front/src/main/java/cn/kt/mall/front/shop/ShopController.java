package cn.kt.mall.front.shop;

import java.util.List;

import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.shop.good.constant.Constants;
import cn.kt.mall.shop.good.vo.GoodRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Result;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.shop.good.entity.GoodEntity;
import cn.kt.mall.shop.good.service.GoodService;
import cn.kt.mall.shop.shop.service.ShopService;
import cn.kt.mall.shop.shop.service.TradeCommentService;
import cn.kt.mall.shop.shop.vo.ApplyShopVO;
import cn.kt.mall.shop.shop.vo.AuthDataVO;
import cn.kt.mall.shop.shop.vo.ShopCommentVO;
import cn.kt.mall.shop.shop.vo.ShopVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "shop-线上&线下店铺管理", tags = "shop-shop")
@RequestMapping("/shop/shop")
@RestController
public class ShopController {

	@Autowired
	ShopService shopService;
	@Autowired
	GoodService goodService;
	@Autowired
	TradeCommentService tradeCommentService;

	// 获取店铺
	@ApiOperation(value = "根据店铺id获取店铺（店铺主页）--NEW", notes = "shopType:0平台(自营)商户(预留字段), 1地面商户, 2网上商户")
	@IgnoreJwtAuth
	@GetMapping("shopByShopId")
	@ResponseBody
	public ShopVO getShopByShopId(@RequestParam("shopId") String shopId,
			@RequestParam(value = "userId", required = false) String userId) {
		return shopService.getShopByShopId(shopId, userId);
	}

	// 获取店铺
	@ApiOperation(value = "获取店铺认证资料--NEW", notes = "shopType:0平台(自营)商户(预留字段), 1地面商户, 2网上商户")
	@GetMapping("shopAuthData")
	@ResponseBody
	public AuthDataVO getShopAuthData(@RequestParam("shopId") String shopId) {
		String userId = SubjectUtil.getCurrent().getId();
		return shopService.getShopAuthData(userId, shopId);
	}

	// 获取店铺商品
/*	@ApiOperation(value = "获取店铺商品列表--NEW")
	@GetMapping("goods")
	@ResponseBody
	public PageVO<GoodEntity> getShopGoods(@RequestParam("shopId") String shopId, @RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize,@RequestParam("userId") String  userId,@RequestParam(value = "goodType", required = false) String goodType) {
		return goodService.getGoodByShopId(shopId,goodType, pageNo, pageSize,userId);
	}*/

	// 获取店铺评论列表
	@ApiOperation(value = "获取店铺评论列表--NEW")
	@GetMapping("comment")
	@ResponseBody
	public PageVO<ShopCommentVO> getShopComment(@RequestParam("shopId") String shopId,
			@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
		return tradeCommentService.getShopComment(shopId, null, null, null, pageNo, pageSize);
	}

	// 获取我开通店铺
	@ApiOperation(value = "获取当前用户开通的店铺--NEW", notes = "shopType:0平台(自营)商户, 1地面商户, 2网上商户")
	@GetMapping("myShop")
	@ResponseBody
	public Result<List<ShopVO>> getShop() {
		String userId = SubjectUtil.getCurrent().getId();
		return Response.result(shopService.getShop(userId));
	}

	// 申请开通线上商城
	@ApiOperation(value = "申请开通线上商城", notes = "")
	@PostMapping("applyShopOnline")
	@ResponseBody
	public Success applyShopOnline(@RequestBody ApplyShopVO applyShopVO) {
		shopService.applyShopOnline(SubjectUtil.getCurrent().getId(), applyShopVO);
		return Response.SUCCESS;
	}

	// 申请开通线下商圈
	@ApiOperation(value = "申请开通线下商圈", notes = "")
	@PostMapping("applyShopOffline")
	@ResponseBody
	public Success applyShopOffline(@RequestBody ApplyShopVO applyShopVO) {
		shopService.applyShopOffline(SubjectUtil.getCurrent().getId(), applyShopVO);
		return Response.SUCCESS;
	}
	// 商城店铺搜索
	@ApiOperation(value = "获取店铺(店铺搜索)--NEW", notes = "shopLevel: 2(零售店)、3(批发店)")
	@GetMapping("searchShops")
	@IgnoreJwtAuth
	@ResponseBody
	public CommonPageVO<GoodRespVO> searchShops(@RequestParam(name = "shopName", required = false) String shopName,
												@RequestParam(value = "userId", required = false) String userId,
												@RequestParam("pageNo") int pageNo,
												@RequestParam("pageSize") int pageSize) {
		return shopService.searchShops(null, userId,shopName, Constants.GoodStatus.GOOD_PASS, null, null, pageNo,
				pageSize);
	}
	// 商城店铺内搜索商品
	@ApiOperation(value = "商城店铺内搜索商品--NEW", notes = "shopLevel: 2(零售店)、3(批发店)")
	@GetMapping("searchShopGood")
	@IgnoreJwtAuth
	@ResponseBody
	public CommonPageVO<GoodRespVO> searchShopGood(@RequestParam(name = "goodName", required = false) String goodName,
												@RequestParam(value = "userId", required = false) String userId,
											    @RequestParam(value = "shopId") String shopId,
												@RequestParam("pageNo") int pageNo,
												@RequestParam("pageSize") int pageSize) {
		return shopService.searchShopGoods(shopId, userId,goodName, null,Constants.GoodStatus.GOOD_PASS, null, null, null,pageNo,
				pageSize);
	}
}
