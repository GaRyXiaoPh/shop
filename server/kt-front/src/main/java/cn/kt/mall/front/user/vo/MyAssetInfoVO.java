package cn.kt.mall.front.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 我的资产明细
 * @author gwj
 */
@Setter
@Getter
public class MyAssetInfoVO implements Serializable {

    private static final long serialVersionUID = -4022829696731439913L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("操作名称")
    private String title;

    @ApiModelProperty("操作时间")
    private String createTime;

    @ApiModelProperty("第一描述")
    private String firstOperation;

    @ApiModelProperty("第一描述显示颜色:red红色，green绿色")
    private String firstOperationColor;

    @ApiModelProperty("第二描述")
    private String secondOperation;

    @ApiModelProperty("第二描述显示颜色:red红色，green绿色")
    private String secondOperationColor;
}
