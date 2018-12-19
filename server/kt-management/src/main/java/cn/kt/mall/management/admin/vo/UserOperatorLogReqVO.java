package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserOperatorLogReqVO implements Serializable {


    private static final long serialVersionUID = 6863541444580415304L;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("用户")
    private String account;

    public UserOperatorLogReqVO(String startTime, String endTime, String account) {
        super();
        this.startTime = startTime;
        this.endTime = endTime;
        this.account = account;
    }

    public UserOperatorLogReqVO() {
        super();
    }

}
