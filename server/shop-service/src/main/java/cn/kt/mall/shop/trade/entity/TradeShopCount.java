package cn.kt.mall.shop.trade.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class TradeShopCount implements Serializable {

    private static final long serialVersionUID = -3755214962339682533L;
    private String id;
    private String shop_count;

}
