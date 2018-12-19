package cn.kt.mall.shop.coupon.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CouponsVO implements Serializable {

    private static final long serialVersionUID = 2308907331146819052L;
    private String id;
    private String couponName;
    private long couponNum;
    private String unit;
    private Date createTime;
    private BigDecimal ratio;
    private int sendDays;
    private Integer couponType;
    private String couponEnglishName;
    private Integer isSend;
}

