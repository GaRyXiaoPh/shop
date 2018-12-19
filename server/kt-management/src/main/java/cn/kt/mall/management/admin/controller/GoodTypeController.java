package cn.kt.mall.management.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.util.TreeNodeUtil;
import cn.kt.mall.common.vo.JsonTreeVO;
import cn.kt.mall.shop.good.entity.GoodTypeEntity;
import cn.kt.mall.shop.good.service.GoodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

//@Api(description = "商品分类管理", tags = "good-type-namage")
//@RequestMapping("/manage/")
//@RestController
public class GoodTypeController {

	@Autowired
	private GoodService goodService;

	@ApiOperation(value = "添加/修改商品分类")
	@PostMapping("addGoodType")
	public Success addGoodType(@RequestBody GoodTypeEntity goodTypeEntity) {
		if(StringUtils.isEmpty(goodTypeEntity.getId())) {
			goodTypeEntity.setId(IDUtil.getUUID());
			goodService.addGoodType(goodTypeEntity);
		}else {
			goodService.updateGoodType(goodTypeEntity);
		}
		return Response.SUCCESS;
	}

	// 获取商品分类
	@ApiOperation(value = "获取所有商品分类树形结构", notes = "")
	@GetMapping("listAllType")
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
				jsonTreeVO.setSort(goodTypeEntity.getSort());
				result.add(jsonTreeVO);
			}
			return TreeNodeUtil.getParentNode(result);
		}
		return result;
	}
	
	@ApiOperation(value = "删除商品分类")
	@PostMapping("delGoodType")
	public Success delGoodType(@RequestParam("typeId") String typeId) {
		goodService.delGoodType(typeId);
		return Response.SUCCESS;
	}
}
