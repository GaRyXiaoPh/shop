
package cn.kt.mall.shop.logistics.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.kt.mall.common.util.JSONUtil;
import cn.kt.mall.common.util.RSAUtils;
import cn.kt.mall.shop.config.LogisticsBaseConfig;
import cn.kt.mall.shop.logistics.vo.LogisticsReqVO;
import cn.kt.mall.shop.logistics.vo.LogisticsRespVO;
import cn.kt.mall.shop.logistics.vo.LogisticsSubReqVO;
import cn.kt.mall.shop.logistics.vo.LogisticsSubRespVO;
import cn.kt.mall.shop.util.HttpUtil;

@Component
public class LogisticsBaseService {

	private final static Logger logger = LoggerFactory.getLogger(LogisticsBaseService.class);

	@Autowired
	private LogisticsBaseConfig logisticsBaseConfig;

	private final static String FORM_DATA = "form-data";

	public LogisticsRespVO query(LogisticsReqVO logisticsReqVo) {
		LogisticsRespVO logisticsRespVO = null;

		if (logisticsReqVo != null && !StringUtils.isBlank(logisticsReqVo.getCom())
				&& !StringUtils.isBlank(logisticsReqVo.getNum()) && logisticsBaseConfig.isStart()) {
			logisticsRespVO = kuaiDi100Query(logisticsReqVo);
		}
		return logisticsRespVO;
	}

	private LogisticsRespVO kuaiDi100Query(LogisticsReqVO logisticsReqVo) {
		LogisticsRespVO respVo = null;

		String param = JSONUtil.toJSONString(logisticsReqVo);
		String customer = logisticsBaseConfig.getLogisticsKuaidi100Customer();

		String key = logisticsBaseConfig.getLogisticsKuaidi100Key();
		String sign = RSAUtils.md5(param + key + customer).toUpperCase();
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("customer", customer));
		data.add(new BasicNameValuePair("sign", sign));
		data.add(new BasicNameValuePair("param", param));

		String resp = HttpUtil.post(logisticsBaseConfig.getLogisticsKuaidi100QueryUrl(), data, FORM_DATA);

		logger.info("url : {} param: {} result : {}", logisticsBaseConfig.getLogisticsKuaidi100QueryUrl(),
				data.toString(), resp);

		if (resp != null) {
			respVo = (LogisticsRespVO) JSONUtil.parseToObject(resp, LogisticsRespVO.class);

		}
		return respVo;
	}

	public boolean subscribe(LogisticsSubReqVO logisticsSubReqVo) {
		LogisticsSubRespVO logisticsSubRespVO = null;
		logisticsSubReqVo.setKey(logisticsBaseConfig.getLogisticsKuaidi100Key());
		logisticsSubReqVo.getParameters().put("callbackurl", logisticsBaseConfig.getLogisticsKuaidi100CallBack());

		String param = JSONUtil.toJSONString(logisticsSubReqVo);

		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("schema", "json"));
		data.add(new BasicNameValuePair("param", param));

		String resp = HttpUtil.post(logisticsBaseConfig.getLogisticsKuaidi100SubscribeUrl(), data, FORM_DATA);
		logger.info("url : {}  param : {} result: {} ", logisticsBaseConfig.getLogisticsKuaidi100SubscribeUrl(),
				data.toString(), resp);

		logisticsSubRespVO = (LogisticsSubRespVO) JSONUtil.parseToObject(resp, LogisticsSubRespVO.class);
		return logisticsSubRespVO.getResult();
	}

}
