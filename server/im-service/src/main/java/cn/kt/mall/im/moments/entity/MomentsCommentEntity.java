package cn.kt.mall.im.moments.entity;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class MomentsCommentEntity {
    
	@ApiModelProperty(hidden = true)
	private Long id;

    @ApiModelProperty("回复ID,为0表示顶层评论")
    private Long replyId;
    
    @ApiModelProperty("莱粉圈信息id")
    private Long momentsId;

    @ApiModelProperty(hidden = true)
    private String userId;
    
    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public Long getMomentsId() {
        return momentsId;
    }

    public void setMomentsId(Long momentsId) {
        this.momentsId = momentsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}