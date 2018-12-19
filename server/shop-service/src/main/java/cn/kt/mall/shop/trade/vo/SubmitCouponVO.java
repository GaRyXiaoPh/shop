package cn.kt.mall.shop.trade.vo;

import cn.kt.mall.shop.coupon.vo.CouponsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SubmitCouponVO {

    @ApiModelProperty("下单获取优惠券名称")
    private String couponName;
    @ApiModelProperty("下单获取优惠券数量")
    private String couponNum;
    @ApiModelProperty("下单获取优惠券Id")
    private String couponId;
    @ApiModelProperty("下单获取优惠券种类")
    private String speciesType;
}
