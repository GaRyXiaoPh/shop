package cn.kt.mall.common.bitcoin.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class EthAddressEntity implements Serializable {
    private static final long serialVersionUID = 5445011850866603506L;


    private BigInteger id;
    private String account;
    private String name;
    private String address;
    private Date createTime;

    public EthAddressEntity(String account, String name, String address){
        this.account = account;
        this.name = name;
        this.address = address;
        this.createTime = new Date();
    }
}
