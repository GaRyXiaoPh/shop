package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * POPC解冻管理
 *
 */

@Setter
@Getter
public class UnfreezeLogVO implements Serializable {


    private static final long serialVersionUID = 4002188183534932539L;

    @ApiModelProperty("解冻时间")
    private Date unfreezeDate;
    @ApiModelProperty("解冻类型")
    private String type;
    @ApiModelProperty("解冻人真实姓名")
    private String trueName;
    @ApiModelProperty("解冻人手机号")
    private String mobile;
    @ApiModelProperty("解冻数量")
    private String unfreezeCash;
    @ApiModelProperty("解冻人ID")
    private String userId;

}
