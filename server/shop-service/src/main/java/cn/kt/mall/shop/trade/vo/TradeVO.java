package cn.kt.mall.shop.trade.vo;

import cn.kt.mall.shop.coupon.entity.UserCouponLogEntity;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.trade.entity.TradeLogEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TradeVO {

    @ApiModelProperty(notes = "交易主键ID")
    private String id;
    @ApiModelProperty(notes = "商户ID")
    private String shopId;
    @ApiModelProperty(value = "商户编号")
    private String shopNo;
    @ApiModelProperty(notes = "商户名称")
    private String shopName;
    @ApiModelProperty(notes = "总商品基础价格（不包含运费）", dataType = "BigDecimal")
    private BigDecimal totalPrice;
    @ApiModelProperty(notes = "总运费", dataType = "BigDecimal")
    private BigDecimal totalFreightFree;
    @ApiModelProperty(notes = "交易编号")
    private String tradeNo;
    @ApiModelProperty(notes = "内部订单编号,多笔订单提交时相同")
    private String interiorNo;
    @ApiModelProperty(notes = "订单状态。0;待客户支付" +
            "1;客户支付成功，商家待发货\n" +
            "2;商家发货成功，客户待收货\n" +
            "3;客户已收货\n" +
            "4;客户申请售后中\n" +
            "5:客户申请退款中\n" +
            "6:客户申请退货中\n" +
            "8:订单支付失败\n" +
            "9:订单支付超时\n" +
            "10:取消订单, 待支付待发货可以取消\n" +
            "12:已评论 订单完结（所有流程都走完，后续无流程可走）")
    private String status;

    @ApiModelProperty(notes = "需付莱姆币")
    private BigDecimal coinWait;
    @ApiModelProperty(notes = "实付莱姆币")
    private BigDecimal coined;
    @ApiModelProperty(notes = "实付积分")
    private BigDecimal point;
    @ApiModelProperty(notes = "下单时莱姆币兑换比率")
    private BigDecimal lemRate;
    @ApiModelProperty(notes = "国")
    private String country;
    @ApiModelProperty(notes = "省code")
    private Long province;
    @ApiModelProperty(notes = "市code")
    private Long city;
    @ApiModelProperty(notes = "县code")
    private Long county;
    @ApiModelProperty(notes = "省value")
    private String provinceValue;
    @ApiModelProperty(notes = "市value")
    private String cityValue;
    @ApiModelProperty(notes = "县value")
    private String countyValue;
    @ApiModelProperty(notes = "详细地址")
    private String detailAddress;
    @ApiModelProperty(notes = "接收者姓名")
    private String recvName;
    @ApiModelProperty(notes = "接收手机号码")
    private String recvMobile;
    @ApiModelProperty(notes = "邮编")
    private String zipcode;
    @ApiModelProperty(notes = "购买人id")
    private String buyUserId;
    @ApiModelProperty(notes = "购买人账号")
    private String buyUserName;
    @ApiModelProperty(notes = "0表示app来源")
    private Short source;

    @ApiModelProperty(notes = "删除标志", dataType = "short")
    private Short flag;

    @ApiModelProperty(notes = "订单商品集合", dataType = "list")
    private List<TradeItemVO> tradeGoods;

    @ApiModelProperty(notes = "备注说明")
    private String mark;
    @ApiModelProperty(notes = "创建时间")
    private Date createTime;
    @ApiModelProperty(notes = "最后时间")
    private Date lastTime;
    @ApiModelProperty(notes = "订单优惠券")
    private List<UserCouponLogEntity> couponExplain;

    @ApiModelProperty(notes = "订单记录状态", dataType = "list:0,买家创建订单成功,1:买家支付订单成功;2卖家发货成功;3买家收货成功;4买家申请售后;5买家申请退款;6买家申请退货;9支付超时;10取消订单；12订单完成；13订单删除")
    List<TradeLogEntity> tradeLogEntityList;
    @ApiModelProperty(notes = "支付类型")
    private String payType;
    @ApiModelProperty(notes = "用户等级")
    private String userLevel;
    @ApiModelProperty(notes = "店长真实姓名")
    private String shopUserTrueName;
    @ApiModelProperty(notes = "店铺信息")
    private ShopEntity shopEntity;
}
