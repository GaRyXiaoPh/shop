package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.beans.BeanInfo;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 解冻管理信息（今日营业额、冻结比例）
 * @author gwj
 */

@Setter
@Getter
public class UnfreezeInfoVO implements Serializable {

    private static final long serialVersionUID = -8506190599804287904L;

    @ApiModelProperty("今日营业额")
    private BigDecimal profitCashToday;

}
