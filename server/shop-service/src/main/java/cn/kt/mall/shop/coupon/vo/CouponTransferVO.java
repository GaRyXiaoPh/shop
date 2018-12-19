package cn.kt.mall.shop.coupon.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CouponTransferVO implements Serializable {


    private static final long serialVersionUID = 7388872498030929377L;

    private String id;
    @ApiModelProperty(notes = "转入用户ID")
    private String rollInUserId;
    @ApiModelProperty(notes = "转出用户ID")
    private String rollOutUserId;
    @ApiModelProperty(notes = "转让关系")
    private String relation;
    @ApiModelProperty(notes = "优惠券Id")
    private String couponId;
    @ApiModelProperty(notes = "数量")
    private BigDecimal amount;
    @ApiModelProperty(notes = "手续费")
    private BigDecimal fee;
    @ApiModelProperty(notes = "转让时间")
    private String createTime;
    @ApiModelProperty(notes = "转出账户姓名")
    private String trueName;
    @ApiModelProperty(notes = "转出账户姓名+手机号")
    private String trueNameAll;
    @ApiModelProperty(notes = "转出账户手机号")
    private String mobile;
    @ApiModelProperty(notes = "转入账户姓名")
    private String name;
    @ApiModelProperty(notes = "转入账户姓名+手机号")
    private String nameAll;
    @ApiModelProperty(notes = "转入账户手机号")
    private String phone;
    @ApiModelProperty(notes = "转让手续费比例")
    private BigDecimal feeRatio;

}
