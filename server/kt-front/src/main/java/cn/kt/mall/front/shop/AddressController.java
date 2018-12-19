package cn.kt.mall.front.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.shop.address.service.AddressService;
import cn.kt.mall.shop.address.vo.AddressVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "shop-用户地址管理", tags = "shop-address")
@RequestMapping("/shop/address")
@RestController
public class AddressController {
	@Autowired
	AddressService addressService;

	// 获取用户地址
	@ApiOperation(value = "获取用户地址", notes = "")
	@GetMapping("list")
	@ResponseBody
	public List<AddressVO> getUserAddress() {
		return addressService.getUserAddress(SubjectUtil.getCurrent().getId());
	}

	// 获取用户地址
	@ApiOperation(value = "添加或者修改用户地址", notes = "")
	@PostMapping("add")
	@ResponseBody
	public Success addUserAddress(@RequestBody AddressVO addressVO) {
		String userId = SubjectUtil.getCurrent().getId();
		addressService.addUserAddress(userId, addressVO);
		return Response.SUCCESS;
	}

	// 删除用户地址
	@ApiOperation(value = "删除用户地址", notes = "")
	@DeleteMapping("del/{id}")
	@ResponseBody
	public Success delUserAddress(@PathVariable("id") String id) {
		String userId = SubjectUtil.getCurrent().getId();
		addressService.delUserAddress(id, userId);
		return Response.SUCCESS;
	}

	// 设置默认地址
	@ApiOperation(value = "设置默认地址")
	@GetMapping("default")
	@ResponseBody
	public Success defUserAddress(@RequestParam("id") String id) {
		String userId = SubjectUtil.getCurrent().getId();
		addressService.defUserAddress(id, userId);
		return Response.SUCCESS;
	}
}
