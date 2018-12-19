package cn.kt.mall.management.admin.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class LevelConfigEntity implements Serializable {

    private static final long serialVersionUID = 73918987750760264L;
    /** 主键 */
    private String id;
    /** 标识 */
    private String configKey;
    /** 说明 */
    private String configDesc;
    /** 升级需要的金额 */
    private BigDecimal amount;
    /** 增加等级 */
    private String configLevel;

}
