package cn.kt.mall.shop.coupon.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class UserAssetBaseSearchVO implements Serializable {

    private String assetType;

    private int start;

    private int end;

    private Long lastReleaseTime;
}
