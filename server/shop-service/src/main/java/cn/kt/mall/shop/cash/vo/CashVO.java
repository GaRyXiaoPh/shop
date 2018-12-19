package cn.kt.mall.shop.cash.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CashVO {

    @ApiModelProperty(notes = "提现记录主键ID")
    private String id;
    @ApiModelProperty(notes = "用户ID")
    private String userId;
    @ApiModelProperty(notes = "提现金额", dataType = "BigDecimal")
    private BigDecimal cashAmount;
    @ApiModelProperty(notes = "处理结果")
    private String status;
    @ApiModelProperty(notes = "创建时间")
    private Date createTime;

}
