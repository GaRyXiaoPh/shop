package cn.kt.mall.im.moments.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MomentsPushVO implements Serializable {

	private static final long serialVersionUID = 3777859237685824992L;
	private String avater;
	private String nick;
	private Date createTime;
	private String content;
	private boolean isLike;
	private Long momentsId;

	private String targetContent;
	private String targetImg;

	public MomentsPushVO(Long momentsId, String avater, String nick, Date createTime, String content, boolean isLike, String targetContent, String targetImg) {
		super();
		this.momentsId = momentsId;
		this.avater = avater;
		this.nick = nick;
		this.createTime = createTime;
		this.content = content;
		this.isLike = isLike;
		this.targetContent = targetContent;
		this.targetImg = targetImg;
	}
}
