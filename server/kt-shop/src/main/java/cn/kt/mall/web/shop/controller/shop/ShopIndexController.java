package cn.kt.mall.web.shop.controller.shop;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.jwt.ShopAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.shop.trade.service.TradeService;
import cn.kt.mall.shop.trade.vo.ManageCountRespVO;

@Api(value = "商家端主页模块", tags = "online-shop-index")
@RequestMapping("/shop/index")
@RestController
public class ShopIndexController {
	
	@Autowired
	private TradeService tradeService;

	/**
	 * 获取商家端主页信息
	 */
	@ApiOperation(value = "获取统计数据", notes = "")
	@GetMapping("info")
	@ResponseBody
	@ShopAuth
	public ManageCountRespVO getShopIndexInfo() {
		return tradeService.tradeCountInfo(SubjectUtil.getCurrentShop().getId(), SubjectUtil.getCurrent().getId());
	}
}
