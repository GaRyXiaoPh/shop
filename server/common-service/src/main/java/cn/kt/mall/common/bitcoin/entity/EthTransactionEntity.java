package cn.kt.mall.common.bitcoin.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class EthTransactionEntity implements Serializable {
    private static final long serialVersionUID = 8774814635099963344L;

    private String hash;
    private String from;
    private String to;
    private String value;
    private String address;
    private String blockHash;
    private String blockNumber;
    private Date timestamp;
    private Date createTime;
}
