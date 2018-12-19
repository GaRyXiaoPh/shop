package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@ApiModel("商店商品物流")
@Getter
@Setter
@NoArgsConstructor
public class LogisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单编号")
    private String interiorNo;
    /**
     * 订单Id
     */
    @ApiModelProperty( "订单Id")
    private String tradeId;

    @ApiModelProperty("编号")
    private String tradeNo;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    //订单状态,状态(0:待发货 1.部分发货 2.已发货)',
    @ApiModelProperty("订单状态1:待发货 2.已发货 3.完成，4部分发货")
    private String status;

    //店铺id',
    @ApiModelProperty("商店Idg")
    private String shopId;

    //商店名称
    @ApiModelProperty( "商店名称")
    private String shopName;
    //店长Id
    @ApiModelProperty("店长Id")
    private  String userId;

    //店长用户名
    @ApiModelProperty("店长用户名")
    private String shopUserName;

    //店长的电话号码
    @ApiModelProperty("店长的电话号码")
    private String shopUserPhone;

    //购买者的用户Id
    @ApiModelProperty("购买者的用户Idg")
    private String buyUserId;

    //收货人名字
    @ApiModelProperty("收货人名字")
    private String recvName;

    //收货地址
    @ApiModelProperty("收货地址")
    private String detailAddress;

    //收货人电话
    @ApiModelProperty("收货人电话")
    private String recvMobile;

    //物流单号
    @ApiModelProperty("物流单号")
    private String logisticsNo = "";

    //物流公司名
    @ApiModelProperty("物流公司名")
    private String logisticsName ="";

    //物品名称
    @ApiModelProperty("物品名称")
    private String goodName;

    @ApiModelProperty("商店编号")
    private String shopNo;
    @ApiModelProperty("省名称")
    private String provinceValue;

    @ApiModelProperty("市名称")
    private String cityValue;

    @ApiModelProperty("区名称")
    private String countyValue;

    @ApiModelProperty
    private String   shopMobile;

    @ApiModelProperty
    private Long currentTime;

    @ApiModelProperty
    private Integer buyNum;
}
