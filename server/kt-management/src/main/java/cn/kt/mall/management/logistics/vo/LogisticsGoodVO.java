package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel("查询列表基本信息结构对象")
@Getter
@Setter
@NoArgsConstructor
public class LogisticsGoodVO implements Serializable {

    //订单的基本信息
    private LogisticsBaseInfoVO logisticsBaseVO;

    //订单的详细基本信息
    private List<LogisticsGoodDetailVO> goodDetailList;

    private Date createTime;

}
