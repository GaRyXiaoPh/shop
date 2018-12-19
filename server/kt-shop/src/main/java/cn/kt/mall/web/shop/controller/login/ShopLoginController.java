package cn.kt.mall.web.shop.controller.login;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.user.vo.LoginVO;
import cn.kt.mall.web.shop.service.impl.ShopLoginServiceImpl;
import cn.kt.mall.web.shop.vo.ShopInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 线上商家端登录接口
 */
@Api(value = "商家端登录模块", tags = "online-shop-login")
@RequestMapping("/shop/user")
@RestController
public class ShopLoginController {

    @Autowired
    private ShopLoginServiceImpl shopLoginServiceImpl;

    @IgnoreJwtAuth
    @ApiOperation(value = "商城登录", notes = "")
    @PostMapping("login")
    @ResponseBody
    public ShopInfoVO shopLogin(LoginVO loginVO)
    {
        return shopLoginServiceImpl.shopLogin(loginVO);
    }

    @ApiOperation(value = "登出")
    @DeleteMapping("logout")
    public Success logout(@RequestHeader("access-token") @ApiIgnore String accessToken) {

        this.shopLoginServiceImpl.logout(accessToken);
        return Response.SUCCESS;
    }
}
