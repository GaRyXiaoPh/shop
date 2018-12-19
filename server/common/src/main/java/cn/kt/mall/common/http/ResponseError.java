package cn.kt.mall.common.http;

import lombok.Getter;
import lombok.Setter;

/**
 * 错误响应
 * Created by jerry on 2017/12/25.
 */
@Getter
@Setter
public class ResponseError {
    private int errorCode;
    private String message;
    private String developerMessage;

    public ResponseError(String message) {
        super();
        this.message = message;
    }

    public ResponseError(int errorCode, String message, String developerMessage) {
        super();
        this.errorCode = errorCode;
        this.message = message;
        this.developerMessage = developerMessage;
    }
}
