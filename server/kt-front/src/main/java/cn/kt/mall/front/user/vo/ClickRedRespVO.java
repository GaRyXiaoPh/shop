package cn.kt.mall.front.user.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClickRedRespVO {

	@ApiModelProperty("0有效 1 失效")
	private Short isEffective;
	@ApiModelProperty("0抢到 1未抢到")
	private Short isReceive;
	@ApiModelProperty("抢到金额")
	private BigDecimal receiveAmount;
}
