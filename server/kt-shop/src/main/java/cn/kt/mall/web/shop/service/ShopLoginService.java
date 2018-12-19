package cn.kt.mall.web.shop.service;

import cn.kt.mall.common.jwt.JwtValidator;
import cn.kt.mall.common.jwt.PermissionValidator;
import cn.kt.mall.common.user.vo.LoginVO;
import cn.kt.mall.web.shop.vo.ShopInfoVO;

public interface ShopLoginService extends JwtValidator, PermissionValidator {

    ShopInfoVO shopLogin(LoginVO loginVO);
    void logout(String accessToken);
}
