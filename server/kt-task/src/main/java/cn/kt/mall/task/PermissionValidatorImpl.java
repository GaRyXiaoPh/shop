package cn.kt.mall.task;

import cn.kt.mall.common.exception.ForbiddenException;
import cn.kt.mall.common.jwt.PermissionValidator;
import org.springframework.stereotype.Component;

@Component
public class PermissionValidatorImpl implements PermissionValidator {
    @Override
    public void check(String userId, String[] roles, String[] privileges) throws ForbiddenException {

    }
}
