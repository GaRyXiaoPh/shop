
package cn.kt.mall.shop.logistics.vo;

import java.util.ArrayList;

import io.swagger.annotations.ApiModelProperty;

public class LogisticsRespVO {

	private String message = "";
	private String nu = "";
	private String ischeck = "0";
	private String com = "";
	private String status = "0";
	private ArrayList<ResultItem> data = new ArrayList<ResultItem>();
	@ApiModelProperty("0在途中、1已揽收、2疑难、3已签收")
	private String state = "0";
	@ApiModelProperty("详细地址")
	private String condition = "";

	@SuppressWarnings("unchecked")
	public LogisticsRespVO clone() {
		LogisticsRespVO r = new LogisticsRespVO();
		r.setCom(this.getCom());
		r.setIscheck(this.getIscheck());
		r.setMessage(this.getMessage());
		r.setNu(this.getNu());
		r.setState(this.getState());
		r.setStatus(this.getStatus());
		r.setCondition(this.getCondition());
		r.setData((ArrayList<ResultItem>) this.getData().clone());

		return r;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNu() {
		return nu;
	}

	public void setNu(String nu) {
		this.nu = nu;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public ArrayList<ResultItem> getData() {
		return data;
	}

	public void setData(ArrayList<ResultItem> data) {
		this.data = data;
	}

	public String getIscheck() {
		return ischeck;
	}

	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

}
