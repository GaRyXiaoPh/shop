package cn.kt.mall.shop.shop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel("商店商品物流")
@Getter
@Setter
@NoArgsConstructor
public class ShopTransportVO implements Serializable {
 private static final long serialVersionUID = -8132139719637031975L;

   // 物流订单表主键
   @ApiModelProperty("物流订单表主键")
    private String id;

    //商店Id
    @ApiModelProperty("商店Id")
    private String shopId;

    //商品Id
    @ApiModelProperty("商品Id")
    private String goodId;
    //订单编号
    @ApiModelProperty("订单编号")
    private String tradeNo;

    //待物流号的总运费
    @ApiModelProperty("待物流号的总运费")
    private BigDecimal totalFreightFree;

    //操作人Id
    @ApiModelProperty("操作人Id")
    private String userId;

    //操作时间
    @ApiModelProperty("操作时间")
    private Date operatorTime = new Date();

    //物流号编号
    @ApiModelProperty( "物流号编号")
    private  String transportNo;

    @ApiModelProperty("物流公司名")
   private String logisticsName;
}
