package cn.kt.mall.shop.trade.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 物流管理查询返回参数
 * Created by gwj on 2018/06/09
 */
@Setter
@Getter
public class ShopTradeRespVO implements Serializable {

    private static final long serialVersionUID = 2319885394712570447L;

    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("下单时间")
    private Date createTime;
    @ApiModelProperty("订单编号")
    private String tradeNo;
    @ApiModelProperty(notes = "订单状态。0待发货、1待收货-部分发货、2待收货、3已完成", dataType = "string")
    private String status;
    @ApiModelProperty("店铺id")
    private String shopId;
    @ApiModelProperty("店铺编号")
    private String shopNo;
    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("店长姓名")
    private String name;
    @ApiModelProperty("true: 只能由店长发货， false:只能由管理员发货")
    private Boolean whetherLogistics;
    @ApiModelProperty("订单金额")
    private String totalPrice;
    @ApiModelProperty("收货人")
    private String recvName;
    @ApiModelProperty("省")
    private Long province;
    @ApiModelProperty("市")
    private Long city;
    @ApiModelProperty("区")
    private Long county;
    @ApiModelProperty("收货地址")
    private String detailAddress;
    @ApiModelProperty("收货人电话")
    private String recvMobile;
}
