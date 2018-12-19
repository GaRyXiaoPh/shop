package cn.kt.mall.common.user.vo;

import cn.kt.mall.common.util.PasswordUtil;
import cn.kt.mall.common.user.entity.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 注册信息VO
 * Created by jerry on 2017/12/29.
 */
@ApiModel(description = "用户注册信息")
@Getter
@Setter
@NoArgsConstructor
public class RegisterVO {

    @ApiModelProperty(notes = "用户名", dataType = "string")
    private String username;

    @ApiModelProperty(notes = "国家地区编码", dataType = "string")
    private String nationalCode;

    @ApiModelProperty(notes = "手机号", dataType = "string")
    private String mobile;

    @ApiModelProperty(notes = "密码密文：sha256hash(密码)", dataType = "string")
    private String password;

    @ApiModelProperty(notes = "昵称", dataType = "string")
    private String nick;

    @ApiModelProperty(notes = "推荐人手机号", dataType = "string")
    private String referrer;

    @ApiModelProperty(notes = "隶属站长/主任ID", dataType = "string")
    private String pid;

    @ApiModelProperty(notes = "短信验证码", dataType = "string")
    private String captcha;
    //站点编号
    private String standNo;
    public UserEntity getUserEntity() {
        String[] pwd = PasswordUtil.getEncryptPassword(password);
        return new UserEntity(username, nationalCode, mobile, pwd[0], pwd[1], nick == null ? username : nick, "", referrer,pid,standNo);
    }
}
