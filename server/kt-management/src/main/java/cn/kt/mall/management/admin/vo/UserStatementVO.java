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
@NoArgsConstructor
public class UserStatementVO implements Serializable {

    private static final long serialVersionUID = 2842391362940412517L;
    @ApiModelProperty(notes = "消费时间")
    private Date createTime;
    @ApiModelProperty(notes = "消费账户用户名")
    private String trueName;
    @ApiModelProperty(notes = "消费账户手机号")
    private String mobile;
    @ApiModelProperty(notes = "消费账户id")
    private String relationUserId;
    @ApiModelProperty(notes = "消费金额")
    private BigDecimal totalPrice;
    @ApiModelProperty(notes = "推荐人用户名")
    private String referrerName;
    @ApiModelProperty(notes = "推荐人id")
    private String userId;
    @ApiModelProperty(notes = "推荐人手机号")
    private String referrerMobile;
    @ApiModelProperty(notes = "返券数量")
    private BigDecimal amount;
    @ApiModelProperty(notes = "返还推荐人比率")
    private BigDecimal returnRatio;
    @ApiModelProperty(notes = "总加价金额")
    private BigDecimal totalRaisePrice;
    @ApiModelProperty(notes = "消费总价=商品原价+商品加价")
    private String totalMoney;


}
