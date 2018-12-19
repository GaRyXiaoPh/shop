package cn.kt.mall.front.external.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@ApiModel(description = "国家地区编码")
@Getter
@Setter
@NoArgsConstructor
public class NationalCodeEntity implements Serializable {
    private static final long serialVersionUID = -1990954020253458835L;

    @ApiModelProperty("ID")
    private int id;

    @ApiModelProperty("国际名称")
    private String nationalName;

    @ApiModelProperty("中文名称")
    private String chineseName;

    @ApiModelProperty("国际简称")
    private String abbre;

    @ApiModelProperty("国际代码")
    private String code;

    @ApiModelProperty("洲际")
    private String land;

    @ApiModelProperty("中文名称")
    private double price;
}
