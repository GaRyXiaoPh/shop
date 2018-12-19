package cn.kt.mall.shop.good.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class GoodPayReqVO implements Serializable {

    private static final long serialVersionUID = -8090725477888534111L;
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("商品Id")
    private String goodId;
    @ApiModelProperty("支付类型Id")
    private String goodPayId;
    @ApiModelProperty("支付类型")
    private String payType;
    @ApiModelProperty("支付价格")
    private BigDecimal price;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("支付名称")
    private String payName;
    @ApiModelProperty("余额比例值(%)")
    private BigDecimal balanceRatio;
    @ApiModelProperty("其它比例值(%)")
    private BigDecimal otherRatio;
}
