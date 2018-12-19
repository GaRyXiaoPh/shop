package cn.kt.mall.offline.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/31.
 */
@Setter
@Getter
public class OffGoodVO implements Serializable{

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("发布开始时间(格式:yyyy-mm-dd HH:mm:ss)")
    private String startTime;

    @ApiModelProperty("发布结束时间(格式:yyyy-mm-dd HH:mm:ss)")
    private String endTime;

    @ApiModelProperty("商品状态(0:全部 1:待审核  2:已上架  3:已下架  4:未通过)")
    private Integer status;

    @ApiModelProperty("页数")
    private Integer pageNo;

    @ApiModelProperty("页码")
    private Integer pageSize;
}
