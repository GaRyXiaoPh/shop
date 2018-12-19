package cn.kt.mall.front.user.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RedpaperReceiveEntity implements Serializable{

	private static final long serialVersionUID = 1886965177111239967L;

	private Long id;

    private String sendId;

    private String redpaperId;
    
    private Integer sortNo;

    private String receiveId;

    private BigDecimal amount;

    private String words;

    private Date createTime;

    private Date lastTime;

    public RedpaperReceiveEntity() {
		super();
	}

	public RedpaperReceiveEntity(String sendId, String redpaperId, String receiveId, BigDecimal amount, String words, Integer sortNo) {
		super();
		this.sendId = sendId;
		this.redpaperId = redpaperId;
		this.receiveId = receiveId;
		this.amount = amount;
		this.words = words;
		this.sortNo = sortNo;
	}
}