package cn.kt.mall.common.jwt;

import cn.kt.mall.common.exception.UnauthorizedException;

/**
 * JWT 验证器
 * Created by jerry on 2018/1/2.
 */
public interface JwtValidator {

    SubjectInstance verify(String jwt) throws UnauthorizedException;

    SubjectInstance verifyShop(String userId) throws UnauthorizedException;
}
