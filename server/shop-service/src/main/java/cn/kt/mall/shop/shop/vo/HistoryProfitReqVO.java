package cn.kt.mall.shop.shop.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 历史盈利数据
 *
 */

@Setter
@Getter
public class HistoryProfitReqVO implements Serializable {

    private static final long serialVersionUID = 7805815418469284751L;
    @ApiModelProperty("开始时间")
    private String beginTime;
    @ApiModelProperty("结束时间")
    private String endTime;

    public HistoryProfitReqVO(String beginTime, String endTime) {
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public HistoryProfitReqVO() {
        super();
    }

}
