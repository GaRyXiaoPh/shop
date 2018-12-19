package cn.kt.mall.offline.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator on 2018/5/12.
 */
@Setter
@Getter
public class OrderRequestVO {

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("开始时间 格式:yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @ApiModelProperty("结束时间 格式:yyyy-MM-dd HH:mm:ss")
    private String endTime;

    @ApiModelProperty("页码")
    private Integer pageNo;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("用户id(不用传)")
    private String shopId;

}
