package cn.kt.mall.front.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.shop.collect.service.CollectService;
import cn.kt.mall.shop.collect.vo.ShopCollectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "shop-线上&线下店铺收藏", tags = "shop-collect")
@RequestMapping("/shop/collect")
@RestController
public class CollectController {

	@Autowired
	CollectService collectService;

	// 收藏店铺
	@ApiOperation(value = "添加店铺收藏", notes = "")
	@PutMapping("addShop")
	@ResponseBody
	public Success addShopCollect(@RequestParam("shopId") String shopId) {
		collectService.addShopCollect(SubjectUtil.getCurrent().getId(), shopId);
		return Response.SUCCESS;
	}

	// 批量取消收藏
	@ApiOperation(value = "批量取消店铺收藏-NEW", notes = "批量删除收藏店铺")
	@PostMapping("delShopBatch")
	@ResponseBody
	public Success delShopCollectByBatch(@RequestParam("shopIds") String[] shopIds) {
		collectService.delShopCollect(SubjectUtil.getCurrent().getId(), shopIds);
		return Response.SUCCESS;
	}

	// 获取我收藏的店铺列表
	@ApiOperation(value = "获取我收藏的店铺列表--NEW", notes = "")
	@GetMapping("listCollectShop")
	@ResponseBody
	public PageVO<ShopCollectVO> listCollectShop(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize) {
		return collectService.listCollectShop(SubjectUtil.getCurrent().getId(), pageNo, pageSize);
	}

}
