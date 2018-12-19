package cn.kt.mall.management.admin.vo.fund;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
public class DeliveFundsItemVO {
	
	@NotEmpty
	public String fundId;
	@NotEmpty
	public String status;
}
