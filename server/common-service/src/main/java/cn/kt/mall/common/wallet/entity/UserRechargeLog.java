package cn.kt.mall.common.wallet.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserRechargeLog implements Serializable {
    private String id;
    private String userId;//用户ID
    private BigDecimal rechargeAmount;//充值金额
    private String rechargeType;//充值类型
    private Date createTime;//创建时间
    private String rechargeUser;//充值用户
    private String operationType;//操作类型
    private String status;//日志状态
    private String customerType;//1:商户后台用户 2：总后台用户
    private String remarks;//备注
}
