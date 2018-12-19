package cn.kt.mall.management.admin.controller;

import java.math.BigDecimal;
import java.util.List;

import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.kt.mall.common.common.service.SysSettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "参数管理", tags = "sys_setting_manage")
@RequestMapping("/sys/manage/")
@RestController
public class SysSettingController {

	@Autowired
	private SysSettingsService sysSettingsService;

	@ApiOperation(value = "获取凯撒网虚拟币人民币价格")
	@GetMapping("coin-price")
	@IgnoreJwtAuth
	public BigDecimal getCoinPrice() {
		BigDecimal price = sysSettingsService.getCurrencyPrice("popc");
		return price;
	}

	@ApiOperation(value = "获取阿里云oss配置")
	@GetMapping("getOssParam")
	public List<SysSettings> getOssParam() {
		return sysSettingsService.getOssParam();
	}

	@ApiOperation(value = "获取优惠券转让配置")
	@GetMapping("getCouponParam")
	public List<SysSettings> getCouponParam() {
		return sysSettingsService.getCouponParam();
	}

	@ApiOperation(value = "获取消费返优惠券配置")
	@GetMapping("getReturnParam")
	public List<SysSettings> getReturnParam() {
		return sysSettingsService.getReturnParam();
	}

	@ApiOperation(value = "彩票/游戏积分提取续费手续费")
	@GetMapping("getGameParam")
	public List<SysSettings> getGameParam() {
		return sysSettingsService.getGameParam();
	}
}
