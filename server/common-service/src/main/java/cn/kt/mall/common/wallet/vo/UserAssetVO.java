package cn.kt.mall.common.wallet.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserAssetVO {
    private String userId;
    @ApiModelProperty("可用余额")
    private BigDecimal availableBalance;
    @ApiModelProperty("冻结余额")
    private BigDecimal reservedBalance;
}
