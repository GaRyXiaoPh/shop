package cn.kt.mall.management.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户信息")
@Data
public class UserInfoVO {
    @ApiModelProperty(notes = "用户Id", dataType = "string")
    private String id;

    @ApiModelProperty(notes = "用户名", dataType = "string")
    private String username;

    @ApiModelProperty(notes = "地区编码", dataType = "string")
    private String nationalCode;

    @ApiModelProperty(notes = "昵称", dataType = "string")
    private String nick;

    @ApiModelProperty(notes = "用户状态：0 正常，1 禁用", dataType = "string")
    private String status;

    @ApiModelProperty(notes = "用户等级编码", dataType = "string")
    private String level;

    @ApiModelProperty(notes = "用户等级名称", dataType = "string")
    private String levelName;

    @ApiModelProperty(notes = "推荐人的用户Id", dataType = "string")
    private String referrer;

    @ApiModelProperty(notes = "推荐人的用户名", dataType = "string")
    private String referrerUsername;

}
