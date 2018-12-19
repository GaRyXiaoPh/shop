package cn.kt.mall.shop.cart.vo;

import cn.kt.mall.shop.good.entity.GoodCouponCenterEntity;
import cn.kt.mall.shop.good.vo.GoodPayReqVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartGoodsItem {

    @ApiModelProperty("购物车id")
    private String cartId;
    @ApiModelProperty("商品名称")
    private String goodName;
    @ApiModelProperty("商品id")
    private String id;
    @ApiModelProperty("购买数量")
    private int buyNum;
    @ApiModelProperty("商品价格")
    private BigDecimal goodPrice;
    @ApiModelProperty("运费")
    private BigDecimal freightFree;
    @ApiModelProperty("商品主图")
    private String goodImg;
    @ApiModelProperty("商店id，方便前端进行全选操作")
    private String shopId;
    @ApiModelProperty("商品一级分类, well-sold: 热销 boutique： 精品")
    private String firstGoodType;
    //商品关联优惠券链表
    private List<GoodCouponCenterEntity> goodCouponCenterEntityList;
    //隶属上级标记
    private String pidFlag;
    //支付方式
    private String payType;
    //支付方式价格
    private String price;
    //商品库存
    private int goodStock;
    //商品下架或删除标记
    private String  invalidFlag;
    //商品支付方式列表
    private List<GoodPayReqVO> goodPayEntityList;

    private String shopShopId;
    //商品加价
    private BigDecimal raisePrice;

}
