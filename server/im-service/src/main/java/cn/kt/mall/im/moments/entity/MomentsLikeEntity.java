package cn.kt.mall.im.moments.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MomentsLikeEntity {
	private Long id;

	private Long momentsId;

	private String userId;

	private Date createTime;

	public MomentsLikeEntity(Long momentsId, String userId) {
		super();
		this.momentsId = momentsId;
		this.userId = userId;
	}

	public MomentsLikeEntity() {
		super();
	}

}