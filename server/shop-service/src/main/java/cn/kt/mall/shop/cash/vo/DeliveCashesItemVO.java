package cn.kt.mall.shop.cash.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
public class DeliveCashesItemVO {
	
	@NotEmpty
	public String cashId;
	@NotEmpty
	public String status;
}
