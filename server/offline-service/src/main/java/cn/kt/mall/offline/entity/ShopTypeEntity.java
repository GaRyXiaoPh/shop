package cn.kt.mall.offline.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@ApiModel(description = "")
@Getter
@Setter
@NoArgsConstructor
public class ShopTypeEntity implements Serializable {
    private static final long serialVersionUID = -1990954020253458835L;

    @ApiModelProperty("商户类型")
    private String id;

    @ApiModelProperty("类型名称")
    private String name;

    @ApiModelProperty("图片路径")
    private String url;

    @ApiModelProperty("排序")
    private Integer sort;

}
