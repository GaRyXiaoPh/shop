package cn.kt.mall.offline.vo;

import cn.kt.mall.offline.entity.OrderDetailResEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/17.
 */
@Getter
@Setter
public class CircleOrderInfo  implements Serializable{

    private static final long serialVersionUID = -1111709372690380913L;

    @ApiModelProperty("商品总价")
    private double totalPrice;

    @ApiModelProperty("订单总价")
    private double orderPrice;

    @ApiModelProperty("需付莱姆")
    private double lem;

    @ApiModelProperty("实付莱姆")
    private double actualLem;

    @ApiModelProperty("订单号")
    private String orderId;

    @ApiModelProperty("下单时间")
    private String createTime;

    @ApiModelProperty("支付时间")
    private String payTime;

    @ApiModelProperty("订单商品信息")
    private List<OrderDetailResEntity> goodsList;
}
