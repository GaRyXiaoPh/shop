package cn.kt.mall.shop.trade.vo;

import java.math.BigDecimal;
import java.util.List;

import cn.kt.mall.shop.coupon.entity.UserCouponLogEntity;
import cn.kt.mall.shop.good.entity.GoodCouponCenterEntity;
import cn.kt.mall.shop.good.entity.GoodPropertyEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TradeItemVO {

    @ApiModelProperty("订单商品id")
    private String id;
    @ApiModelProperty("主订单id")
    private String tradeId;
    @ApiModelProperty("店铺id")
    private String shopId;
    @ApiModelProperty("商品id")
    private String goodId;
    @ApiModelProperty("商品名称")
    private String goodName;
    @ApiModelProperty("购买数量")
    private int buyNum;
    @ApiModelProperty("单个购买价格")
    private BigDecimal buyPrice;
    @ApiModelProperty("商品主图")
    private String goodImg;
    @ApiModelProperty("运费")
    private BigDecimal freightFree;
    @ApiModelProperty("商品分类, well－sold:热销 boutique: 精品")
    private String goodType;

    @ApiModelProperty("商品属性")
    private List<GoodPropertyEntity> goodPropertyList;
    //支付方式
    private String payType;
    //订单商品对应获得的兑换券列表
    private List<UserCouponLogEntity> userCouponLogEntityList;
    @ApiModelProperty("商品发货状态")
    private int goodStatus;
    //商品状态（上架、下架、删除）
    private String status;
    //商品下架或删除标记
    private String  invalidFlag;
    //商品加价
    private BigDecimal raisePrice;
}
