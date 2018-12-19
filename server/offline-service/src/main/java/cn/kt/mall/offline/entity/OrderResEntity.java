package cn.kt.mall.offline.entity;

import cn.kt.mall.offline.vo.OrderVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by Administrator on 2018/5/14.
 */
@Getter
@Setter
public class OrderResEntity {

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("购买者id")
    private String userId;

    @ApiModelProperty("店铺id")
    private String shopId;

    @ApiModelProperty("商品价格")
    private Double totalPrice;

    @ApiModelProperty("商品价格")
    private Double rate;

    @ApiModelProperty("商品价格")
    private Double lyme;

    @ApiModelProperty("商品价格")
    private Double actualLyme;

    @ApiModelProperty("标识(0:我要买单订单  1:立即购买订单)")
    private Integer mark;

    // 新建订单实体
    public OrderResEntity(OrderVO orderVO,String orderId,String userId,Integer mark){
        this.orderId = orderId;
        this.userId = userId;
        this.shopId = orderVO.getShopId();
        this.totalPrice = orderVO.getTotalPrice();
        this.rate = orderVO.getRate();
        this.lyme = orderVO.getLyme();
        this.actualLyme = orderVO.getActualLyme();
        this.mark = mark;
    }

}
