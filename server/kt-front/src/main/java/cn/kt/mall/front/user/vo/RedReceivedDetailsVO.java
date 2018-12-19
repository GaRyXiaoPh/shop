package cn.kt.mall.front.user.vo;

import java.math.BigDecimal;
import java.util.Date;

import cn.kt.mall.im.friend.vo.FriendshipVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RedReceivedDetailsVO {
	
	@ApiModelProperty("接收者id")
    private String receiveId;
	@ApiModelProperty("红包金额")
    private BigDecimal amount;
	@ApiModelProperty("留言")
    private String words;
	@ApiModelProperty("抢到时间")
    private Date createTime;
	
	private FriendshipVO friendshipVO;
}
