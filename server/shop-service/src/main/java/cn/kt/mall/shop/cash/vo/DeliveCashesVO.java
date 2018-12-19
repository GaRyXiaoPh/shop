package cn.kt.mall.shop.cash.vo;

import cn.kt.mall.shop.trade.vo.DeliveGoodsItemVO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class DeliveCashesVO {
	
	@NotNull
	List<DeliveCashesItemVO> data;
}
