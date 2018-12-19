package cn.kt.mall.front.shop;

import java.util.ArrayList;
import java.util.List;

import cn.kt.mall.front.jwt.Subject;
import cn.kt.mall.shop.good.vo.AdResVO;
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
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.util.TreeNodeUtil;
import cn.kt.mall.common.vo.JsonTreeVO;
import cn.kt.mall.shop.collect.service.CollectService;
import cn.kt.mall.shop.good.constant.Constants;
import cn.kt.mall.shop.good.entity.GoodEntity;
import cn.kt.mall.shop.good.entity.GoodTypeEntity;
import cn.kt.mall.shop.good.service.GoodService;
import cn.kt.mall.shop.good.vo.GoodRespVO;
import cn.kt.mall.shop.good.vo.GoodVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "shop-线上商品", tags = "shop-good")
@RequestMapping("/shop/good")
@RestController
public class GoodController {

	@Autowired
	GoodService goodService;
	@Autowired
	CollectService collectService;

	// 获取商品分类
	@ApiOperation(value = "根据parentId获取商品分类--NEW", notes = "parentId为空获取第一级")
	@GetMapping("goodType")
	@IgnoreJwtAuth
	@ResponseBody
	public List<GoodTypeEntity> getGoodType(@RequestParam(name = "parentId", required = false) String parentId) {
		return goodService.getGoodType(parentId);
	}

	// 获取商品分类
	@ApiOperation(value = "获取所有商品分类树形结构--NEW", notes = "")
	@GetMapping("listAllType")
	@IgnoreJwtAuth
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

	// 获取商品详细信息
	@ApiOperation(value = "获取商品详细信息 -- NEW", notes = "goodId必传")
	@GetMapping("getGood")
	@IgnoreJwtAuth
	@ResponseBody
	public GoodVO getGood(@RequestParam("goodId") String goodId,@RequestParam("shopId") String shopId,
			@RequestParam(value = "userId", required = false) String userId) {
		return goodService.getGoodByGoodId(goodId, userId,shopId,Constants.GoodStatus.GOOD_PASS);
	}

	// 商城商品搜索
	@ApiOperation(value = "获取商品(商品搜索)--NEW", notes = "searchMode代表排序规则，1代表综合排序，2代表新品优先，3代表销量优先")
	@GetMapping("searchGoods")
	@IgnoreJwtAuth
	@ResponseBody
	public CommonPageVO<GoodRespVO> searchGoods(@RequestParam(name = "name", required = false) String goodName,
												@RequestParam("searchMode") int searchMode,
												@RequestParam(name = "goodType", required = false) String goodType, @RequestParam("pageNo") int pageNo,
												@RequestParam("pageSize") int pageSize) {
		return goodService.searchGoods(null, goodName, goodType, Constants.GoodStatus.GOOD_PASS, searchMode, null, null, pageNo,
				pageSize);
	}
	// 商城商品搜索
	@ApiOperation(value = "web前台获取商品(商品搜索)--NEW", notes = "searchMode代表排序规则，1代表综合排序，2代表新品优先，3代表销量优先; shopLevel: 2(零售店)、3(批发店)")
	@GetMapping("searchGoodsFront")
	@IgnoreJwtAuth
	@ResponseBody
	public CommonPageVO<GoodRespVO> searchGoodsFront(@RequestParam(name = "name", required = false) String goodName,
												@RequestParam(name = "searchMode",required = false) String  searchMode,
												 @RequestParam(name = "userId",required = false) String userId,
												@RequestParam(name = "goodType", required = false) String goodType, @RequestParam("pageNo") int pageNo,
												@RequestParam("pageSize") int pageSize) {
		return goodService.searchGoodsFront(null,userId, goodName, goodType, Constants.GoodStatus.GOOD_PASS, searchMode, null, null, pageNo,
				pageSize);
	}

	// 获取店铺所有商品
	@ApiOperation(value = "获取店铺所有商品", notes = "")
	@GetMapping("goodByShopId")
	@IgnoreJwtAuth
	@ResponseBody
	public PageVO<GoodEntity> getGoodByShopId(@RequestParam("shopId") String shopId, @RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize,@RequestParam("userId") String  userId,@RequestParam(value = "goodType", required = false) String goodType,
											  @RequestParam(value = "sort", required = false) String sort) {
		return goodService.getGoodByShopId(shopId,goodType, pageNo, pageSize,userId,sort);
	}
	// 收藏商品
	@ApiOperation(value = "添加商品收藏", notes = "")
	@PutMapping("collect")
	@ResponseBody
	public Success addGoodCollect(@RequestParam("goodId") String goodId) {
		collectService.addGoodCollect(SubjectUtil.getCurrent().getId(), goodId);
		return Response.SUCCESS;
	}

	// 批量取消商品收藏
	@ApiOperation(value = "批量取消商品收藏--NEW", notes = "")
	@PostMapping("unCollectBatch")
	@ResponseBody
	public Success unCollectBatch(@RequestParam("goodId") String[] goodIds) {
		collectService.delGoodCollect(SubjectUtil.getCurrent().getId(), goodIds);
		return Response.SUCCESS;
	}

	// 获取我收藏的商品列表
	@ApiOperation(value = "获取我收藏的商品列表--NEW", notes = "")
	@GetMapping("listCollect")
	@ResponseBody
	public PageVO<GoodEntity> listCollect(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
		return collectService.getGoodCollectEntityList(SubjectUtil.getCurrent().getId(), pageNo, pageSize);
	}

	// 首页展示
	@ApiOperation(value = "首页展示--NEW", notes = "goodType 传商品分类Id; shopLevel: 2(零售店)、3(批发店)")
	@GetMapping("listAllGoodsByType")
	@IgnoreJwtAuth
	@ResponseBody
	public PageVO<GoodRespVO> listAllGoodsByType(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize,
												 @RequestParam(name = "goodType", required = false) String goodType,
												 @RequestParam(value = "userId", required = false) String userId) {
		//String userId = Subject.getCurrent().getId();
		return goodService.searchGoodsByType(goodType, Constants.GoodStatus.GOOD_PASS,userId, pageNo, pageSize);
	}
	// 首页展示
	@ApiOperation(value = "首页轮播图", notes = "")
	@GetMapping("listAdvertise")
	@IgnoreJwtAuth
	@ResponseBody
	public List<AdResVO> listAdvertise() {
		return goodService.getAdResVOList();
	}
}
