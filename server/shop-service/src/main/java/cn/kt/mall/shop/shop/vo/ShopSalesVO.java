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
public class ShopSalesVO implements Serializable {
    private String id;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("购买人ID")
    private String buyUserId;
    @ApiModelProperty("购买人")
    private String buyUserName;
    @ApiModelProperty("购买人电话")
    private String moblie;
    @ApiModelProperty("订单金额")
    private BigDecimal baseTotal;
    @ApiModelProperty("关系")
    private String relationship;
    @ApiModelProperty("单个店铺订单总金额")
    private BigDecimal totalPrice;

}
