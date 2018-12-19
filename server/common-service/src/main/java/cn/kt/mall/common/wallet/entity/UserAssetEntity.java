package cn.kt.mall.common.wallet.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserAssetEntity implements Serializable {

    @ApiModelProperty(notes = "id", dataType = "string")
    private String id;
    @ApiModelProperty(notes = "用户ID", dataType = "string")
    private String userId;
    //是否可提现
    private Boolean withdrawable;
    private Boolean spendable;
    private Boolean internalAsset;
    //资产代号
    private String currency;
    //可用余额
    private BigDecimal availableBalance;
    //冻结余额 －－ 具体的冻结原因需要看交易历史
    private BigDecimal reservedBalance;
    private Date createTime;
    private Date lastTime;
    private String operationType;//日志操作类型 1:充值 2:扣除
    private String customerType;//用户类型 1：商铺用户 2：
    //余额明细
    private List<StatementEntity> statementEntityList;
    //转让起始金额
    private BigDecimal transferStart;
    //转让增加值
    private BigDecimal transferIncrease;

    //内部转让手续费百分比
    private BigDecimal insideRatio;
    //外部转让手续费百分比
    private BigDecimal outsideRatio;
    //优惠券初始化类型
    private int tradeType;
    // 用户初始化优惠券
    private BigDecimal reservedBalanceBase;
    // 用户初始化优惠券释放总数
    private BigDecimal releaseAmountBase;

    // 用户初始化优惠券释放总数
    private BigDecimal afterAmount;
}
