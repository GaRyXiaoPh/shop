package cn.kt.mall.management.admin.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 支付类型
 */
@Setter
@Getter
@NoArgsConstructor
public class PayTypeVO implements Serializable {

    private static final long serialVersionUID = -917505648576542404L;
    // id
    private String id;
    // 支付名称
    private String payName;
    // 支付类型
    private String payType;
    // 余额比例值(%)
    private String balanceRatio;
    // 其它比例值(%)
    private String otherRatio;
}
