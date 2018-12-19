package cn.kt.mall.shop.coupon.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class CouponsEntity implements Serializable{

    private static final long serialVersionUID = -8952879740957157134L;
    private String id;
    private String couponName;
    private long couponNum;
    private String unit;
    private Date createTime;
    //比例
    private BigDecimal ratio;

    private int sendDays;

}
