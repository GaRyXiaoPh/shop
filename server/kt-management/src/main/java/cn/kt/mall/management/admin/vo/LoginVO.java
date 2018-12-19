package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 登录信息
 * Created by jerry on 2017/12/29.
 */
@ApiModel(description = "用户登录信息")
@Getter
@Setter
@NoArgsConstructor
public class LoginVO {

    @ApiModelProperty(notes = "账号", dataType = "string")
    private String account;

    @ApiModelProperty(notes = "密码密文：sha256hash(密码)", dataType = "string")
    private String password;
}
