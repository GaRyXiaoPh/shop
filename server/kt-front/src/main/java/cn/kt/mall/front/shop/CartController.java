package cn.kt.mall.front.shop;

import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.wallet.service.ShopSalesAmountService;
import cn.kt.mall.front.user.service.MyAssetService;
import cn.kt.mall.shop.cart.service.CartService;
import cn.kt.mall.shop.cart.vo.BuyGoodVO;
import cn.kt.mall.shop.cart.vo.CartVO;
import cn.kt.mall.shop.cart.vo.UpdateCartVO;
import cn.kt.mall.shop.good.service.GoodService;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "shop-购物车", tags = "shop-cart")
@RequestMapping("/shop/cart")
@RestController
public class CartController {

	@Autowired
	CartService cartService;
	@Autowired
	MyAssetService myAssetService;
	@Autowired
	ShopSalesAmountService shopSalesAmountService;
	@Autowired
	SysSettingsService sysSettingsService;
	@Autowired
	GoodService goodService;
	// 添加购物车
	@ApiOperation(value = "添加购物车", notes = "")
	@PostMapping("add")
	@ResponseBody
	public Success addCart(@RequestBody BuyGoodVO buyGoodVO,@RequestParam("pidFlag") String  pidFlag) {
		//String goodName =  sysSettingsService.getGoodName();
		/*if(buyGoodVO.getGoodName().equals("正宇汽车")){
			goodService.checkShopSalesAmountByShopIdAndGoodId(buyGoodVO.getShopId(),buyGoodVO.getGoodId(),buyGoodVO.getBuyNum());
		}*/
		//验证店铺信用金
		//shopSalesAmountService.checkShopSalesAmount(buyGoodVO.getShopId());
		cartService.addCart(SubjectUtil.getCurrent().getId(), buyGoodVO,pidFlag);
		return Response.SUCCESS;
	}

	// 从购物车删除
	@ApiOperation(value = "从购物车删除", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "id:购物车列表返回的ID", name = "id", paramType = "query", required = true) })
	@DeleteMapping("delete")
	@ResponseBody
	public Success delCart(@RequestParam("id") String id) {
		cartService.delCart(SubjectUtil.getCurrent().getId(), id);
		return Response.SUCCESS;
	}

	// 从购物车批量删除
	@ApiOperation(value = "从购物车批量删除", notes = "")
	@PostMapping("delBatch")
	@ResponseBody
	public Success delCartBatch(@RequestBody String[] ids) {
		cartService.delCartByBatch(SubjectUtil.getCurrent().getId(), ids);
		return Response.SUCCESS;
	}

	// 批量修改购物车里面商品数量
	@ApiOperation(value = "批量修改购物车里面商品数量", notes = "")
	@PutMapping("updBatch")
	@ResponseBody
	public Success updBatch(@RequestBody UpdateCartVO updateCartVO) {
		cartService.updCartByBatch(updateCartVO);
		return Response.SUCCESS;
	}

	// 请求我的购物车
	@ApiOperation(value = "我的购物车列表--NEW", notes = "")
	@GetMapping("myCart")
	@ResponseBody
	public List<CartVO> myCart() {
		return cartService.myCart(SubjectUtil.getCurrent().getId());
	}

	// 查询购物车商品数量
	@ApiOperation(value = "查询购物车商品数量--NEW", notes = "")
	@GetMapping("myCartGoodsNum")
	@ResponseBody
	public int myCartGoodsNum() {
		return cartService.getCartGoodsCountByUserId(SubjectUtil.getCurrent().getId());
	}
}
