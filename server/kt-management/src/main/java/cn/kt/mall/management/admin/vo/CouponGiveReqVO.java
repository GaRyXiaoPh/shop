package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class CouponGiveReqVO implements Serializable {

    private static final long serialVersionUID = 8136187154521882777L;
    @ApiModelProperty("开始时间")
    private String beginTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("用户")
    private String iphone;
    @ApiModelProperty("优惠券名称")
    private List<String> ids;

    public CouponGiveReqVO(String beginTime, String endTime, String iphone,
                           List<String> ids) {
        super();
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.iphone = iphone;
        this.ids = ids;
    }

    public CouponGiveReqVO() {
        super();
    }

}

