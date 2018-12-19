package cn.kt.mall.offline.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2018/5/12.
 */
@Getter
@Setter
public class OrderEntity {
    @ApiModelProperty("订单id")
    private String  orderId;

    @ApiModelProperty("用户名")
    private String  username;

    @ApiModelProperty("订单商品")
    private String orderGood;

    @ApiModelProperty("商品总价")
    private double  totalPrice;

    @ApiModelProperty("实际支付金额")
    private double lyme;

    @ApiModelProperty("购买时间")
    private String payTime;

    @ApiModelProperty("评分")
    private Integer score;
}
