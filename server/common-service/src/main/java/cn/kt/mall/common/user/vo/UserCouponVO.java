package cn.kt.mall.common.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class UserCouponVO implements Serializable {

    private static final long serialVersionUID = -104578231601873343L;

    @ApiModelProperty("优惠券名称")
    private String couponName;

    @ApiModelProperty("可使用数量")
    private BigDecimal couponNum;

    @ApiModelProperty("待使用数量")
    private BigDecimal currentExtractNum;
}
