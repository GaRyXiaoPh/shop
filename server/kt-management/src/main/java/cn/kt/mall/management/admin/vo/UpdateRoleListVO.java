package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class UpdateRoleListVO implements Serializable {

    private static final long serialVersionUID = 4002188183534932539L;

    @ApiModelProperty("角色ID")
    private Integer roleId;
    @ApiModelProperty("修改后角色权限列表")
    private Integer [] res;

}
