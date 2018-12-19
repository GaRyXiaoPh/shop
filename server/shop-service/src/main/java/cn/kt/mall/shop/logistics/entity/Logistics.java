
package cn.kt.mall.shop.logistics.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class Logistics implements Serializable {

	private static final long serialVersionUID = -3240493709086525368L;
	@ApiModelProperty(value = "序号 自增")
	private Integer id;
	@ApiModelProperty(value = "运单号")
	private String logisticsNo;
	@ApiModelProperty(value = "物流公司")
	private String com;
	@ApiModelProperty(value = "状态")
	private Integer logisticsStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public Integer getLogisticsStatus() {
		return logisticsStatus;
	}

	public void setLogisticsStatus(Integer logisticsStatus) {
		this.logisticsStatus = logisticsStatus;
	}
}
