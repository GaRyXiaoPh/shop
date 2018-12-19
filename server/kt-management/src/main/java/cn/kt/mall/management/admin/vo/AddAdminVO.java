package cn.kt.mall.management.admin.vo;

import cn.kt.mall.management.admin.entity.AdminEntity;
import cn.kt.mall.common.util.PasswordUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 注册信息VO
 * Created by jerry on 2017/12/29.
 */
@ApiModel(description = "管理员新增信息")
@Getter
@Setter
@NoArgsConstructor
public class AddAdminVO {

    @ApiModelProperty(notes = "账号，登录使用", dataType = "string")
    private String account;

    @ApiModelProperty(notes = "职位名称", dataType = "string")
    private String position;

    @ApiModelProperty(notes = "角色id", dataType = "Integer")
    private Integer roleId;

    @ApiModelProperty(notes = "明文密码", dataType = "string")
    private String password;



    public AdminEntity getUserEntity(String userId,String id) {
        String[] pwd = PasswordUtil.getEncryptPassword(password);
        AdminEntity entity = new AdminEntity(account,account, pwd[0], pwd[1],"",userId,id);
        entity.setPosition(position);
        return entity;
    }
}
