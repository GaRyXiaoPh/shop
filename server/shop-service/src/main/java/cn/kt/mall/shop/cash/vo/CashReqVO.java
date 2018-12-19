package cn.kt.mall.shop.cash.vo;

import cn.kt.mall.common.util.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class CashReqVO implements Serializable{

	private static final long serialVersionUID = -4886312560816859265L;
	@ApiModelProperty(notes = "用户id", hidden = true)
	private String userId;
	@ApiModelProperty(notes = "提现状态")
	private String status;
	@ApiModelProperty(notes = "开始时间")
	private Date startTime;
	@ApiModelProperty(notes = "结束时间")
	private Date endTime;
	@ApiModelProperty(notes = "真实姓名")
	private String trueName;
	@ApiModelProperty(notes = "是否有店铺，0没有，1有")
	private String isShop;
	@ApiModelProperty(notes = "表名")
	private String excelName;
	public CashReqVO(String userId, String status, String startTime,
					 String endTime, String trueName, String isShop) {
		super();
		this.userId = userId;
		this.status = status;
		this.startTime = DateUtil.getDateTime(startTime);
		this.endTime = DateUtil.getDateTime(endTime);
		this.trueName = trueName;
		this.isShop = isShop;
	}

	public CashReqVO() {
		super();
	}

}
