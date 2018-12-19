package cn.kt.mall.common.sms.model;

import lombok.Getter;

/**
 * 短信类型
 * Created by wqt on 2017/11/27.
 */
@Getter
public enum SmsType {
    CAPTCHA("01", "验证码短信");

    private String code;
    private String name;

    SmsType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
