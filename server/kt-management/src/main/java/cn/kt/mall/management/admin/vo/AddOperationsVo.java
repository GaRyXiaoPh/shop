package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class AddOperationsVo {
    @ApiModelProperty("资源ID")
    private Integer resId;
    @ApiModelProperty("操作子集")
    private List<Integer> operations;
}
