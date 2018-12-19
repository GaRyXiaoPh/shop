package cn.kt.mall.common.sms.service;

import cn.kt.mall.common.sms.sender.HttpSenderSmsBao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.captcha.service.CaptchaImgService;
import cn.kt.mall.common.exception.ServerException;
import cn.kt.mall.common.sms.entity.SmsCaptcha;
import cn.kt.mall.common.sms.entity.SmsRecordEntity;
import cn.kt.mall.common.sms.mapper.SmsDAO;
import cn.kt.mall.common.sms.model.SmsCaptchaType;
import cn.kt.mall.common.sms.model.SmsSendResult;
import cn.kt.mall.common.sms.sender.HttpSender;
import cn.kt.mall.common.sms.sender.InternationalRes;
import cn.kt.mall.common.sms.sender.InternationalSender;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.util.JSONUtil;
import cn.kt.mall.common.util.RandomUtil;

/**
 * 短信业务处理类 Created by Administrator on 2017/6/18.
 */
@Service
public class SmsService {
	private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

	private static final String SUCCESS = "0";//发送成功
	private static final String PASSWORD = "30";//错误密码
	private static final String ACCOUNT = "40";//账号不存在
	private static final String BALANCE = "41";//余额不足
	private static final String IP = "43";//IP地址限制
	private static final String SENSITIVE = "50";//内容含有敏感词
	private static final String MOBILE = "51";//手机号码不正确
	private static final String PARAM = "%@%";

	@Value("${spring.profiles.active}")
	private String profiles;

	@Value("${sms.url}")
	private String url;
	@Value("${sms.account}")
	private String account;
	@Value("${sms.pswd}")
	private String pswd;
	@Value("${sms.product}")
	private String product;
	@Value("${sms.require:true}")
	private boolean verifyRequired;
	@Value("${sms.sign:【乾特商城】}")
	private String sign;
	@Value("${international-sms.account}")
	private String internationalAccount;

	@Value("${international-sms.password}")
	private String internationalPassword;

	/**
	 * 短信验证码长度
	 */
	private static final int SMS_CAPTCHA_LENGTH = 6;

	private CaptchaImgService captchaImgService;

	private SmsDAO smsDAO;

	@Autowired
	public SmsService(CaptchaImgService captchaImgService, SmsDAO smsDAO) {
		this.captchaImgService = captchaImgService;
		this.smsDAO = smsDAO;
	}

	public void sendSmsCaptcha(String userId, String nationalCode, String mobile, String captchaTypeCode,
			String captchaImgSno, String captchaImgWord) {
		// 检查图形验证码
		this.captchaImgService.check(captchaImgSno, captchaImgWord);
		this.sendSmsCaptcha(userId, nationalCode, mobile, captchaTypeCode);
	}

