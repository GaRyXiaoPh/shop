package cn.kt.mall.web.shop.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class TradeGoodLogisticsVo implements Serializable {

    @ApiModelProperty(name = "订单ID")
    private String tradeId;
    @ApiModelProperty(name = "商品ID集合")
    private List<String> goodId;
    @ApiModelProperty(name = "物流名称")
    private String logistics;
    @ApiModelProperty(name = "物流单号")
    private String ogisticsNumber;
}
