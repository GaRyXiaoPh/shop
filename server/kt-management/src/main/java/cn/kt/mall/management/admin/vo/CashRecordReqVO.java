package cn.kt.mall.management.admin.vo;

import cn.kt.mall.common.util.DateUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CashRecordReqVO implements Serializable {
    private static final long serialVersionUID = 8409011353794937436L;
    //开始时间
    private String beginTime;
    //结束时间
    private String endTime;
    //操作类型
    private String operationType;
    //操作人
    private String opreatorUser;
    //状态
    private String status;
    //手机号码(对象)
    private String phone;
    //操作人Id
    private String operatorUserId;

    public CashRecordReqVO(String beginTime, String endTime, String operationType,
                     String opreatorUser, String status, String phone) {
        super();
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.operationType = operationType;
        this.opreatorUser = opreatorUser;
        this.status = status;
        this.phone = phone;
    }

    public CashRecordReqVO() {
        super();
    }
}
