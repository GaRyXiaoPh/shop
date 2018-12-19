package cn.kt.mall.shop.coupon.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class TBUserCouponVO implements Serializable {

    private String id;

    private BigDecimal releaseNum;
}
