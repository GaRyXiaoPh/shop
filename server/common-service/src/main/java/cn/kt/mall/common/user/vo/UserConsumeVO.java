package cn.kt.mall.common.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 各人消费总计
 * Created by dyd on 2018/6/29.
 */
@ApiModel(description = "各人消费总计")
@Getter
@Setter
@NoArgsConstructor
public class UserConsumeVO {


    @ApiModelProperty(notes = "id", dataType = "string")
    private String id;

    @ApiModelProperty(notes = "用户id", dataType = "string")
    private String userId;

    @ApiModelProperty(notes = "消费总计", dataType = "bigDecimal")
    private BigDecimal consume;

    @ApiModelProperty(notes = "用户等级", dataType = "int")
    private int userLevel;

    @ApiModelProperty(notes = "用户升级之前等级", dataType = "int")
    private int userLevelBefore;
}
