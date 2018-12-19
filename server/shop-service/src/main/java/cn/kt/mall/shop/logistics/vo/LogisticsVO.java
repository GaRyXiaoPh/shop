package cn.kt.mall.shop.logistics.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class LogisticsVO implements Serializable {
    private Integer id;
    private String name;

    public LogisticsVO(String name) {
        this.name = name;
    }
}
