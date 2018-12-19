package cn.kt.mall.offline.web.controller;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.offline.service.OfflineUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Administrator on 2018/4/27.
 */
@Api(description = "Web商圈", tags = "circle_web")
@RequestMapping("/circle/web")
@RestController
public class LoginController {
    @Autowired
    private OfflineUserService offlineUserService;

    @ApiOperation(value = "商户登录")
    @IgnoreJwtAuth
    @GetMapping("login")
    public Map login(@Param("username") String username, @Param("pwd") String pwd) {
        return offlineUserService.login(username,pwd);
    }
}
