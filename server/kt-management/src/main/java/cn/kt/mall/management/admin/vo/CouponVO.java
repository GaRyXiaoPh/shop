package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class CouponVO implements Serializable {
    private static final long serialVersionUID = 2970777341965201099L;
    private String id;
    private String userId;
    private long cdkeyNum;
    private long couponType;
    private long couponNum;

    private String status;
    //以下为查询赠送记录追加内容 6.27bylism
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("用户名称")
    private String trueName;
    @ApiModelProperty("用户手机号")
    private String mobile;
    @ApiModelProperty("优惠券名称")
    private String couponName;
    @ApiModelProperty("赠送数量")
    private String amount;

}

