package cn.kt.mall.common.exception;

/**
 * 拒绝访问异常
 * Created by jerry on 2017/12/28.
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("你没有权限进行当前操作");
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
