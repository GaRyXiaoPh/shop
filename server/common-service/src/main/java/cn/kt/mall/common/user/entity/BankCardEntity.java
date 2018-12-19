package cn.kt.mall.common.user.entity;


import cn.kt.mall.common.user.model.UserLevel;
import cn.kt.mall.common.util.IDUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户银行卡信息
 * Created by jerry on 2017/12/21.
 */
@Getter
@Setter
@NoArgsConstructor
public class BankCardEntity implements Serializable {
    private static final long serialVersionUID = -1661830493897252605L;

    /** 主键 */
    private String id;

    /** 用户Id */
    private String userId;

    /** 真实姓名 */
    private String trueName;

    /** 银行卡号 */
    private String bankCard;

    /** 银行名称 */
    private String bankName;

    /** 开户行*/
    private String openBank;

    /** 创建时间 or 注册时间 */
    private Date createTime;

    /** 最后更新时间 */
    private Date updateTime;



    public BankCardEntity(String userId, String trueName, String bankCard, String bankName, String openBank) {
        this.userId = userId;
        this.trueName = trueName;
        this.bankCard = bankCard;
        this.bankName = bankName;
        this.openBank = openBank;
    }
}
