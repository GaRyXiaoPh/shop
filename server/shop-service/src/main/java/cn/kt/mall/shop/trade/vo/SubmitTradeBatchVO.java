package cn.kt.mall.shop.trade.vo;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class SubmitTradeBatchVO {
    @ApiModelProperty("购物车id集合，如果是从购物车开始支付则需要传，方便下单成功后删除购物车信息")
    private String[] cartIds;

    @ApiModelProperty("下单主体集合")
    private List<SubmitTradeVO> tradeList;

}
