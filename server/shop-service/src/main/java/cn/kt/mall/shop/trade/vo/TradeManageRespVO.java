package cn.kt.mall.shop.trade.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.kt.mall.shop.trade.entity.TradeItemEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TradeManageRespVO {

    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("店铺id")
    private String shopId;
    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("订单编号")
    private String tradeNo;
    @ApiModelProperty("购买人")
    private String buyUserId;
    @ApiModelProperty("购买人账号")
    private String userMobile;

    @ApiModelProperty(notes = "订单状态。1待发货、4部分发货、2待收货、3已完成", dataType = "string")
    private String status;
    @ApiModelProperty("总基础费用（单位：元）")
    private BigDecimal totalPrice;
    @ApiModelProperty("总运费（单位：元）")
    private BigDecimal totalFreightFree;
    @ApiModelProperty("待付款（单位：莱姆币）")
    private BigDecimal coinWait;
    @ApiModelProperty("下单时莱姆币兑换比例")
    private BigDecimal lemRate;
    @ApiModelProperty("实付款（单位：莱姆币）")
    private BigDecimal coined;
    @ApiModelProperty("收货地址")
    private String detailAddress;
    @ApiModelProperty("省")
    private Long province;
    @ApiModelProperty("市")
    private Long city;
    @ApiModelProperty("区")
    private Long county;
    @ApiModelProperty("收货人")
    private String recvName;
    @ApiModelProperty("收货人电话")
    private String recvMobile;
    @ApiModelProperty("邮政编码")
    private String zipcode;
    @ApiModelProperty("订单备注")
    private String mark;
    @ApiModelProperty("用户删除状态：0未删除，1已删除")
    private Short flag;
    @ApiModelProperty("商家删除状态：0未删除，1已删除")
    private Short shopFlag;
    private Date createTime;
    private List<TradeItemEntity> tradeItems;
    @ApiModelProperty("关系")
    private String relationship;
    @ApiModelProperty("级别")
    private String level;

    @ApiModelProperty("商品总数")
    private int totalGoodpNum;

    @ApiModelProperty("商品种类")
    private int totalType;

    @ApiModelProperty(notes = "订单内部单号")
    private String interiorNo;

    private String provinceValue;
    private String cityValue;
    private String countyValue;

}
