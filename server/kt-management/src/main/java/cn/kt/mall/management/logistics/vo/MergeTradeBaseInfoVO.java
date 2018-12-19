package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ApiModel("部分合单请求数据封装")
@Getter
@Setter
@NoArgsConstructor
public class MergeTradeBaseInfoVO implements Serializable {

    @ApiModelProperty("物流号")
    private String transportNo;
    @ApiModelProperty("物流名称")
    private String transportCompany;
    @ApiModelProperty("运费 ")
    private BigDecimal freightFree;

    public List<MergeTradeReqsVO> tradeReqsVOList;
}
