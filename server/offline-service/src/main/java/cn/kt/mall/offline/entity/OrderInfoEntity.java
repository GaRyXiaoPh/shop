package cn.kt.mall.offline.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2018/5/14.
 */
@Getter
@Setter
public class OrderInfoEntity {

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("购买者id")
    private String userId;

    @ApiModelProperty("购买者名称")
    private String username;

    @ApiModelProperty("店铺id")
    private String shopId;

    @ApiModelProperty("商家id")
    private String merUserId;

    @ApiModelProperty("总价")
    private Double totalPrice;

    @ApiModelProperty("莱姆比例")
    private Double rate;

    @ApiModelProperty("支付莱姆币")
    private Double lyme;

    @ApiModelProperty("实际支付莱姆币")
    private Double actualLyme;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("下单时间")
    private String  createTime;

    @ApiModelProperty("购买时间")
    private String  payTime;
}
