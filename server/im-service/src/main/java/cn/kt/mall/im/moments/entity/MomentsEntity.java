package cn.kt.mall.im.moments.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MomentsEntity {
    private Long id;

    private String content;

    private String addressLon;

    private String addressLat;
    
	private String location;

    private String userId;

    private Date createTime;
    
	public MomentsEntity(String content, String addressLon, String addressLat, String userId) {
		super();
		this.content = content;
		this.addressLon = addressLon;
		this.addressLat = addressLat;
		this.userId = userId;
	}

	public MomentsEntity() {
		super();
	}
    
}