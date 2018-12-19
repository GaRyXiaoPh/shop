package cn.kt.mall.shop.trade.entity;

import cn.kt.mall.shop.good.entity.GoodCouponCenterEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

@Setter
@Getter
@NoArgsConstructor
public class TradeItemEntity implements Serializable {
    private static final long serialVersionUID = -2093082766895385040L;

    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("交易主表id")
    private String tradeId;
    @ApiModelProperty("店铺id")
    private String shopId;
    @ApiModelProperty("商品id")
    private String goodId;
    @ApiModelProperty("商品名称")
    private String goodName;
    @ApiModelProperty("商品图片")
    private String goodImg;
    @ApiModelProperty("购买数量")
    private int buyNum;
    @ApiModelProperty("购买价格")
    private BigDecimal buyPrice;
    @ApiModelProperty("单个运费")
    private BigDecimal freightFree;
    @ApiModelProperty("购买人")
    private String buyUserId;
    private Date createTime;
    private Date lastTime;
    //支付方式
    private String payType;
    private String goodType;
    private int goodStatus;
    //商品状态（上架、下架、删除）
    private String status;
    //商品下架或删除标记
    private String  invalidFlag;
    //商品加价
    private BigDecimal raisePrice;

}
