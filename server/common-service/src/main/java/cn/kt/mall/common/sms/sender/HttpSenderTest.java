package cn.kt.mall.common.sms.sender;

public class HttpSenderTest {
	public static void main(String[] args) {
		String url = "http://222.73.117.158/msg/";// 应用地址
		url = "http://47.92.118.152/msg/";
		String account = "vip_wqt_sztest";// 账号
		String pswd = "CgXnYNv7Ns5ET";// 密码
		String mobile = "13113654807";// 手机号码，多个号码使用","分割
		String msg = "亲爱的用户，您的验证码是123456，5分钟内有效。";// 短信内容
		boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
		String product = "141-92";// 产品ID
		String extno = "0002";// 扩展码

		try {
			String returnString = HttpSender.batchSend(url, account, pswd, mobile, msg, needstatus, product, extno);
			System.out.println(returnString);
			// TODO 处理返回值,参见HTTP协议文档
		} catch (Exception e) {
			// TODO 处理异常
			e.printStackTrace();
		}
	}
}
