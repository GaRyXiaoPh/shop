package cn.kt.mall.shop.shop.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShopCommentVO implements Serializable {
	private static final long serialVersionUID = -1920203191446536613L;
	@ApiModelProperty("id")
	private Long id;
	@ApiModelProperty(value = "如果是回复，这里的pid传回复的信息的id，否则传0", required = true)
	private Long pid;
	@ApiModelProperty(value = "店铺id", required = true)
	private String shopId;
	@ApiModelProperty(value = "交易id", required = true)
	private String tradeId;
	@ApiModelProperty(value = "是否匿名：0否，1是")
	private Short anonymity;
	@ApiModelProperty(hidden = true)
	private String userId;
	@ApiModelProperty(value = "类型：0 回复，1 评论", required = true)
	private Short commentType;
	@ApiModelProperty(value = "文本内容", required = true)
	private String commentText;
	@ApiModelProperty(value = "图片列表")
	private String commentPic;
	@ApiModelProperty(value = "评分")
	private Double score;
	
	private Date createTime;

	/** 用户名 */
	private String userName;
	/** 昵称 */
	private String nick;
	// 头像URL
	private String avatar;
}
