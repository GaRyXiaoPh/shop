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
public class ShopStatisticsReqVO implements Serializable {
    private static final long serialVersionUID = -2792359640555280406L;
    @ApiModelProperty("开始时间")
    private String beginTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("店铺类型--2:零售店  3:批发店")
    private String shopType;
    @ApiModelProperty("店铺ID")
    private String shopNo;
    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("用户名称")
    private String userName;

    public ShopStatisticsReqVO(String beginTime, String endTime, String shopType,
                                   String shopNo, String shopName, String userName) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.shopType = shopType;
        this.shopNo = shopNo;
        this.shopName = shopName;
        this.userName = userName;
    }

    public ShopStatisticsReqVO() {
        super();
    }
}
