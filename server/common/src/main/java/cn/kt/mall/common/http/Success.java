package cn.kt.mall.common.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 成功响应
 * Created by wqt on 2018/2/1.
 */
@ApiModel("操作成功响应")
public class Success {

    @ApiModelProperty(notes = "成功消息:success", dataType = "string")
    private String message;

    Success(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
