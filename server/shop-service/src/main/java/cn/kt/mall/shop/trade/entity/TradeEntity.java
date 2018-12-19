package cn.kt.mall.shop.trade.entity;

import cn.kt.mall.shop.coupon.entity.UserReleaseCouponLogEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TradeEntity implements Serializable {
    private static final long serialVersionUID = -4160903428049805229L;

    private String id;
    private String tradeNo;
    private String interiorNo;
    private String shopId;
    private String buyUserId;
    private String status;
    private BigDecimal totalPrice;
    private BigDecimal totalFreightFree;
    private BigDecimal coinWait;
    private BigDecimal lemRate;
    private BigDecimal totalCny;
    private BigDecimal coined;
    private BigDecimal point;
    private String country;
    private Long province;
    private Long city;
    private Long county;
    private String provinceValue;
    private String cityValue;
    private String countyValue;
    private String detailAddress;
    private String recvName;
    private String recvMobile;
    private String zipcode;
    private String mark;
    private Short flag;
    private Short shopFlag;
    private Date createTime;
    private Date lastTime;
    private Short source;
    private String couponExplain;
    //支付类型
    private String payType;
    //释放记录
    private List<UserReleaseCouponLogEntity> uerReleaseCouponLogEntityList;
    private Long currentTime;
    public TradeEntity(String id, String shopId, String detailAddress, String recvName, String recvMobile,
                       String zipcode, Long province, Long city, Long county) {
        super();
        this.id = id;
        this.shopId = shopId;
        this.detailAddress = detailAddress;
        this.recvName = recvName;
        this.recvMobile = recvMobile;
        this.zipcode = zipcode;
        this.province = province;
        this.city = city;
        this.county = county;
    }

}
