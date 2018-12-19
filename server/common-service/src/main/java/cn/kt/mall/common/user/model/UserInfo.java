package cn.kt.mall.common.user.model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 用户信息
 * Created by jerry on 2017/12/28.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserInfo {
    @ApiModelProperty(notes = "用户Id", dataType = "string")
    private String id;

    @ApiModelProperty(notes = "用户名", dataType = "string")
    private String username;

    @ApiModelProperty(notes = "地区编码", dataType = "string")
    private String nationalCode;

    @ApiModelProperty(notes = "昵称", dataType = "string")
    private String nick;

    @ApiModelProperty(notes = "推荐人", dataType = "string")
    private String referrer;

    @ApiModelProperty(notes = "用户状态：0 正常，1 禁用", dataType = "string")
    private String status;

    @ApiModelProperty(notes = "头像", dataType = "string")
    private String avatar;

    @ApiModelProperty(notes = "推荐人昵称", dataType = "string")
    private String referrerNick;

    @ApiModelProperty(notes = "会员信用金", dataType = "BigDecimal")
    private BigDecimal point;

    @ApiModelProperty(notes = "会员等级", dataType = "int")
    private int userlevel;

    @ApiModelProperty(notes = "实名认证状态", dataType = "String")
    private String  certificationStatus;

    //实名认证类别
    private String  certificateType;

    //站点编号
    private String standNo;
    //手机号
    private String mobile;
    //信用分
    private BigDecimal  xin;
    //上级id
    private String  pid;
    /** 用户等级 */
    private String level;
}
