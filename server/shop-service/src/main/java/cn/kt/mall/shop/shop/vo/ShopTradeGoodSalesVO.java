package cn.kt.mall.shop.shop.vo;

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
public class ShopTradeGoodSalesVO implements Serializable {
    @ApiModelProperty("店铺类型")
    private String shopType;
    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("销售时间")
    private Date createTime;
    @ApiModelProperty("商品名称")
    private String skuName;
    @ApiModelProperty("商品销售数量")
    private int goodCount;
    @ApiModelProperty("商品销售金额")
    private BigDecimal price;
    @ApiModelProperty("商品id")
    private String goodId;
    @ApiModelProperty("开始时间")
    private Date startTime;
    @ApiModelProperty("结束时间")
    private Date endTime;
    @ApiModelProperty("销售金额")
    private BigDecimal totalPoint;
    @ApiModelProperty("2:零售店,3:批发店")
    private String shopLevel;

    @ApiModelProperty("店铺销售总金额")
    private BigDecimal shopTotalPoint;
}
