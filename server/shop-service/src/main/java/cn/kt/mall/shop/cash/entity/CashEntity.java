package cn.kt.mall.shop.cash.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class CashEntity implements Serializable {
    private static final long serialVersionUID = 284225801729573398L;

    private String id;
    private String userId;
    private BigDecimal cashAmount;
    private String status;
    private Date createTime;
    private Date updateTime;
    private String remark;
}
