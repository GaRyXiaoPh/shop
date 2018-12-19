package cn.kt.mall.offline.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */
@Getter
@Setter
public class OrderDetailEntity  implements Serializable{

    private static final long serialVersionUID = 1837446470136836124L;

    @ApiModelProperty("订单编号")
    private String orderId;

    @ApiModelProperty("商品名称")
    private String userId;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("购买时间")
    private String payTime;

    @ApiModelProperty("商品总价")
    private double totalPrice;

    @ApiModelProperty("支付莱姆币")
    private double lyme;

    @ApiModelProperty("订单商品信息")
    private List<OrderDetailResEntity> orderGoodInfo;

    @ApiModelProperty("订单评价")
    private CommentEntity orderComment;
}
