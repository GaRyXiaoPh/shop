package io.rong.messages;

import io.rong.util.GsonUtil;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MomentsMessage extends BaseMessage {

	private transient static final String TYPE = "RC:MonentsMsg";
	private String extra;

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this, MomentsMessage.class);
	}

	public MomentsMessage(String extra) {
		super();
		this.extra = extra;
	}

}
