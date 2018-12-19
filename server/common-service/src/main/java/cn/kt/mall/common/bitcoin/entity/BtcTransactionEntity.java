package cn.kt.mall.common.bitcoin.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BtcTransactionEntity implements Serializable {
    private static final long serialVersionUID = 8904850283938851722L;

    private String hash;
    private int block;
    private String from;
    private String to;
    private String amount;
    private Date recvTime;
    private Date createTime;

    public BtcTransactionEntity(String hash, int block, String from, String to, String amount, Date recvTime){
        this.hash = hash;
        this.block = block;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.recvTime = recvTime;
        this.createTime = new Date();
    }
}
