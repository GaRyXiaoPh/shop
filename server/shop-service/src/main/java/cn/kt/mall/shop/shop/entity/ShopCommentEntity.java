package cn.kt.mall.shop.shop.entity;

import java.util.Date;
import java.util.List;

import cn.kt.mall.common.util.JSONUtil;
import cn.kt.mall.shop.shop.vo.CommentVO;
import cn.kt.mall.shop.trade.constant.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShopCommentEntity {

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

	// 新建评论实体
	public ShopCommentEntity(String shopId,String tradeId,String userId,String text, Double score, List<String> images){
        this.pid = 0L;
        this.shopId = shopId;
        this.tradeId = tradeId;
        this.commentType = Constants.TradeCommentType.COMMENT_1;
        this.userId = userId;
        this.commentText = text;
        this.score = score;
        CommentVO commentVO = new CommentVO(images);
        this.commentPic = JSONUtil.toJSONString(commentVO);
    }

	// 新建回复实体
	public ShopCommentEntity(Long pid, String shopId, String tradeId, String userId, String text) {
		this.tradeId = tradeId;
		this.pid = pid;
		this.userId = userId;
		this.commentText = text;
		this.commentType = Constants.TradeCommentType.COMMENT_0;
		this.shopId = shopId;
	}

	public ShopCommentEntity() {

	}
}
