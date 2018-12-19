package cn.kt.mall.common.jwt;

import java.lang.annotation.*;

/**
 * 忽略JWT验证
 * Created by jerry on 2018/1/2.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreJwtAuth {

}
