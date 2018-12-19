package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("操作权限")
public class AdminOperationVO implements Serializable {
    private static final long serialVersionUID = -1661830493897252605L;

    @ApiModelProperty("操作ID")
    private Integer opId;
    @ApiModelProperty("操作名称")
    private String name;
    @ApiModelProperty("操作英文名称")
    private String englishName;
    @ApiModelProperty("是否拥有该操作权限")
    private Boolean selected = false;
}
