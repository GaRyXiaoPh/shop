package cn.kt.mall.common.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("修改下级用户等级信息")
@Getter
@Setter
@NoArgsConstructor
public class EditSlaveLevelVO {

    @ApiModelProperty(notes = "用户Id", dataType = "string")
    private String slaveId;

    @ApiModelProperty(notes = "用户等级(编码)：l1 - l10", dataType = "string")
    private String level;

    @ApiModelProperty(hidden = true)
    private String currentId;
}
