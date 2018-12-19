package cn.kt.mall.management.admin.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class FundVO implements Serializable {
    private static final long serialVersionUID = -818877246706999851L;
    private String id;
    private String userId;//用户ID
    private BigDecimal rechargeAmount;//充值金额
    private String rechargeType;//充值类型
    private Date createTime;//创建时间
    private String trueName;
    private String mobile;
    private Date beginTime;
    private Date endTime;
    //操作类型1：充值2：扣除
    private String operationType;
    //时间查询条件：如查询最近7天，30天等
    private String timeType;
    //审核状态：0：未审核,1:通过,2:未通过
    private String status;
}
