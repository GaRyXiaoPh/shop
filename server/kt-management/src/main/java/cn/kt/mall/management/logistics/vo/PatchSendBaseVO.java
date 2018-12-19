package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ApiModel("批量发货基础类，前端传入")
@Getter
@Setter
@NoArgsConstructor
public class PatchSendBaseVO implements Serializable {

    @ApiModelProperty("请求发货对象列表")
    private List<PatchSendReqsVO> sendReqsVOList;

    @ApiModelProperty("运费")
    private BigDecimal freightFree;

    //物流号
    @ApiModelProperty("物流号")
    private String transportNo;

    //物流公司
    @ApiModelProperty("物流公司")
    private String transportCompany;


}
