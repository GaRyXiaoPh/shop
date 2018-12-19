package cn.kt.mall.common.bitcoin.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
public class BtcConfEntity implements Serializable {
    private static final long serialVersionUID = 8461624020116401747L;

    private String keyex;
    private String valueex;
    private String mark;
    private Date lastTime;

    public BtcConfEntity(String keyex, String valueex, String mark){
        this.keyex = keyex;
        this.valueex = valueex;
        this.mark = mark;
        this.lastTime = new Date();
    }
}
