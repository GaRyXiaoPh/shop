package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ApiModel("批量发货查询条件")
@Getter
@Setter
@NoArgsConstructor
public class PatchSendReqsVO implements Serializable {

    //订单编号字符串，根据，分割
    @ApiModelProperty("订单编号列表")
    private String tradeId;

    @ApiModelProperty("商店Id")
    private String shopId;
    //商品Id
    @ApiModelProperty("商品Id")
    private String goodId;


}
