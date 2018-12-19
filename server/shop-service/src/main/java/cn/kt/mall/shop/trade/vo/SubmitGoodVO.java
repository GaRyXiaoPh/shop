package cn.kt.mall.shop.trade.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SubmitGoodVO {

    @ApiModelProperty("商品id")
    private String goodId;
    @ApiModelProperty("购买数量")
    private int buyNum;
    @ApiModelProperty("商品支付方式id")
    private String goodPayId;
    @ApiModelProperty("商品种类")
    private String goodType;
    @ApiModelProperty("下单优惠券信息集合")
    private List<SubmitCouponVO> couponsList;
}
