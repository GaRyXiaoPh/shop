package cn.kt.mall.offline.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Administrator on 2018/5/14.
 */
@Setter
@Getter
public class OrderVO {

    @ApiModelProperty("店铺id")
    private String shopId;

    @ApiModelProperty("商品总价")
    private Double totalPrice;

    @ApiModelProperty("莱姆比例")
    private Double rate;

    @ApiModelProperty("莱姆币")
    private Double lyme;

    @ApiModelProperty("实际支付的莱姆币")
    private Double actualLyme;

    @ApiModelProperty("商品列表")
    private List<OrderDetailVO> goodsDetail;

    @ApiModelProperty("商品列表(0:我要买单  1:立即购买)")
    private Integer flag;
}
