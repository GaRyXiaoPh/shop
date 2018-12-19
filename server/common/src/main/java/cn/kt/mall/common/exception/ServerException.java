package cn.kt.mall.common.exception;


import lombok.Getter;
import lombok.Setter;

/**
 * 服务器异常
 * Created by jerry on 2017/12/21.
 */
@Getter
@Setter
public class ServerException extends RuntimeException {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
