package cn.kt.mall.management.admin.vo;

import cn.kt.mall.management.admin.entity.AdminEntity;
import cn.kt.mall.common.admin.model.AdminInfo;
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
public class LoginInfoVO extends AdminInfo {

    @ApiModelProperty(notes = "token", dataType = "string")
    private String accessToken;

    public LoginInfoVO(AdminEntity entity, String accessToken) {
        super();
        BeanUtils.copyProperties(entity, this);
        this.accessToken = accessToken;
    }
}
