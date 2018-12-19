package cn.kt.mall.common.sms.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/5.
 */
@Getter
@Setter
public class SmsCaptchaVO {
    @ApiModelProperty(notes = "手机号", dataType = "string")
    private String mobile;

    @ApiModelProperty(notes = "验证码类型：REGISTER 注册验证码, PASSWORD_FORGET 忘记密码验证码, SECONDARY_PASSWORD_FORGET 忘记二级密码, GESTURE_PASSWORD_FORGET 忘记手势密码", dataType = "string")
    private String type;

}
