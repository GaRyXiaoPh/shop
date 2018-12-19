package cn.kt.mall.common.wallet.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 解冻列表
 * @author gwj
 */

@Setter
@Getter
public class UnfreezeVO implements Serializable {

    private static final long serialVersionUID = -5569007771654168955L;

    @ApiModelProperty("主键id")
    private String id;
    private String userId;
    @ApiModelProperty("解冻类型 1:自然解冻  2:购买商品解冻")
    private Integer type;
    @ApiModelProperty("解冻比例")
    private String unfreezeRatio;
    @ApiModelProperty("盈利总金额")
    private BigDecimal profitCashTotal;
    @ApiModelProperty("解冻总金额 -- 盈利金额乘以 解冻比例")
    private BigDecimal unfreezeCashTotal;
    @ApiModelProperty("本次解冻金额")
    private BigDecimal unfreezeCash;
    @ApiModelProperty("解冻日期")
    private String unfreezeDate;
    @ApiModelProperty("创建时间")
    private Date createTime;

}
