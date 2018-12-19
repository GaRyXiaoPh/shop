package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel("商品详情信息实体类对象")
@Getter
@Setter
@NoArgsConstructor
public class GoodDetailVO implements Serializable {

    /**
     * 订单Id
     */
    @ApiModelProperty("订单Id")
    private String tradeId;


    //订单编号
    @ApiModelProperty( "订单编号")
    private String tradeNo;


    //店铺id',
    @ApiModelProperty("店铺id")
    private String shopId;

    //订单状态,状态(0待发货、1待收货-部分发货、2待收货、3已完成)',
    @ApiModelProperty("订单状态,状态(1:待发货 2.已发货 3.完成，4部分发货")
    private String status;

    //商品图片
    @ApiModelProperty("商品图片")
    private String goodImgUrl;

    //商品名称
    @ApiModelProperty("商品名称")
    private String goodName;

    //商品分类
    @ApiModelProperty("商品分类")
    private String goodType;

    //商品单位
    @ApiModelProperty("商品单位")
    private String goodUnit;
    //购买数量
    @ApiModelProperty("购买数量" )
    private int buyNum;
    //购买价格
    @ApiModelProperty("购买价格" )
    private BigDecimal buyPrice;
    //优惠券列表，赠送的优惠券列表
    @ApiModelProperty("优惠券列表" )
    private List<String> couponList;


    //物流单号
    @ApiModelProperty("物流单号" )
    private String logisticsNo = "";

    //物流公司名
    private String logisticsName ="";










}
