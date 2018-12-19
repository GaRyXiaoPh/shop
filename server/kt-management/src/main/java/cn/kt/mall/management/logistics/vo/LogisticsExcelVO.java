package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@ApiModel("合并后的订单商品详情")
@Getter
@Setter
@NoArgsConstructor
public class LogisticsExcelVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    @ApiModelProperty("序号")
    private Integer index;
    @ApiModelProperty("下单时间")
    private Date createTime;
    //订单id
    @ApiModelProperty("订单id")
    private String tradeId;
    //订单编号
    @ApiModelProperty("订单编号")
    private String tradeNo;

    //店铺id',
    @ApiModelProperty("店铺id")
    private String shopId;
    //店铺id',
    @ApiModelProperty("店铺编号")
    private String shopNo;
    //商品ID
    @ApiModelProperty("商品ID")
    private String goodId;

    //订单商品
    @ApiModelProperty("订单商品")
    private String goodName;

    //商店名称
    @ApiModelProperty("商店名称")
    private String shopName;

    //商品数量
    @ApiModelProperty("商品数量")
    private Integer buyNum;

    //收货人名字
    @ApiModelProperty("收货人名字")
    private String recvName;

    //收货地址
    @ApiModelProperty("收货地址")
    private String detailAddress;

    //收货人电话
    @ApiModelProperty("收货人电话")
    private String recvMobile;

    @ApiModelProperty("省名称")
    private String provinceValue;

    @ApiModelProperty("市名称")
    private String cityValue;

    @ApiModelProperty("区名称")
    private String countyValue;

    @ApiModelProperty("物流名称")
    private String transportCompany;

    @ApiModelProperty("物流单号")
    private String transportNo;

}
