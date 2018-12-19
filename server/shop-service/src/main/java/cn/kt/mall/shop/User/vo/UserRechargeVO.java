package cn.kt.mall.shop.User.vo;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class UserRechargeVO implements Serializable {

    private String id;
    private Date createTime;
    private String trueName;
    private String username;
    private String mobile;
    private String referrer;
    private String referrerName;
    private String referrerMobile;

}
