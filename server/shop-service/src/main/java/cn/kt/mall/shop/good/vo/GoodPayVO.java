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
public class GoodPayVO implements Serializable {

    private static final long serialVersionUID = -3690765967264862170L;
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("商品Id")
    private String goodId;
    @ApiModelProperty("支付方式id")
    private String goodPayId;
    @ApiModelProperty("支付类型, 和UserAssetEntity的currency一致")
    private String payType;
    @ApiModelProperty("支付价格:现金")
    private BigDecimal price;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("支付价格:popc")
    private BigDecimal popcPrice;

}
