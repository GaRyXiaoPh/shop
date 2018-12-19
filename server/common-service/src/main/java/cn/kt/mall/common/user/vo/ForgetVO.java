package cn.kt.mall.common.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 忘记密码
 * Created by jerry on 2017/12/29.
 */
@ApiModel(description = "忘记密码")
@Getter
@Setter
@NoArgsConstructor
public class ForgetVO {

    @ApiModelProperty(notes = "手机号", dataType = "string")
    private String mobile;

    @ApiModelProperty(notes = "密码密文：sha256hash(密码)", dataType = "string")
    private String password;

    @ApiModelProperty(notes = "验证码", dataType = "string")
    private String captcha;
}
