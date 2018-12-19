package cn.kt.mall.offline.web.user.controller;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.user.vo.LoginInfoVO;
import cn.kt.mall.common.user.vo.LoginVO;
import cn.kt.mall.offline.web.user.service.UserService;
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
@Api(description = "用户-登录&登出", tags = "user-login-logout")
@RestController
public class SubjectController {

    private UserService userService;

    @Autowired
    public SubjectController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "登录", notes = "返回:登录信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "LoginVO", name = "login", value = "账号信息", defaultValue = "{\"username\":\"jim\",\"password\":\"123\"}", required = true, paramType = "body")
    })
    @PostMapping("/login")
    public LoginInfoVO login(@RequestBody @ApiIgnore LoginVO loginVO) {
        return this.userService.login(loginVO);
    }

    @ApiOperation(value = "登出")
    @DeleteMapping("/logout")
    public Success logout(@RequestHeader("access-token") @ApiIgnore String accessToken) {
        this.userService.logout(accessToken);
        return Response.SUCCESS;
    }
}
