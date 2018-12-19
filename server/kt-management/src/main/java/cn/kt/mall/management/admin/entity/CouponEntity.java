package cn.kt.mall.management.admin.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class
CouponEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    private String id;
    private String couponName;
    private long couponNum;
    private String unit;
    private Date createTime;
    private BigDecimal ratio;
    private Integer sendDays;
    private Integer couponType;
    private String couponEnglishName;
    private Integer isSend;
    //0.不可与第三方对接 1.可以与第三方对接 2.其他
    private Integer isDocking;
}
