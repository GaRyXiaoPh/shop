package cn.kt.mall.shop.trade.vo;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeliveGoodsVO {
	
	@NotNull
	List<DeliveGoodsItemVO> data;
}
