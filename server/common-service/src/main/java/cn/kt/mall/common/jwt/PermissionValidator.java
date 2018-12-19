package cn.kt.mall.common.jwt;

import cn.kt.mall.common.exception.ForbiddenException;

/**
 * 权限 验证器
 * Created by jerry on 2018/1/2.
 */
public interface PermissionValidator {

    void check(String userId, String[] roles, String[] privileges) throws ForbiddenException;
}
