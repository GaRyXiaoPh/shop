package cn.kt.mall.shop.logistics.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class LogisticsDetail implements Serializable {

	private static final long serialVersionUID = -3240493709086525368L;
	@ApiModelProperty(value = "序号 自增")
	private Integer id;

	@ApiModelProperty(value = "物流号")
	private String logisticsNo;

	@ApiModelProperty(value = "物流详情")
	private String logisticsContext;

	@ApiModelProperty(value = "格式化的时间")
	private String ftime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogisticsContext() {
		return logisticsContext;
	}

	public void setLogisticsContext(String logisticsContext) {
		this.logisticsContext = logisticsContext;
	}

	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

}
