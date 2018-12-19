package cn.kt.mall.shop.coupon.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Setter
@Getter
public class BasePointVO implements Serializable {

    private String v_userId;
    private String v_couponId;
    private String v_assetType;

}
