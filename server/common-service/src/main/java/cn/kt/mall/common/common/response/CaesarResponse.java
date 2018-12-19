package cn.kt.mall.common.common.response;

import lombok.Data;

@Data
public class CaesarResponse {
    private Integer statusCode;
    private String errorMessage;
    private String content;
}
