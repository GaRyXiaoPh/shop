package cn.kt.mall.shop.trade.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TradeAbnormalRespVO implements Serializable{

	private static final long serialVersionUID = -5991518764721920434L;
	@ApiModelProperty("订单id")
	private String tradeId;
	@ApiModelProperty("订单编号")
	private String tradeNo;
	@ApiModelProperty("退货原因")
	private String reason;
	@ApiModelProperty("退款金额（元）")
	private BigDecimal amount;
	@ApiModelProperty("退款金额（莱姆）")
	private BigDecimal amountLem;
	@ApiModelProperty("买家名称")
	private String userName;
	@ApiModelProperty("买家昵称")
	private String nick;
	@ApiModelProperty("申请时间")
	private Date createTime;
	@ApiModelProperty("处理状态（0，处理中，1同意，2拒绝）")
	private Short processType;
	
	private String logImg1;
	private String logImg2;
	private String logImg3;
	private String logValue1;
	private String logValue2;
}
