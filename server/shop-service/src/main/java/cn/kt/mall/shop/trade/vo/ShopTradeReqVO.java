package cn.kt.mall.shop.trade.vo;

import cn.kt.mall.common.util.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 物流管理传入参数
 * Created by gwj on 2018/06/09
 */
@Setter
@Getter
public class ShopTradeReqVO implements Serializable {

    private static final long serialVersionUID = 3447171868819892507L;

    @ApiModelProperty(notes = "状态(0待发货、1待收货-部分发货、2待收货、3已完成)")
    private String status;
    @ApiModelProperty(notes = "开始时间")
    private Date startTime;
    @ApiModelProperty(notes = "结束时间")
    private Date endTime;
    @ApiModelProperty(notes = "店铺编号")
    private String shopNo;

    @ApiModelProperty(notes = "店长名称")
    private String shopownerName;
    @ApiModelProperty(notes = "商品名称")
    private String goodsName;

    private String shopId;
    // true:管理后台查询
    private boolean adminQuery = true;

    public ShopTradeReqVO() {
        super();
    }

    public ShopTradeReqVO(String status, String startTime, String endTime, String shopNo, String shopownerName, String goodsName) {
        super();
        this.status = status;
        this.startTime = DateUtil.getDateTime(startTime);
        this.endTime = DateUtil.getDateTime(endTime);
        this.shopNo = shopNo;
        this.shopownerName = shopownerName;
        this.goodsName = goodsName;
    }
}
