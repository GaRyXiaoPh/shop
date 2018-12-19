package cn.kt.mall.shop.cart.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartVO {

    @ApiModelProperty("商户id")
    private String id;
    @ApiModelProperty("商户编号")
    private String shopNo;
    @ApiModelProperty("商户名称")
    private String shopName;
    @ApiModelProperty("商户头像")
    private String avatar;
    @ApiModelProperty("满减金额")
    private BigDecimal feightRate;

    @ApiModelProperty("该店铺商品集合")
    List<CartGoodsItem> goodsData;
}
