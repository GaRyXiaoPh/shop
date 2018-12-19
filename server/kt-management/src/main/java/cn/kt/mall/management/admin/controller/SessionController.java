package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.admin.model.CurrentAdmin;
import cn.kt.mall.management.admin.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 会话信息 Controller
 * Created by jerry on 2017/12/28.
 */
@Api(description = "用户-会话", tags = "management-session")
@RequestMapping("/session")
@RestController
public class SessionController {

    private AdminService adminService;

    @Autowired
    public SessionController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiOperation(value = "获取当前登录用户信息", notes = "返回:登录信息")
    @GetMapping("current")
    public CurrentAdmin getCurrent(@RequestHeader("access-token") @ApiIgnore String accessToken) {
        return this.adminService.getCurrent(accessToken);
    }

    @ApiOperation(value = "检查当前用户登录情况", notes = "返回:boolean, true 登录，false 未登录")
    @GetMapping("check")
    public boolean check(@RequestHeader("access-token") @ApiIgnore String accessToken) {
        return this.adminService.check(accessToken);
    }
}
