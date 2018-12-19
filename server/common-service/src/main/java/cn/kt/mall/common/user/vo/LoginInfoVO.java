package cn.kt.mall.common.user.vo;

import cn.kt.mall.common.user.model.UserInfo;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.user.model.UserLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * 用户登录信息VO
 * Created by jerry on 2017/12/29.
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginInfoVO extends UserInfo {

    @ApiModelProperty(notes = "昵称", dataType = "string")
    private String nick;

    @ApiModelProperty(notes = "用户名", dataType = "string")
    private String username;

    @ApiModelProperty(notes = "国家地区编码", dataType = "string")
    private String nationalCode;

    @ApiModelProperty(notes = "手机号", dataType = "string")
    private String mobile;

    @ApiModelProperty(notes = "用户等级", dataType = "string")
    private String level;

    @ApiModelProperty(notes = "用户等级名称", dataType = "string")
    private String levelName;

    @ApiModelProperty(notes = "是否设置交易密码：0 否， 1 是", dataType = "string")
    private String transactionPassword;

    @ApiModelProperty(notes = "token", dataType = "string")
    private String accessToken;

    public LoginInfoVO(UserEntity entity, String accessToken) {
        super();
        BeanUtils.copyProperties(entity, this);
        this.accessToken = accessToken;
        this.nick = entity.getNick() == null ? entity.getUsername() : entity.getNick();
        this.nationalCode = this.nationalCode == null ? "886" : this.nationalCode;
        UserLevel userLevel = UserLevel.getByCode(entity.getLevel());
        this.levelName = (userLevel == null ? UserLevel.DEFAULT : userLevel).getName();
    }
}
