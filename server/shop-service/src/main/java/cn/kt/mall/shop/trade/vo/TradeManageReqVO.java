package cn.kt.mall.shop.trade.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.kt.mall.common.util.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TradeManageReqVO implements Serializable {

    private static final long serialVersionUID = -4886312560816859265L;
    @ApiModelProperty(notes = "店铺id", hidden = true)
    private String shopId;
    @ApiModelProperty(notes = "交易编号")
    private String tradeNo;
    @ApiModelProperty(notes = "名称")
    private String searchName;
    @ApiModelProperty(notes = "收货人")
    private String recvName;
    @ApiModelProperty(notes = "用户id")
    private String userId;

    @ApiModelProperty(notes = "开始时间")
    private Date startTime;
    @ApiModelProperty(notes = "结束时间")
    private Date endTime;

    @ApiModelProperty(hidden = true)
    private Short flag;
    @ApiModelProperty(hidden = true)
    private Short shopFlag;

    @ApiModelProperty(notes = "处理结果状态")
    private String status;
    @ApiModelProperty(notes = "处理类型状态")
    private Short logStatus;
    @ApiModelProperty(notes = "处理类型状态")
    private List<String> shopIdList;
    @ApiModelProperty(notes = "商品集合")
    private List<String> goodList;
    @ApiModelProperty(notes = "订单内部单号")
    private String interiorNo;

    public TradeManageReqVO(String shopId, String tradeNo, String searchName, String recvName, String startTime,
                            String endTime, String status, List<String> goodList) {
        super();
        this.shopId = shopId;
        this.tradeNo = tradeNo;
        this.searchName = searchName;
        this.recvName = recvName;
        this.startTime = DateUtil.getDateTime(startTime);
        this.endTime = DateUtil.getDateTime(endTime);
        this.status = status;
        this.goodList = goodList;

    }

    public TradeManageReqVO() {
        super();
    }

}
