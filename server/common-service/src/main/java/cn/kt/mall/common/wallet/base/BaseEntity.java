package cn.kt.mall.common.wallet.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 4954848788695897079L;

    @ApiModelProperty(notes = "id", dataType = "string")
    private String id;
    @ApiModelProperty(notes = "用户ID", dataType = "string")
    private String userId;
    @ApiModelProperty(notes = "LEM地址", dataType = "string")
    private String coinAddress;
    @ApiModelProperty(notes = "LEM余额", dataType = "string")
    private BigDecimal coin;
    @ApiModelProperty(notes = "LEM冻结", dataType = "string")
    private BigDecimal coinFrozen;
    @ApiModelProperty(notes = "地址备注", dataType = "string")
    //币种备注
    private String remark;
    private Date createTime;
    private Date lastTime;
}
