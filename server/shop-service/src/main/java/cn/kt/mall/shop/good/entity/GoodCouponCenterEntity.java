package cn.kt.mall.shop.good.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class GoodCouponCenterEntity implements Serializable {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("商品ID")
    private String goodId;
    @ApiModelProperty("店铺ID")
    private String shopId;
    @ApiModelProperty("优惠券ID")
    private String couponId;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("优惠券数量")
    private BigDecimal couponNum;
    @ApiModelProperty("优惠券类型")
    private int couponType;
    @ApiModelProperty("优惠券种类")
    private int speciesType;
    //优惠券名称
    private String  couponName;
    //优惠券配比
    private BigDecimal ratio;
    @ApiModelProperty("需要释放的天数")
    private int sendDays;
    @ApiModelProperty("是否可发放")
    private int isSend;

}
