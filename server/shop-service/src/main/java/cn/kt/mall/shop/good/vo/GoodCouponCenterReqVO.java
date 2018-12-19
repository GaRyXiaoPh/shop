package cn.kt.mall.shop.good.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter

public class GoodCouponCenterReqVO implements Serializable {
    private String couponName;
    private long couponNum;
    private BigDecimal ratio;

}
