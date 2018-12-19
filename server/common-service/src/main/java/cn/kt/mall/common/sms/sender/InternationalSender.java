package cn.kt.mall.common.sms.sender;

import cn.kt.mall.common.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 国际短信接口
 * Created by wqt on 2018/2/2.
 */
public class InternationalSender {
    private static Logger logger = LoggerFactory.getLogger(InternationalSender.class);

    public static final String CHARSET = "UTF-8";

    public static final String URL = "http://intapi.253.com/send/json";

    public static InternationalRes send(String account, String password, String mobile, String message) {
        String params = JSONUtil.toJSONString(new InternationalReq(account, password, mobile, message));

        String result = InternationalHttpUtil.post(URL, params);
        return JSONUtil.parseToObject(result, InternationalRes.class);
    }

    public static void main(String[] args) {
        InternationalRes result = send("I2376600", "GBcrUoQYNdfcf1", "8613113654807", "hello");

        // String result = send("I2376600", "GBcrUoQYNdfcf1", "441618505872", "hello");
        System.out.println(JSONUtil.toJSONString(result));
    }
}


