package io.rong.messages;

import io.rong.util.GsonUtil;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RedInfoMessage extends BaseMessage {
	private String message;
	private String userId;
	private String name;
	private Long time;
	private String redType;// 1领取红包信息 ， 2红包被领取信息
	private transient static final String TYPE = "RC:RedPackageInfoNtf";

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this, RedInfoMessage.class);
	}

	public RedInfoMessage(String message, String userId, String name, Long time, String redType) {
		super();
		this.message = message;
		this.userId = userId;
		this.name = name;
		this.time = time;
		this.redType = redType;
	}

	public RedInfoMessage() {
		super();
	}

}
