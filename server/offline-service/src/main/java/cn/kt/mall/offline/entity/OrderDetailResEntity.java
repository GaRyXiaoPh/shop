package cn.kt.mall.offline.entity;

import cn.kt.mall.offline.vo.OrderDetailVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2018/5/14.
 */
@Getter
@Setter
public class OrderDetailResEntity {

    @ApiModelProperty("商品id")
    private String orderId;

    @ApiModelProperty("商品名称")
    private String userId;

    @ApiModelProperty("店铺id")
    private String shopId;

    @ApiModelProperty("商品id")
    private String goodNo;

    @ApiModelProperty("商品名称")
    private String goodName;

    @ApiModelProperty("商品主图")
    private String goodMainPic;

    @ApiModelProperty("商品价格")
    private double price;

    @ApiModelProperty("商品数量")
    private Integer num;

    @ApiModelProperty("小计")
    private double subtotal;

    // 新建订单实体
    public OrderDetailResEntity(OrderDetailVO orderDetailVO, String orderId, String userId,String shopId){
        this.orderId = orderId;
        this.userId = userId;
        this.shopId = shopId;
        this.goodNo = orderDetailVO.getGoodNo();
        this.goodName = orderDetailVO.getGoodName();
        this.goodMainPic = orderDetailVO.getGoodMainPic();
        this.price = orderDetailVO.getPrice();
        this.num = orderDetailVO.getNum();
    }

    // 新建订单实体
    public OrderDetailResEntity(String goodNo,String goodName,String goodMainPic,double price,int num,double subtotal){
        this.goodNo = goodNo;
        this.goodName = goodName;
        this.goodMainPic = goodMainPic;
        this.price =price;
        this.num = num;
        this.subtotal = subtotal;
    }

}
