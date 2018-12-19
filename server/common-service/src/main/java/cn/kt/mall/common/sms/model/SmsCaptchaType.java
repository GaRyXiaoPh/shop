package cn.kt.mall.common.sms.model;

import lombok.Getter;

/**
 * 短信验证码类型枚举类
 * Created by wqt on 2017/9/6.
 */
@Getter
public enum SmsCaptchaType {
    REGISTER("01", "您正在注册新账号，验证码%@%。妥善保管，请勿转发，5分钟内有效"),
    PASSWORD_FORGET("02", "您正在进行忘记密码操作，验证码%@%。妥善保管，请勿转发，5分钟内有效"),
    TRANSACTION_PASSWORD("03", "您正在进行设置交易密码操作，验证码%@%。妥善保管，请勿转发，5分钟内有效"),
    TRANSACTION_PASSWORD_EDIT("04", "您正在进行修改交易密码操作，验证码%@%。妥善保管，请勿转发，5分钟内有效"),
    MOBILE_EDIT("05", "您正在进行更换手机号码操作，验证码%@%。妥善保管，请勿转发，5分钟内有效");

    private String value;
    private String template;

    SmsCaptchaType(String value, String template) {
        this.value = value;
        this.template = template;
    }
}
