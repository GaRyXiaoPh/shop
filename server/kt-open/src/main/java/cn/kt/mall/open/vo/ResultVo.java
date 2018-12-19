
package cn.kt.mall.open.vo;

import java.util.ArrayList;

import cn.kt.mall.common.util.JSONUtil;
import cn.kt.mall.shop.logistics.vo.ResultItem;

/**
 * 物流给第三方结果VO
 */
public class ResultVo {

	private String message = "";
	private String nu = "";
	private String ischeck = "0";
	private String com = "";
	private String status = "0";
	private ArrayList<ResultItem> data = new ArrayList<ResultItem>();
	private String state = "0";
	private String condition = "";

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

	@Override
	public String toString() {
		return JSONUtil.toJSONString(this);
	}
}
