package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("订单商品excel导出")
public class LogisticsExportVO {
    @ApiModelProperty("excel名字")
    private String excelName;
    @ApiModelProperty("订单Id")
    private List<LogisticsIdsVO> logisticsIdsVOS;
}
