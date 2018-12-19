package cn.kt.mall.shop.trade.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TradeCountVO implements Serializable {

	private static final long serialVersionUID = 7075540250386072506L;
	@ApiModelProperty("当天订单数")
	private int count;
	@ApiModelProperty("当天订单总金额（元）")
	private BigDecimal totalCny;
	@ApiModelProperty("当天订单总金额（LEM）")
	private BigDecimal totalLem;
	@ApiModelProperty("当天")
	private String day;

	public TradeCountVO(int count, BigDecimal totalCny, BigDecimal totalLem, String day) {
		super();
		this.count = count;
		this.totalCny = totalCny;
		this.totalLem = totalLem;
		this.day = day;
	}

	public TradeCountVO() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TradeCountVO [count=").append(count).append(", totalCny=").append(totalCny)
				.append(", totalLem=").append(totalLem).append(", day=").append(day).append("]");
		return builder.toString();
	}

}
