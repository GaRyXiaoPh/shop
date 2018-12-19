package cn.kt.mall.common.wallet.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class UserFundsVO implements Serializable {
    private String id;
    private String userId;//用户ID
    private String rechargeType;//充值类型
    private String operationType;//操作类型
    private BigDecimal rechargeAmount;//充值金额
    private Date createTime;//操作时间
    private String RechargeUser;//被充值人姓名
    private String RechargeUserMobile;//被充值人电话
    private String accountType; //账号类型
    private String shopType;//店铺类型
    private String remarks;//备注
}
