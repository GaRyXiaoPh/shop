package cn.kt.mall.management.admin.vo.fund;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class FundRespVO implements Serializable {
    private static final long serialVersionUID = -818877246706999851L;
    private String id;
    private String userId;
    private Date createTime;//创建时间
    //操作类型1：充值2：扣除
    private String operationType;
    // 到账账户类型
    private String shopLevel;
    // 被操作人真实姓名
    private String trueName;
    // 被操作人mobile
    private String mobile;
    private BigDecimal rechargeAmount;//充值金额
    //审核状态：0：未审核,1:通过,2:未通过
    private String status;
    // 备注
    private String remarks;
}
