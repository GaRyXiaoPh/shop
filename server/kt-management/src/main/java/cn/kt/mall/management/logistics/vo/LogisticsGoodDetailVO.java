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

@ApiModel("查询列表商品详情")
@Getter
@Setter
@NoArgsConstructor
public class LogisticsGoodDetailVO implements Serializable {

    /**
     * 商品Id
     */
    @ApiModelProperty( "商品Id")
    private String goodId;
    /**
     * 内部编号
     */
    @ApiModelProperty( "内部编号")
    private String interiorNo;
    /**
     * 下单时间
     */
    @ApiModelProperty( "下单时间")
    private Date createTime;
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
    @ApiModelProperty("订单状态,状态(0:未发货 1.已发货")
    private Integer sendStatus;


    //商品名称
    @ApiModelProperty("商品名称")
    private String goodName;

    //购买数量
    @ApiModelProperty("购买数量" )
    private int buyNum;
    //购买价格
    @ApiModelProperty("购买价格" )
    private BigDecimal buyPrice;

    //商店名称
    @ApiModelProperty( "商店名称")
    private String shopName;
    //店长Id
    @ApiModelProperty("店长Id")
    private  String userId;

    //店长用户名
    @ApiModelProperty("店长用户名")
    private String shopUserName;

    //收货人名字
    @ApiModelProperty("收货人名字")
    private String recvName;

    //收货地址
    @ApiModelProperty("收货地址")
    private String detailAddress;

    //收货人电话
    @ApiModelProperty("收货人电话")
    private String recvMobile;

    @ApiModelProperty("商店Id(查询使用)")
    private String shopNo;
    @ApiModelProperty(" 店长电话")
    private String shopMobile;
}
