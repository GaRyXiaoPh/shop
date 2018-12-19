package cn.kt.mall.management.admin.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CouponTransferReqVO implements Serializable {

    private static final long serialVersionUID = -4357558864075274748L;
    //开始时间
    private String beginTime;
    //结束时间
    private String endTime;
    //转出账户
    private String rollOutAccount;
    //转入账户
    private String rollInAccount;

    public CouponTransferReqVO(String beginTime, String endTime, String rollOutAccount,
                            String rollInAccount) {
        super();
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.rollOutAccount = rollOutAccount;
        this.rollInAccount = rollInAccount;
    }

    public CouponTransferReqVO() {
        super();
    }
}
