package cn.kt.mall.im.moments.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentRespVO implements Serializable {

	private static final long serialVersionUID = -5799638280823005828L;

	@ApiModelProperty("评论id")
	private Long id;
	@ApiModelProperty("回复id，为0表示顶层评论")
	private Long replyId;
	@ApiModelProperty("信息id")
	private Long momentsId;
	@ApiModelProperty("评论人id")
	private String userId;
	@ApiModelProperty("回复信息")
	private String content;
	@ApiModelProperty("评论时间")
	private Date createTime;
	@ApiModelProperty("头像")
	private String avatar;
	@ApiModelProperty("昵称")
	private String nick;
	@ApiModelProperty("用户名称")
	private String userName;
	@ApiModelProperty("备注名称")
	private String remarkName;
}
