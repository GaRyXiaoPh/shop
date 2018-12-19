package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@ApiModel("订单详情数据重构")
@Getter
@Setter
@NoArgsConstructor
public class LogisticsDetailVO {

    @ApiModelProperty("订单基本信息列表")
    private List<LogisticsVO> logisticsVOList;

    //private List<Good> list
    @ApiModelProperty("商品详情")
    private List<ShopTradeItemVO> shopTradeItemList;



}
