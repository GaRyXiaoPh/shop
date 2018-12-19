package cn.kt.mall.shop.trade.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TradeRefundVO implements Serializable{

	private static final long serialVersionUID = 3895554312254913548L;
	private String backName;
	private String backMobile;
	private String detailsAddress;
	private String province;
	private String city;
	private String county;
}
