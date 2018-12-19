package cn.kt.mall.common.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户信息
 * Created by jerry on 2017/12/28.
 */
@Getter
@Setter
@NoArgsConstructor
public class LevleConfig implements Serializable {
    @ApiModelProperty(notes = "Id", dataType = "string")
    private String id;
    @ApiModelProperty(notes = "标识", dataType = "string")
    private String configKey;
    @ApiModelProperty(notes = "说明")
    private String configDesc;
    @ApiModelProperty(notes = "升级需要的金额", dataType = "string")
    private BigDecimal amount;
    @ApiModelProperty(notes = "增加等级", dataType = "string")
    private String configLevel;


}
