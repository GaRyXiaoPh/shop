package cn.kt.mall.front.user.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class RedpaperEntity implements Serializable{
	
	private static final long serialVersionUID = -7838443107549204539L;

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
	private Date createTime;
	private Date lastTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId == null ? null : sendId.trim();
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId == null ? null : targetId.trim();
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words == null ? null : words.trim();
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public Short getState() {
		return state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
}