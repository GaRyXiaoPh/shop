package cn.kt.mall.common.jwt;

import java.lang.annotation.*;

/**
 * 角色权限检查
 * Created by jerry on 2018/1/2.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionCheck {
    public String[] roles() default {"aaa","bbb","ccc"};

    public String[] privileges() default {"111"};
}
