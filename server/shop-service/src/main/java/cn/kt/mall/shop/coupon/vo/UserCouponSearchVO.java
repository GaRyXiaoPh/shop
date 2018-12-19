package cn.kt.mall.shop.coupon.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
public class UserCouponSearchVO implements Serializable {

    private Date createTime;

    private String couponId;

    private Long lastReleaseTime;

    private int start;

    private int end;
}
