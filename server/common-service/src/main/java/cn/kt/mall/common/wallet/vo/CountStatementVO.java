package cn.kt.mall.common.wallet.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CountStatementVO implements Serializable {

	private static final long serialVersionUID = 6053564783313442316L;
	private String month;
	private BigDecimal totalLem;

	public CountStatementVO(String month, BigDecimal totalLem) {
		super();
		this.month = month;
		this.totalLem = totalLem;
	}

	public CountStatementVO() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CountStatementVO [month=").append(month).append(", totalLem=").append(totalLem).append("]");
		return builder.toString();
	}

}
