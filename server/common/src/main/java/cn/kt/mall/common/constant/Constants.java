package cn.kt.mall.common.constant;

/**
 * 公用常量类
 * Created by jerry on 2017/12/25.
 */
public final class Constants {

    public static final String CHARSET = "UTF-8";

    public static final String SUCCESS = "success";

    public static final String STATE_SUCCESS = "1";

    public static final int DEFAULT_0 = 0;
    public static final int DEFAULT_1 = 1;

    public final class Header {
        public static final String ACCESS_TOKEN = "access-token";
    }

    public final class ResponseErrorStatus {
        public static final String BUSINESS = "business";

        public static final String ERROR = "system";
    }

    public final class ResponseMessage {
        public static final String ERROR = "系统异常，我们会尽快处理";
    }
}
