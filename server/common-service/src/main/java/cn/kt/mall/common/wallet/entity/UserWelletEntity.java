package cn.kt.mall.common.wallet.entity;

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
public class UserWelletEntity implements Serializable {

    @ApiModelProperty(notes = "id", dataType = "string")
    private String id;
    @ApiModelProperty(notes = "用户ID", dataType = "string")
    private String userId;
    @ApiModelProperty(notes = "popc钱包", dataType = "string")
    private String popcAddress;
}
