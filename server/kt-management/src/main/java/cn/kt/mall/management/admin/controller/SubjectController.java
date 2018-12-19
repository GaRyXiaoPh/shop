package cn.kt.mall.management.admin.controller;

import cn.kt.mall.management.admin.service.AdminService;
import cn.kt.mall.management.admin.vo.LoginInfoVO;
import cn.kt.mall.management.admin.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 用户状态管理 Controller
 * Created by jerry on 2017/12/29.
 */
@Api(description = "用户-登录&登出", tags = "management-login-logout")
@RestController
public class SubjectController {

    private AdminService adminService;

    @Autowired
    public SubjectController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiOperation(value = "登录", notes = "返回:登录信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "LoginVO", name = "login", value = "账号信息", defaultValue = "{\"account\":\"jim\",\"password\":\"123\"}", required = true, paramType = "body")
    })
    @PostMapping("/login")
    public LoginInfoVO login(@RequestBody @ApiIgnore LoginVO loginVO) {
        return this.adminService.login(loginVO);
    }

    @ApiOperation(value = "登出")
    @DeleteMapping("/logout")
    public void logout(@RequestHeader("access-token") @ApiIgnore String accessToken) {
        this.adminService.logout(accessToken);
    }
}
