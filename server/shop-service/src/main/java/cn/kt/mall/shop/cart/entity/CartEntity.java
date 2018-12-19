package cn.kt.mall.shop.cart.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CartEntity implements Serializable {
    private static final long serialVersionUID = 2173266345525564943L;

    private String id;
    private String shopId;
    private String goodId;
    private int buyNum;
    private BigDecimal buyPrice;
    private String buyUserId;
    //隶属上级标记
    private String pidFlag;

    private String shopShopId;
    private BigDecimal raisePrice;
}
