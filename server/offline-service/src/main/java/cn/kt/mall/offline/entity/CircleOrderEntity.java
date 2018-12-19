package cn.kt.mall.offline.entity;

import cn.kt.mall.offline.vo.CircleOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/17.
 */
@Getter
@Setter
public class CircleOrderEntity implements Serializable{

    private static final long serialVersionUID = -138822992691712710L;

    @ApiModelProperty("总价")
    private double totalPrice;

    @ApiModelProperty("店铺名称")
    private String company;

    @ApiModelProperty("店铺图片")
    private String companyImg;

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("店铺id")
    private String shopId;

    @ApiModelProperty("是否评论")
    private boolean flag;

    @ApiModelProperty("订单信息")
    private CircleOrderInfo orderInfo;
}
