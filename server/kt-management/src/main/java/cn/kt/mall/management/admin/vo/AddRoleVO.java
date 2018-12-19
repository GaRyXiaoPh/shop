package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ApiModel("添加权限资源列表")
@Data
public class AddRoleVO implements Serializable {
    private static final long serialVersionUID = -1661830493897252605L;
    @ApiModelProperty("角色名称")
    private String name;
    @ApiModelProperty("资源ID")
    private Integer resId;
    @ApiModelProperty("子集资源列表")
    private List<Integer> childResIds;
    @ApiModelProperty("子集操作列表")
    private List<AddOperationsVo> menuOperations;

}
