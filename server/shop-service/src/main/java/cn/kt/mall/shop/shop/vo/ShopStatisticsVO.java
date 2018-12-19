package cn.kt.mall.shop.shop.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ShopStatisticsVO implements Serializable {

    private String shopName;
    private String mobile;
    private String name;
    private BigDecimal cnySum;
    private BigDecimal popcSum;
    private String personCount;
    private String userId;
    //店铺id
    private String id;
    //店铺编号
    private String shopNo;
    //店铺等级
    private String shopLevel;
    //店铺营业总额
    private BigDecimal totalPrice;
    //团队增长
    private BigDecimal teamCount;

}
