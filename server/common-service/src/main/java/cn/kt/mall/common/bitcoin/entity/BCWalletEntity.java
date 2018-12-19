package cn.kt.mall.common.bitcoin.entity;


import cn.kt.mall.common.util.IDUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BCWalletEntity implements Serializable {
    private static final long serialVersionUID = -206674879956823783L;

    private String id;
    private String type;
    private String platform;
    private String userId;
    private String address;
    private String password;
    private String file;
    private BigDecimal balance;
    private Date createTime;
    private Date lastTime;

    public BCWalletEntity(String userId, String type, String platform, String address, String password, String file) {
        super();
        this.id = IDUtil.getUUID();
        this.userId = userId;
        this.type = type;
        this.platform = platform;
        this.address = address;
        this.password = password;
        this.file = file;
    }
}
