package cn.kt.mall.front.shop;

import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.offline.entity.ShopTypeEntity;
import cn.kt.mall.offline.service.ShopTypeService;
import cn.kt.mall.shop.city.entity.CityEntity;
import cn.kt.mall.shop.city.service.CityService;
import cn.kt.mall.shop.city.vo.CityBaseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */
@Api(description = "信息管理模块", tags = "manage")
@RequestMapping("/manage")
@RestController
public class CityController {

	@Autowired
	private CityService cityService;

	@Autowired
	private ShopTypeService shopTypeService;

	// 获取省份
	@ApiOperation(value = "省份列表", notes = "")
	@IgnoreJwtAuth
	@ResponseBody
	@GetMapping("provinceList")
	public List<CityEntity> provinceList() {
		return cityService.getProvinceList();
	}

	// 根据省份获取市区列表
	@ApiOperation(value = "市区列表", notes = "")
	@IgnoreJwtAuth
	@ResponseBody
	@GetMapping("cityList")
	public List<CityEntity> cityList(@RequestParam("id") Long id) {
		return cityService.getCityList(id);
	}


	@ApiOperation(value = "区县列表", notes = "")
	@IgnoreJwtAuth
	@ResponseBody
	@GetMapping("countyList")
	public List<CityEntity> countyList(@RequestParam("id") Long id) {
		return cityService.getCountyList(id);
	}

	@ApiOperation(value = "省市区列表", notes = "")
	@IgnoreJwtAuth
	@ResponseBody
	@GetMapping("getAllCityList")
	public List<CityEntity> getAllCityList() {
		return cityService.getAllCityList();
	}


	@ApiOperation(value = "获取商户类型列表", notes = "")
	@IgnoreJwtAuth
	@ResponseBody
	@GetMapping("getShopType")
	public List<ShopTypeEntity> getShopType() {
		return shopTypeService.getTypeList();
	}

	// 获取所有城市列表
	@ApiOperation(value = "获取所有城市列表", notes = "")
	@IgnoreJwtAuth
	@ResponseBody
	@GetMapping("queryCityList")
	public List<CityBaseVO> queryCityList(@ApiParam(value = "搜索城市名称(查询所有传null或者'')") @RequestParam String name) {
		return cityService.queryCityList(name);
	}

	//根据城市名称获取城市sid
	@ApiOperation(value = "根据城市名称获取城市sid", notes = "")
	@IgnoreJwtAuth
	@ResponseBody
	@GetMapping("selectCodeById")
	public CityBaseVO selectCodeById(@ApiParam(value = "城市名称") @RequestParam String name) {
		return cityService.selectCodeById(name);
	}

}
