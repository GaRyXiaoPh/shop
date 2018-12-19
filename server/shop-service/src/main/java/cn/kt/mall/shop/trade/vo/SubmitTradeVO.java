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
public class SubmitTradeVO {
    @ApiModelProperty(name="商户ID", dataType = "string")
    private String shopId;
    @ApiModelProperty(name = "用户地址ID", dataType = "string")
    private String addressId;
    @ApiModelProperty(notes="备注说明", dataType = "string")
    private String mark;
    @ApiModelProperty("下单商品信息集合")
    private List<SubmitGoodVO> goodList;
}
