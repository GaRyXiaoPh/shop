package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class ExtractVO implements Serializable {


    private static final long serialVersionUID = 4201397946922829355L;
    private String id;
    @ApiModelProperty(notes = "操作日期")
    private Date createTime;
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
    @ApiModelProperty(notes = "提取手续费比例")
    private BigDecimal ratio;
    @ApiModelProperty(notes = "状态")
    private String status;
    @ApiModelProperty(notes = "优惠券ID")
    private String couponId;
    @ApiModelProperty(notes = "手续费")
    private BigDecimal poundage;
    @ApiModelProperty(notes = "提取之前的数量")
    private BigDecimal applyNumBefore;
    @ApiModelProperty(notes = "提取之后的数量")
    private BigDecimal applyNumAfter;
    @ApiModelProperty(notes = "审核人")
    private String operatorUserId;
    @ApiModelProperty(notes = "审核日期")
    private Date operatorTime;
    @ApiModelProperty(notes = "到账账户")
    private String arriveAccount;


    @ApiModelProperty(notes = "开始时间")
    private String beginTime;
    @ApiModelProperty(notes = "结束时间")
    private String endTime;
    @ApiModelProperty(notes = "优惠券类型id")
    private String type;
    @ApiModelProperty(notes = "操作类型")
    private String operateType;
    public ExtractVO(String beginTime, String endTime, String mobile,
                               String type, String status,String operateType) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.mobile = mobile;
        this.type = type;
        this.status = status;
        this.operateType = operateType;
    }

    public ExtractVO() {
        super();
    }
}
