package cn.kt.mall.management.admin.vo.fund;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class FundReqVO implements Serializable {
    //操作类型(1：充值2：扣除)
    private String operationType;
    //状态(0：未审核,1：通过，2：拒绝)
    private String status;
    private String beginTime;
    private String endTime;
    //被操作人mobile
    private String mobile;
}
