package cn.kt.mall.management.admin.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserOperatorLogVO implements Serializable {
    //'操作记录id
    private String id;

    //操作玩家的id
    private String userId;

    //操作人登录账号
    private String account;

    //操作时间
    private Date opratorTime  = new Date();
    //操作人职位(角色)
    private String position;
    //1.资金记录 2.通过 3.充值。4.批量发货',
    private int operatorType;
    //操作名称
    private String operatorName;

}
