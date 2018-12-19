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
public class BtcAddressEntity implements Serializable {
    private static final long serialVersionUID = 2086614927555604535L;

    private BigInteger id;
    private String account;
    private String name;
    private String address;
    private Date createTime;

    public BtcAddressEntity(String account, String name, String address){
        this.account = account;
        this.name = name;
        this.address = address;
        this.createTime = new Date();
    }
}
