package cn.kt.mall.common.http;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 成功响应
 * Created by wqt on 2018/2/1.
 */
@Getter
@Setter
@NoArgsConstructor
public class Response {

    private static final String MESSAGE = "success";

    public static final Success SUCCESS = new Success(MESSAGE);

    public static <T> Result<T> result(T result) {
        return new Result<>(result);
    }
}
