
package cn.kt.mall.open.vo;

/**
 * 物流回调请求VO
 */
public class NoticeRequestVo {

	private String status = "";
	private String billstatus = "";
	private String message = "";
	private ResultVo lastResult = new ResultVo();

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBillstatus() {
		return billstatus;
	}

	public void setBillstatus(String billstatus) {
		this.billstatus = billstatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResultVo getLastResult() {
		return lastResult;
	}

	public void setLastResult(ResultVo lastResult) {
		this.lastResult = lastResult;
	}
}
