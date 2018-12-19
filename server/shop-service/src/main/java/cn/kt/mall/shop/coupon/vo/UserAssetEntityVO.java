package cn.kt.mall.shop.coupon.vo;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class UserAssetEntityVO implements Serializable {

        private String userId;

        private BigDecimal releaseNum;

        private String currencyType;
}
