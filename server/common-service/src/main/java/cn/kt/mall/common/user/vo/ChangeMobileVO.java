package cn.kt.mall.common.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("修改手机号信息")
@Getter
@Setter
@NoArgsConstructor
public class ChangeMobileVO {

    @ApiModelProperty(notes = "国家地区编码", dataType = "string")
    private String nationalCode;

    @ApiModelProperty(notes = "手机号码", dataType = "string")
    private String mobile;

    @ApiModelProperty(notes = "验证码", dataType = "string")
    private String captcha;
}
