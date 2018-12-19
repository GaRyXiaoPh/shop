package cn.kt.mall.shop.shop.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ShopSalesAndTimeVO implements Serializable {
    @ApiModelProperty("销售时间")
    private Date time;
    @ApiModelProperty("销售金额")
    private BigDecimal sale;
    @ApiModelProperty("信用金金额")
    private BigDecimal pointSum;
    @ApiModelProperty("优惠券金额")
    private BigDecimal couponSum;

}
