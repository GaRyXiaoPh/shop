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
public class ShopTradeGoodSalesReqVO implements Serializable {

    private static final long serialVersionUID = -9048528024968149363L;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("店铺类型--2:零售店  3:批发店")
    private String shopType;
    @ApiModelProperty("商品名称")
    private String skuName;
    @ApiModelProperty("店铺名称")
    private String shopName;

    public ShopTradeGoodSalesReqVO(String startTime, String endTime, String shopType,
                                   String skuName, String shopName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.shopType = shopType;
        this.skuName = skuName;
        this.shopName = shopName;
    }

    public ShopTradeGoodSalesReqVO() {
        super();
    }
}
