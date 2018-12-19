package cn.kt.mall.common.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 修改密码
 * Created by jerry on 2017/12/29.
 */
@ApiModel(description = "修改密码")
@Getter
@Setter
@NoArgsConstructor
public class PasswordVO {

    @ApiModelProperty(notes = "原密码密文：sha256hash(密码)", dataType = "string")
    private String oldPassword;

    @ApiModelProperty(notes = "新密码密文：sha256hash(密码)", dataType = "string")
    private String password;
}
