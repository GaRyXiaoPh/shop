package cn.kt.mall.offline.web.user.controller;

import cn.kt.mall.common.user.model.CurrentUser;
import cn.kt.mall.offline.web.user.service.UserService;
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
@Api(description = "用户-会话", tags = "user-session")
@RequestMapping("/session")
@RestController
public class SessionController {

    private UserService userService;

    @Autowired
    public SessionController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "获取当前登录用户信息", notes = "返回:登录信息")
    @GetMapping("current")
    public CurrentUser getCurrent(@RequestHeader("access-token") @ApiIgnore String accessToken) {
        return this.userService.getCurrent(accessToken);
    }

    @ApiOperation(value = "检查当前用户登录情况", notes = "返回:boolean, true 登录，false 未登录")
    @GetMapping("check")
    public boolean check(@RequestHeader("access-token") @ApiIgnore String accessToken) {
        return this.userService.check(accessToken);
    }
}
