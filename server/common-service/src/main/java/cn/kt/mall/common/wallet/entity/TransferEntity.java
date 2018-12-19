package cn.kt.mall.common.wallet.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

//转账，提现记录

@Getter
@Setter
@NoArgsConstructor
public class TransferEntity implements Serializable {
    private static final long serialVersionUID = 822148249504833344L;

    private String hash;
    private BigInteger block;
    private String from;
    private String to;
    private BigDecimal amount;
    private String mark;
    private String status;
    private Date createTime;
    private Date lastTime;

    public TransferEntity(String hash, String from, String to, BigDecimal amount, String mark, String status){
        this.hash = hash;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.mark = mark;
        this.status = status;
        this.createTime = new Date();
        this.lastTime = new Date();
    }
}
