package cn.kt.mall.shop.cash.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class CashRespVO implements Serializable {

    private static final long serialVersionUID = -5991518764711920434L;

    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("提现时间")
    private Date createTime;
    @ApiModelProperty("批准时间")
    private Date updateTime;
    @ApiModelProperty("提币地址")
    private String popcAddress;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("用户名")
    private String trueName;
    @ApiModelProperty("提币数量")
    private BigDecimal cashAmount;
    @ApiModelProperty(notes = "提现状态。0未处理、1同意、2拒绝")
    private String status;
    @ApiModelProperty(notes = "用户ID")
    private String userId;
    @ApiModelProperty(notes = "用户PID")
    private String pid;
    @ApiModelProperty(notes = "店铺id",hidden = true)
    private String shopId;
    @ApiModelProperty(notes = "是否可以提币(0:否,1:是)")
    private String whetherPay;


}
