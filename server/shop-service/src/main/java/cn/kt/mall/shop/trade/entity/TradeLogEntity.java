package cn.kt.mall.shop.trade.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class TradeLogEntity {

	private Long id;
	private String shopId;
	private String tradeId;
	private Short logType;
	@ApiModelProperty("状态(0待发货、1待收货-部分发货、2待收货、3已完成)")
	private Short logCode;
	@ApiModelProperty("操作人留的备注信息")
	private String logValue;
	@ApiModelProperty("附图1")
	private String logImg1;
	@ApiModelProperty("附图2")
	private String logImg2;
	@ApiModelProperty("附图3")
	private String logImg3;
	@ApiModelProperty("当logCode为2时，这里存储卖家发货的快递类型；")
	private String logValue1;
	@ApiModelProperty("当logCode为2时，这里存储卖家发货的快递编号；")
	private String logValue2;
	@ApiModelProperty("处理状态（0，处理中，1同意，2拒绝,3处理中状态（退货流程中表示买家申请退款，卖家同意退货状态））")
	private Short status = 0;
	private Date createTime;

	public TradeLogEntity() {

	}

	public TradeLogEntity(String shopId, String tradeId, Short logType, Short logCode, String logValue) {
		this.shopId = shopId;
		this.tradeId = tradeId;
		this.logCode = logCode;
		this.logValue = logValue;
		this.logType = logType;
	}

	public TradeLogEntity(String shopId, String tradeId, Short logType, Short logCode, String logValue, String logImg1,
			String logImg2, String logImg3) {
		this.shopId = shopId;
		this.tradeId = tradeId;
		this.logCode = logCode;
		this.logValue = logValue;
		this.logType = logType;
		this.logImg1 = logImg1;
		this.logImg2 = logImg2;
		this.logImg3 = logImg3;
	}

	public TradeLogEntity(String shopId, String tradeId, Short logType, Short logCode, String logValue, String logImg1,
			String logImg2, String logImg3, String logValue1, String logValue2) {
		this.shopId = shopId;
		this.tradeId = tradeId;
		this.logCode = logCode;
		this.logValue = logValue;
		this.logType = logType;
		this.logImg1 = logImg1;
		this.logImg2 = logImg2;
		this.logImg3 = logImg3;
		this.logValue1 = logValue1;
		this.logValue2 = logValue2;
	}
}
