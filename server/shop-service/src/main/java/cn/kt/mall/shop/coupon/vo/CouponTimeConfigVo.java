package cn.kt.mall.shop.coupon.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
@Setter
@Getter
@NoArgsConstructor
public class CouponTimeConfigVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    //不发送优惠券时间
    private Date notSendTime;
}
