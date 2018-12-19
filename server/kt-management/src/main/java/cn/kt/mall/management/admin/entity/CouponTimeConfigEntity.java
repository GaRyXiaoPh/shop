package cn.kt.mall.management.admin.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class CouponTimeConfigEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //优惠券Id
    private String id;

    //不发送优惠券时间
    private Date notSendTime;


}
