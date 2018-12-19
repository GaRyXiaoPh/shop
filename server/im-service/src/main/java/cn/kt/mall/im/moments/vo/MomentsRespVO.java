package cn.kt.mall.im.moments.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MomentsRespVO implements Serializable{

	private static final long serialVersionUID = -8516804546566264874L;
	
	private Long id;
	@ApiModelProperty("信息内容")
	private String content;
	@ApiModelProperty("经度")
	private String addressLon;
	@ApiModelProperty("纬度")
	private String addressLat;
	@ApiModelProperty("定位位置")
	private String location;
	@ApiModelProperty("发布人")
	private String userId;
	@ApiModelProperty("头像")
	private String avatar;
	@ApiModelProperty("昵称")
	private String nick;
	@ApiModelProperty("用户名称")
	private String userName;
	@ApiModelProperty("备注名称")
	private String remarkName;
	@ApiModelProperty("发布时间")
	private Date createTime;

	@ApiModelProperty("信息图片")
	private List<String> images;

	@ApiModelProperty("点赞人列表")
	private List<LikeRespVO> likeList;
	@ApiModelProperty("评论列表")
	private List<CommentRespVO> commentList;
	
}
