package cn.kt.mall.common.shop.vo;

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
public class ShopSalesAmountVO implements Serializable {
    private static final long serialVersionUID = -9048528024968149363L;
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("修改前销售额度")
    private BigDecimal shopSalesAmountBefore;
    @ApiModelProperty("销售额度的变动金额")
    private BigDecimal shopSalesAmountChange;
    @ApiModelProperty("修改后销售额度")
    private BigDecimal shopSalesAmountAfter;
    @ApiModelProperty("操作类型 1：管理后台充值，2：会员消费")
    private String operateType;
    @ApiModelProperty("导致资金变化的纪录的ID")
    private String referenceId;
    @ApiModelProperty("创建时间")
    private Date createTime;

}
