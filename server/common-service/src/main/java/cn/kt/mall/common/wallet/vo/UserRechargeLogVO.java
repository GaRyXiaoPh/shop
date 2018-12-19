package cn.kt.mall.common.wallet.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserRechargeLogVO implements Serializable {
    private String id;
    private String userId;//用户ID
    private BigDecimal rechargeAmount;//充值金额
    private String rechargeType;//充值类型
    private Date createTime;//创建时间
    private String trueName;
    private String mobile;
    private String beginTime;
    private String endTime;
    //用户名：手机号
    private String userName;
    //状态(0：通过,1：拒绝)
    private String status;
    //操作类型(1：充值2：扣除)
    private String operationType;
    //被操作对象
    private String rechargeUser;
    //操作对象(1:总后台管理员，2：商铺管理员)
    private String customerType;

}
