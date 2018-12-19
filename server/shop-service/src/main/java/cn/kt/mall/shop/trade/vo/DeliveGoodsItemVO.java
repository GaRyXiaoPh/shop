package cn.kt.mall.shop.trade.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveGoodsItemVO {
	
	@NotEmpty
	@ApiModelProperty("订单ID")
	public String tradeId;
	@NotEmpty
	@ApiModelProperty("物流公司名称")
	public String label;
	@NotEmpty
	@ApiModelProperty("物流单号")
	public String logisticsNo;
}
