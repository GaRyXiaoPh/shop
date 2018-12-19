package cn.kt.mall.front.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 我的资产
 * @author gwj
 */
@Setter
@Getter
public class MyAssetVO implements Serializable {

    private static final long serialVersionUID = -3990863436207300701L;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty(value = "资产代号,如：point、popc")
    private String currency;

    @ApiModelProperty("可用现金")
    private Double availableBalance;

    @ApiModelProperty("不可用现金")
    private Double reservedBalance;

    @ApiModelProperty("总现金")
    private Double totlePoint;

    @ApiModelProperty("可用POPC")
    private Double availablePOPC;

    @ApiModelProperty("不可用POPC")
    private Double reservedPOPC;

    @ApiModelProperty("总POPC")
    private Double totlePOPC;

    @ApiModelProperty("popc和cny的比率")
    private String popcForCny;


}
