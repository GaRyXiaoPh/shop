package cn.kt.mall.front.settings.controller;

import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

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

}
