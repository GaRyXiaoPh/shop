package cn.kt.mall.common.asserts;

import cn.kt.mall.common.exception.BadRequestException;
import cn.kt.mall.common.exception.BusinessException;
import cn.kt.mall.common.exception.UnauthorizedException;

/**
 * 断言工具类
 * Created by jerry on 2017/12/21.
 */
public class A {

    public static void check(boolean check, String msg) {
        if(check) {
            throw new BusinessException(msg);
        }
    }

    public static void check(boolean check, int status, String msg) {
        if(check) {
            throw new BusinessException(status, msg);
        }
    }
    public static void check(String msg) {
        throw new BusinessException(msg);
    }
    public static void checkParam(boolean check, String msg) {
        if(check) {
            throw new BadRequestException(msg);
        }
    }

    /**
     * 判断枚举类型参数断言
     * @param clazz 枚举类型
     * @param name 枚举名称
     * @param message 断言失败消息
     */
    public static <T extends Enum<T>> T assertEnumParam(Class<T> clazz, String name, String message) {
        try {
            return Enum.valueOf(clazz, name);
        } catch (Exception e) {
            throw new BadRequestException(message);
        }
    }

    public static void checkAuth(boolean check, String msg) {
        if(check) {
            throw new UnauthorizedException(msg);
        }
    }
}
