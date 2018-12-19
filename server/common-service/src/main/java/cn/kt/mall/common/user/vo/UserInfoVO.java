package cn.kt.mall.common.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoVO {
    @ApiModelProperty(notes = "用户Id", dataType = "string")
    String userId;
    @ApiModelProperty(notes = "用户账户", dataType = "string")
    String username;
    @ApiModelProperty(notes = "手机号", dataType = "string")
    String mobile;
    @ApiModelProperty(notes = "用户昵称", dataType = "string")
    String nick;
    @ApiModelProperty(notes = "头像", dataType = "string")
    String avatar;
    @ApiModelProperty(notes = "是否好友，0否1是2未知", dataType = "short")
    Short isFriend;
}