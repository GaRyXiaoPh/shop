package cn.kt.mall.offline.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/25.
 */
@Setter
@Getter
public class DataEntity  implements Serializable{
    @ApiModelProperty("数量")
    private Integer  num;

    @ApiModelProperty("莱姆币")
    private Double  lyme;

    @ApiModelProperty("交易时间")
    private String dayTime;

    @ApiModelProperty("状态id")
    private Integer  status;

    @ApiModelProperty("状态名称")
    private String  statusName;


}
