package cn.kt.mall.front.user.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RedPaperRespVO {
	
	@ApiModelProperty("红包id")
	private String id;
	@ApiModelProperty("发送者id")
	private String sendId;
	@ApiModelProperty("目标id")
	private String targetId;
	@ApiModelProperty("红包金额")
	private BigDecimal amount;
	@ApiModelProperty("红包数量")
	private Integer number;
	@ApiModelProperty("留言")
	private String words;
	@ApiModelProperty("红包类型1，通用-单聊红包，2通用-群聊红包，3群聊拼手气红包")
	private Short type;
	@ApiModelProperty("红包状态 1:有效 2:失效")
	private Short state;
	@ApiModelProperty("创建时间")
	private Date createTime;
	@ApiModelProperty("失效时间")
	private Date lastTime;
	
//	private String avatar;
//	private String nick;
//	private String userId;
//	private String userName;
}
