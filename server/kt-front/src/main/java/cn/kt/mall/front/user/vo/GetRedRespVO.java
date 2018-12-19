package cn.kt.mall.front.user.vo;

import java.math.BigDecimal;

import cn.kt.mall.im.friend.vo.FriendshipVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetRedRespVO {

	@ApiModelProperty("0有效 1 失效")
	private Short isEffective;
	@ApiModelProperty("0 未领取 1 已领取 2已抢完")
	private Short isReceive;
	@ApiModelProperty("抢到金额")
	private BigDecimal receiveAmount;
	
	private FriendshipVO friendshipVO;
	
}
