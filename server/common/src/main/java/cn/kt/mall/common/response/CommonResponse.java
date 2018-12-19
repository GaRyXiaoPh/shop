package cn.kt.mall.common.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by Administrator on 2017/7/19.
 */
@Setter
@Getter
@Accessors(chain = true)
public class CommonResponse <T> {
    protected int statusCode; //0为正常，其他为错误
    protected String errorMessage;// 错误描述，当statusCode不是0时不为空
    protected T content; //业务相关内容


    public CommonResponse(int statusCode){
        this.statusCode = statusCode;
    }

    public CommonResponse(int statusCode,String errorMessage){
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public CommonResponse(T content) {
        this.statusCode = 0;
        this.content = content;
    }

    public CommonResponse() {

    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
