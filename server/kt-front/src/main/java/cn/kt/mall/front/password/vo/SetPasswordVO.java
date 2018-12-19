package cn.kt.mall.front.password.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 密码设置信息
 * Created by wqt on 2018/1/29.
 */
@ApiModel(description = "密码设置信息")
@Getter
@Setter
@NoArgsConstructor
public class SetPasswordVO {

    @ApiModelProperty(notes = "密码密文：sha256hash(密码)", dataType = "string")
    private String password;

    @ApiModelProperty(notes = "验证码", dataType = "string")
    private String captcha;
}
