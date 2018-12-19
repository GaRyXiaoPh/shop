package cn.kt.mall.shop.trade.vo;

import java.math.BigDecimal;
import java.util.List;

import cn.kt.mall.common.wallet.vo.CountStatementVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManageCountRespVO {

	@ApiModelProperty("申请中商品数量")
	private Integer applyGoodCount;
	@ApiModelProperty("下架商品数量")
	private Integer downGoodCount;
	@ApiModelProperty("审核不通过商品数量")
	private Integer failGoodCount;
	@ApiModelProperty("审核通过中商品数量")
	private Integer passGoodCount;

	@ApiModelProperty("订单待付款数量")
	private Integer tradeNotPayCount;
	@ApiModelProperty("订单待发货数量")
	private Integer tradeNotSendCount;
	@ApiModelProperty("订单待评价数量")
	private Integer tradeCommentCount;
	@ApiModelProperty("订单退款待处理数量")
	private Integer tradeDrawBackCount;
	@ApiModelProperty("订单退货待处理数量")
	private Integer tradeRefoundCount;

	@ApiModelProperty("莱姆余额")
	private BigDecimal coin;
	@ApiModelProperty("莱姆收益")
	private BigDecimal incomeLem;

	@ApiModelProperty("最近七日订单统计数据")
	private List<TradeCountVO> tradeCountData;

	@ApiModelProperty("最近七日收益数据")
	private List<CountStatementVO> countStatementList;

	public ManageCountRespVO(Integer applyGoodCount, Integer downGoodCount, Integer failGoodCount,
			Integer passGoodCount, Integer tradeNotPayCount, Integer tradeNotSendCount, Integer tradeCommentCount,
			Integer tradeDrawBackCount, Integer tradeRefoundCount, List<TradeCountVO> tradeCountData, BigDecimal coin,
			BigDecimal incomeLem, List<CountStatementVO> countStatementList) {
		super();
		this.applyGoodCount = applyGoodCount;
		this.downGoodCount = downGoodCount;
		this.failGoodCount = failGoodCount;
		this.passGoodCount = passGoodCount;
		this.tradeNotPayCount = tradeNotPayCount;
		this.tradeNotSendCount = tradeNotSendCount;
		this.tradeCommentCount = tradeCommentCount;
		this.tradeDrawBackCount = tradeDrawBackCount;
		this.tradeRefoundCount = tradeRefoundCount;
		this.tradeCountData = tradeCountData;
		this.coin = coin;
		this.incomeLem = incomeLem;
		this.countStatementList = countStatementList;
	}

}
