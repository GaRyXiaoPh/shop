package cn.kt.mall.front.user.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RedPushVO implements Serializable {

	private static final long serialVersionUID = -1789463406815622255L;

	private String userId;
	private String image;
	private String type = "get_red";

	public RedPushVO(String userId, String image, String type) {
		super();
		this.userId = userId;
		this.image = image;
		this.type = type;
	}

	public RedPushVO() {
		super();
	}

	public RedPushVO(String userId, String image) {
		super();
		this.userId = userId;
		this.image = image;
	}

}
