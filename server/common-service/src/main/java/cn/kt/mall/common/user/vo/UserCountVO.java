package cn.kt.mall.common.user.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCountVO implements Serializable {

	private static final long serialVersionUID = 794161361376538447L;
	private String day;
	private int count;

	public UserCountVO(String day, int count) {
		super();
		this.day = day;
		this.count = count;
	}

	public UserCountVO() {
		super();
	}

}
