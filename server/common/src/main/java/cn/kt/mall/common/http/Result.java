package cn.kt.mall.common.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("响应结果")
@Getter
@Setter
@NoArgsConstructor
public class Result<T> {

    @ApiModelProperty(notes = "响应结果")
    private T result;

    public Result(T result) {
        this.result = result;
    }
}
