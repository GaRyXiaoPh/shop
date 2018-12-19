package cn.kt.mall.common.admin.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 * Created by jerry on 2017/12/28.
 */
@Getter
@Setter
@NoArgsConstructor
public class AdminInfo implements Serializable {
    @ApiModelProperty(notes = "用户Id", dataType = "string")
    private String id;

    @ApiModelProperty(notes = "用户名", dataType = "string")
    private String username;
    @ApiModelProperty(notes = "职位名称")
    private String position;

    @ApiModelProperty(notes = "昵称", dataType = "string")
    private String nick;
    @ApiModelProperty(notes = "角色名称", dataType = "string")
    private String roleName;

    private Integer roleId;
    //这个用户所有的权限列表
    private String resName;

    @ApiModelProperty(notes = "用户状态：0 正常，1 禁用", dataType = "string")
    private String status;

    /** 创建时间 or 注册时间 */
    private Date createTime;
}
