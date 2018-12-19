package cn.kt.mall.management.admin.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class MoneyEntity implements Serializable {

    private static final long serialVersionUID = 73918987750760264L;
    /** 主键 */
    private String id;
    /** 用户Id */
    private String userId;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
    /** 提现金额 */
    private long money;
    /** 提现状态状态：0未提现，1提现 */
    private String status;
    /** 备注 */
    private String remark;

}
