package cn.kt.mall.im.moments.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LikeRespVO implements Serializable{
	
	private static final long serialVersionUID = 9066674530146697778L;

	@ApiModelProperty("点赞id")
	private Long id;
	@ApiModelProperty("点赞信息id")
	private Long momentsId;
	@ApiModelProperty("点赞人userId")
	private String userId;
	@ApiModelProperty("头像")
	private String avatar;
	@ApiModelProperty("昵称")
	private String nick;
	@ApiModelProperty("用户名称")
	private String userName;
	@ApiModelProperty("备注名称")
	private String remarkName;
	@ApiModelProperty("点赞时间")
	private Date createTime;
}
