package cn.kt.mall.shop.coupon.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ExtractVO implements Serializable {


    private static final long serialVersionUID = 4201397946922829355L;
    private String id;
    @ApiModelProperty(notes = "操作日期")
    private String createTime;
    @ApiModelProperty(notes = "提取账户")
    private String userId;
    @ApiModelProperty(notes = "提取账户用户名")
    private String trueName;
    @ApiModelProperty(notes = "提取账户手机号")
    private String mobile;
    @ApiModelProperty(notes = "优惠券类型")
    private String couponName;
    @ApiModelProperty(notes = "提取数量")
    private BigDecimal amount;
    @ApiModelProperty(notes = "提取数量之前")
    private BigDecimal amountBefore;
    @ApiModelProperty(notes = "提取数量之后")
    private BigDecimal amountAfter;
    @ApiModelProperty(notes = "提取手续费比例")
    private BigDecimal ratio;
    @ApiModelProperty(notes = "状态")
    private String status;
    @ApiModelProperty(notes = "优惠券ID")
    private String couponId;
    @ApiModelProperty(notes = "手续费")
    private BigDecimal poundage;
    @ApiModelProperty(notes = "到账账户")
    private String arriveAccount;

    @ApiModelProperty(notes = "开始时间")
    private String beginTime;
    @ApiModelProperty(notes = "结束时间")
    private String endTime;
    @ApiModelProperty(notes = "优惠券类型id")
    private String type;
    @ApiModelProperty(notes = "彩票积分操作类型 1.提取;2.转换成余额")
    private int  operateType;
    public ExtractVO(String beginTime, String endTime, String mobile,
                     String type, String status) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.mobile = mobile;
        this.type = type;
        this.status = status;
    }

    public ExtractVO() {
        super();
    }
}
