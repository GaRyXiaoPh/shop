package cn.kt.mall.front.version.controller;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.front.version.service.AppVersionService;
import cn.kt.mall.front.version.vo.VersionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * APP版本更新模块 Controller
 * Created by jerry on 2017/12/21.
 */
@Api(description = "APP版本更新", tags = "app-version")
@RestController
public class AppVersionController {

    private AppVersionService appVersionService;

    @Autowired
    public AppVersionController(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }

    @ApiOperation(value = "获取最新版本信息")
    @GetMapping("/app/version/latest")
    public VersionVO appVersions(@RequestParam("platform") String platform) {
        A.check(platform == null || (platform.compareTo("ios")!=0 && platform.compareTo("android")!=0), "系统平台不对");
        return this.appVersionService.getVersion(platform);
    }
}
