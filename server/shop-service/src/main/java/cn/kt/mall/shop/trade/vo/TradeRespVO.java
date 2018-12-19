package cn.kt.mall.shop.trade.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class TradeRespVO {

    @ApiModelProperty("内部订单编号")
    private String interiorNo;
    @ApiModelProperty("莱姆币兑换比率")
    private BigDecimal lemRate;
    @ApiModelProperty("订单总金额（元）（包括运费）")
    private BigDecimal baseTotal;
    @ApiModelProperty("订单总金额（莱姆币）（包括运费）")
    private BigDecimal lemTotal;
}
