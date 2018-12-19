package cn.kt.mall.shop.cart.vo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BuyGoodVO {
    private String shopId;
    private String goodId;
    private String goodName;
    private int buyNum;
}
