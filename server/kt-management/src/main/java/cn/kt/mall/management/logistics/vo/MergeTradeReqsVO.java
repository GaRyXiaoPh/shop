package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ApiModel("部分合单请求")
@Getter
@Setter
@NoArgsConstructor
public class MergeTradeReqsVO implements Serializable {

    @ApiModelProperty("商店Id")
    private String shopId;
    @ApiModelProperty("订单编号")
    private String tradeNo;
    @ApiModelProperty("订单Id")
    private String tradeId;

    @ApiModelProperty("商品Id")
    private String goodId;


}
