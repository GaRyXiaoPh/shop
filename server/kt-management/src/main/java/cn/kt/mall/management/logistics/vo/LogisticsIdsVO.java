package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单商品excel导出")
public class LogisticsIdsVO {

    @ApiModelProperty("内部订单Id")
    private String interiorNo;
    @ApiModelProperty("订单Id")
    private String tradeId;
    @ApiModelProperty("商品ID")
    private String goodId;
}
