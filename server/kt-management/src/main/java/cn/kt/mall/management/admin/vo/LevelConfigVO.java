package cn.kt.mall.management.admin.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class LevelConfigVO implements Serializable {

    private static final long serialVersionUID = -862265174963657895L;
    private String id;
    private String configKey;
    private String configDesc;
    private BigDecimal amount;
    private String configLevel;
}
