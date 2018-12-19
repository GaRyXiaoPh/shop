package cn.kt.mall.common.wallet.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.kt.mall.common.constant.RedisConstants;
import cn.kt.mall.common.util.HttpsUtils;
import cn.kt.mall.common.util.JSONUtil;
import cn.kt.mall.common.util.RedisUtil;
import cn.kt.mall.common.wallet.vo.LemRateVO;

@Service
public class LemRateService {

	private static Logger logger = LoggerFactory.getLogger(LemRateService.class);
	private static final String LEM_RATE_URL = "https://api.rmtransaction.com/api/ticker/lmc";
	@Autowired
	private RedisUtil redisUtil;

	public Double getLemRateByHttps() {
		try {
			String result = HttpsUtils.getByHttp(LEM_RATE_URL);
			if (StringUtils.isNotEmpty(result)) {
				logger.info(result);
				LemRateVO lemRateVO = JSONUtil.parseToObject(result, LemRateVO.class);
				if (lemRateVO != null && lemRateVO.getCurrency() == 0) {
					Double rate = lemRateVO.getUnitPriceCny();
					redisUtil.set(RedisConstants.LEM_RATE_KEY, String.valueOf(rate));
					return rate;
				}
			}
			logger.error("get lem result is null");
		} catch (Exception e) {
			logger.error("get lem rate fail.", e);
		}
		return null;
	}

	public Double getLemRate() {
		String object = redisUtil.get(RedisConstants.LEM_RATE_KEY);
		if (object != null) {
			return Double.valueOf(object);
		} else {
			return getLemRateByHttps();
		}
	}
}
