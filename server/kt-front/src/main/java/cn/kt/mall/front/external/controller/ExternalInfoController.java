package cn.kt.mall.front.external.controller;

import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.front.external.entity.NationalCodeEntity;
import cn.kt.mall.front.external.service.ExternalInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 外部信息
 * Create by wqt on 2018/1/29.
 */
@Api(description = "外部信息", tags = "external-info")
@IgnoreJwtAuth
@RequestMapping("/external/info")
@RestController
public class ExternalInfoController {

    private ExternalInfoService externalInfoService;

    @Autowired
    public ExternalInfoController(ExternalInfoService externalInfoService) {
        this.externalInfoService = externalInfoService;
    }

    @ApiOperation(value = "获取国家地区代码列表", notes = "返回:国家地区代码列表")
    @GetMapping("national_codes")
    public List<NationalCodeEntity> getNationalCodes() {
        return this.externalInfoService.getNationalCodes();
    }
}