	/**
	 * 发送短信验证码
	 *
	 * @param mobile
	 *            手机号
	 */
	public void sendSmsCaptcha(String userId, String nationalCode, String mobile, String captchaTypeCode) {

		// A.checkParam(!RegexUtil.checkMobile(mobile), "手机号格式不正确");

		SmsCaptchaType captchaType = A.assertEnumParam(SmsCaptchaType.class, captchaTypeCode.trim(),
				"不支持的验证码类型:" + captchaTypeCode);

		// 2.检查是否连续发送短信验证码
		A.check(!isAllowCaptcha(mobile), "1分钟内只允许请求1条6短信验证码");

		// 开发环境使用
	/*	if (profiles.contains("dev") || profiles.contains("test")) {
			SmsCaptcha devCaptcha = new SmsCaptcha(IDUtil.getUUID(), mobile, "123456");
			// 存储短信记录
			updateMobileCaptcha(devCaptcha);
			return;
		}*/

		// 3.生成新的短信验证码
		String captcha = RandomUtil.getCode(SMS_CAPTCHA_LENGTH);
		SmsCaptcha smsCaptcha = new SmsCaptcha(IDUtil.getUUID(), mobile, captcha);

		try {
			// 4.获得短信内容
			String msg = sign + this.getCaptchaMsg(captchaType, captcha);

			// 5.调用短信服务
			if ("86".equals(nationalCode)) {
				String result = HttpSenderSmsBao.request(url, account, pswd, mobile, msg);
				if(SUCCESS.equals(result)){
					logger.info("短信验证码发送成功" + "------" + result);
				}else if(PASSWORD.equals(result)){
					logger.error("错误密码" +  "------" + result);
				}else if(ACCOUNT.equals(result)){
					logger.error("账号不存在" +  "------" + result);
				}else if(BALANCE.equals(result)){
					logger.error("余额不足" +  "------" + result);
				}else if(IP.equals(result)){
					logger.error("IP地址限制" + "------" +  result);
				}else if(SENSITIVE.equals(result)){
					logger.error("内容含有敏感词" + "------" +  result);
				}else if(MOBILE.equals(result)){
					logger.error("手机号码不正确" + "------" +  result);
				}

			/*	String returnString = HttpSender.batchSend(url, account, pswd, mobile, msg, true, product, null);
				SmsSendResult result = new SmsSendResult(returnString);

				if (!SUCCESS.equals(result.getCode())) {
					logger.error("调用短信服务接口失败，失败响应：" + JSONUtil.toJSONString(result) + "，服务端响应：" + returnString);
				} else {
					logger.debug("调用调用短信服务接口成功，成功响应：" + JSONUtil.toJSONString(result) + "，服务端响应：" + returnString);
					this.smsDAO.addSmsRecord(new SmsRecordEntity(mobile, "00", msg, userId));
				}*/
			} else {
				nationalCode = nationalCode == null || nationalCode.trim().length() == 0 ? "886" : nationalCode.trim();
				InternationalRes result = InternationalSender.send(internationalAccount, internationalPassword,
						nationalCode + mobile, "【LEM】" + msg);
				if (result == null || SUCCESS.equals(result.getCode())) {
					logger.error("调用国际短信服务接口失败，失败响应：" + JSONUtil.toJSONString(result));
				} else {
					logger.debug("调用调用短信服务接口成功，成功响应：" + JSONUtil.toJSONString(result));
					this.smsDAO.addSmsRecord(new SmsRecordEntity(mobile, "00", msg, userId));
				}
			}

			// 6.存储短信记录
			updateMobileCaptcha(smsCaptcha);
		} catch (Exception e) {
			throw new ServerException("调用发送短信接口错误", e);
		}
	}

	public void sendInfoMessage(String userId, String mobile, String content) {

//		A.checkParam(!RegexUtil.checkMobile(mobile), "手机号格式不正确");

		// 开发环境使用
		if (profiles.contains("dev") || profiles.contains("test")) {
			return;
		}

		try {
			String returnString = HttpSender.batchSend(url, account, pswd, mobile, content, true, product, null);
			SmsSendResult result = new SmsSendResult(returnString);

			if (!SUCCESS.equals(result.getCode())) {
				logger.error("调用短信服务接口失败，失败响应：" + JSONUtil.toJSONString(result) + "，服务端响应：" + returnString);
			} else {
				logger.debug("调用调用短信服务接口成功，成功响应：" + JSONUtil.toJSONString(result) + "，服务端响应：" + returnString);
				this.smsDAO.addSmsRecord(new SmsRecordEntity(mobile, "02", content, userId));
			}
		} catch (Exception e) {
			throw new ServerException("调用发送短信接口错误", e);
		}
	}

	private void updateMobileCaptcha(SmsCaptcha smsCaptcha) {
		int cnt = this.smsDAO.updateSmsCaptcha(smsCaptcha);
		if (cnt == 0) {
			this.smsDAO.addSmsCaptcha(smsCaptcha);
		}
	}

	/**
	 * 获取验证码短信
	 *
	 * @param type
	 *            验证码类型：SmsCaptchaType
	 * @param captcha
	 *            6位数字验证码
	 * @return String 消息内容
	 */
	private String getCaptchaMsg(SmsCaptchaType type, String captcha) {
		StringBuffer template = new StringBuffer(type.getTemplate());
		return replaceParam(template, captcha).toString();
	}

	private StringBuffer replaceParam(StringBuffer template, String paramVal) {
		int idx = template.indexOf(PARAM);
		return template.replace(idx, idx + PARAM.length(), paramVal);
	}

	/**
	 * 检查是否允许发送短信验证码(1分钟以内只能发送一条短信)
	 *
	 * @param mobile
	 *            手机号
	 * @return boolean
	 */
	private boolean isAllowCaptcha(String mobile) {
		return this.smsDAO.getAvailableCaptchaCntByMobile(mobile) == 0;
	}

	/**
	 * 检查手机短信验证码是否有效(5分钟)
	 *
	 * @param mobile
	 *            手机号
	 * @param captcha
	 *            验证码
	 * @return boolean
	 */
	public boolean checkCaptcha(String mobile, String captcha) {
		if (!verifyRequired) {
			return true;
		}

		return this.smsDAO.checkMobileCaptcha(mobile, captcha) > 0;
	}

}
