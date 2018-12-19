package cn.kt.mall.management.admin.vo.fund;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class DeliveFundsVO {
	
	@NotNull
	List<DeliveFundsItemVO> data;
}
