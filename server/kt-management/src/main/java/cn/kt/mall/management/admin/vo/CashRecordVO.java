package cn.kt.mall.management.admin.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CashRecordVO implements Serializable {
    private static final long serialVersionUID = -1L;

    //操作时间
    private Date createTime;
    //操作类型
    private String operationType;
    //资金类型
    private String rechargeType;
    //数量
    private BigDecimal rechargeAmount;
    //操作人Id
    private String userId;
    //操作人
    private String opreatorUser;
    //操作人名字加账号
    private String opreatorAll;
    //登录账号
    private String loginName;
    //操作人店铺类型
    private String shopType;
    //登录ip
    private String loginIp;
    //状态
    private String status;
    //1为商户后台 2为总后台
    private String customerType;
    //对象(真名)
    private String trueName;
    //对象(电话号码)
    private String phone;
    //被操作人ID
    private String rechargeUser;
    //对象名字加账号
    private String rechargeAll;
    private String remarks;


}
