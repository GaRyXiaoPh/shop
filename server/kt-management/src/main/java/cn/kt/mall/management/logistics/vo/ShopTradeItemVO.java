package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ApiModel("商店商品详情")
@Getter
@Setter
@NoArgsConstructor
public class ShopTradeItemVO implements Serializable {
    //订单详情表Id
    private String id;

        //订单表Id主键
        @ApiModelProperty("订单表Id主键")
        private String tradeId;
        @ApiModelProperty("订单编号")
        private String tradeNo;
        //店铺id
        @ApiModelProperty("店铺id")
        private String shopId;

        //商品ID
        @ApiModelProperty("商品ID")
        private String goodId;
        //商品名称
        @ApiModelProperty("商品名称 ")
        private String goodName;

        //商品主图
        @ApiModelProperty("商品主图")
         private String goodImg;
         //购买数量
         @ApiModelProperty("购买数量")
         private Integer buyNum;

        @ApiModelProperty("单位")
         private String unit;
         //购买价格
         @ApiModelProperty("购买价格")
        private BigDecimal buyPrice;
        //购买者ID
        @ApiModelProperty("购买者ID")
        private String buyUserId;

         //运费
         @ApiModelProperty("运费")
        private BigDecimal freightFree;

        //0:该商品未发货 1.该商品已经发货
        @ApiModelProperty("发货状态 0:该商品未发货 1.该商品已经发货")
        private Integer  goodStatus;
        @ApiModelProperty("商品类型")
        private String goodType;

        @ApiModelProperty("物流号")
        private String logisticsNo;

        @ApiModelProperty ("物流名称")
        private String logisticsName;

        @ApiModelProperty("优惠券信息")
        private List<String> coupNameList;


}
