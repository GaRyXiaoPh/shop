package cn.kt.mall.web.shop.controller.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import cn.kt.mall.shop.shop.entity.ShopTemplateEntity;
import cn.kt.mall.shop.shop.service.ShopTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "商家端店铺物流模版", tags = "online-shop-template")
@RequestMapping("/shop/template")
@RestController
public class ShopTemplateController {
	@Autowired
	private ShopTemplateService shopTemplateService;

	@ApiOperation(value = "获取该店铺物流模版", notes = "")
	@GetMapping("listTemplate")
	@ResponseBody
	@ShopAuth
	public CommonPageVO<ShopTemplateEntity> listTemplate(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize) {
		return shopTemplateService.listTemplate(SubjectUtil.getCurrentShop().getId(), pageNo, pageSize);
	}

	@ApiOperation(value = "编辑物流模版", notes = "")
	@PostMapping("addTemplate")
	@ResponseBody
	@ShopAuth
	public Success addTemplate(@RequestBody @Validated ShopTemplateEntity shopTemplateEntity) {
		shopTemplateEntity.setShopId(SubjectUtil.getCurrentShop().getId());
		shopTemplateService.addTemplate(shopTemplateEntity);
		return Response.SUCCESS;
	}

	@ApiOperation(value = "编辑物流模版", notes = "")
	@DeleteMapping("delTemplate")
	@ResponseBody
	@ShopAuth
	public Success delTemplate(@RequestParam("templateId") String templateId) {
		shopTemplateService.delTemplate(SubjectUtil.getCurrentShop().getId(), templateId);
		return Response.SUCCESS;
	}
	
	@ApiOperation(value = "获取所有物流模版", notes = "")
	@GetMapping("getAllTemplate")
	@ResponseBody
	@ShopAuth
	public List<ShopTemplateEntity> getAllTemplate() {
		return shopTemplateService.getAllTemplate(SubjectUtil.getCurrentShop().getId());
	}
}
