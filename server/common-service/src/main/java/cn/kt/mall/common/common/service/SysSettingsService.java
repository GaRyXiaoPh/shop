package cn.kt.mall.common.common.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import cn.kt.mall.common.common.response.CaesarResponse;
import cn.kt.mall.common.util.HttpsUtils;
import cn.kt.mall.common.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.mapper.SysSettingsMapper;
import cn.kt.mall.common.constant.CommonConstants;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysSettingsService {
	private static Logger logger = LoggerFactory.getLogger(SysSettingsService.class);


	@Autowired
	private SysSettingsMapper settingsMapper;


	public BigDecimal getReleaseRatio() {
		SysSettings settings = settingsMapper.selectByCode("unfreeze_ratio");
		A.check(settings == null, "解冻比例未配置");

		return new BigDecimal(settings.getLabel().trim()).divide(new BigDecimal("100"),2, RoundingMode.HALF_UP);
	}
	public BigDecimal getReturnRatio() {
		SysSettings settings = settingsMapper.selectByCode("return_ratio");
		A.check(settings == null, "返还推荐人比例未配置");

		return new BigDecimal(settings.getLabel().trim()).divide(new BigDecimal("100"),2, RoundingMode.HALF_UP);
	}
	public BigDecimal getShopPointBase() {
		SysSettings settings = settingsMapper.selectByCode("shop_point_base");
		A.check(settings == null, "商铺信用金最低基数未配置");

		return new BigDecimal(settings.getLabel().trim());
	}
	public List<SysSettings> getTranferConfig() {
		List<SysSettings> settingsList = settingsMapper.getTranferConfig();
		//A.check(settingsList.size() < 5, "转让基础数据未配置");
		return settingsList;
	}
	public String  getGoodName() {
		SysSettings settings = settingsMapper.selectByCode("good_name");
		A.check(settings == null, "正宇汽车参数未配置");
		return settings.getLabel().trim();
	}
	public SysSettings getLogisticsById(String code, String expCode) {
		return settingsMapper.selectByPrimaryKey(code, expCode);
	}

	public void updateRate(Integer shopRate, Integer userRate, Integer sysRate) {
		A.check(shopRate >= 100 || shopRate <= 0, "入参异常");
		A.check(userRate >= 100 || userRate <= 0, "入参异常");
		A.check(sysRate >= 100 || sysRate <= 0, "入参异常");
		A.check(shopRate + userRate + sysRate != 100, "入参异常");
		settingsMapper.updateByLabel(CommonConstants.MERCHANT, Double.toString((shopRate / 100.0d)));
		settingsMapper.updateByLabel(CommonConstants.REFERRER, Double.toString((userRate / 100.0d)));
		settingsMapper.updateByLabel(CommonConstants.PLATFORM, Double.toString((sysRate / 100.0d)));
	}

	//从凯撒平台获取指定币种的人民币价格（不是实时价格，而是某个区段的开盘价格）
	//把人民币金额除以这个价格，得出对应的虚拟币的数量
	public BigDecimal getCurrencyPrice(String currency) {
		SysSettings caesarApiHost = settingsMapper.selectByCode("caesar-api-host");
		A.check(caesarApiHost == null, "凯撒api地址未配置");

		String response = HttpsUtils.getByHttp(caesarApiHost.getLabel() + "/market/coin-price?symbol=" + currency + "&baseCurrency=cny");
		try {
			CaesarResponse caesarResponse = JSONUtil.parseToObject(response, CaesarResponse.class);
			return new BigDecimal(caesarResponse.getContent());
		} catch (Exception e) {
			logger.error("failed to parse caesar response",e);
			A.check(true, "failed to parse caesar response");
		}

		return null;
	}

	/**
	 * 获取阿里云oss配置
	 * @return
	 */
	public List<SysSettings> getOssParam() {
        List<SysSettings> sysSettings = settingsMapper.getOssParam();
		return sysSettings;
	}

	public SysSettings getSysSettingsByCode(String code){
		return settingsMapper.selectByCode(code);
	}

	/**
	 * 获取优惠券配置
	 * @return
	 */
    public List<SysSettings> getCouponParam() {
		List<SysSettings> sysSettings = settingsMapper.getCouponParam();
		return sysSettings;
    }

	/**
	 * 获取消费返优惠券配置
	 * @return
	 */
	public List<SysSettings> getReturnParam() {
		List<SysSettings> sysSettings = settingsMapper.getReturnParam();
		return sysSettings;
	}
	/**
	 * 编辑优惠券配置
	 * @param sysSettings
	 */
	@Transactional
	public void updateCouponConfig(List<SysSettings> sysSettings) {
		for (SysSettings list: sysSettings) {
			int rows = 0;
			rows = settingsMapper.updateByPrimaryKeySelective(list);
			A.check(rows != 1, "更新配置失败");
		}
	}
	/**
	 * 彩票/游戏积分提取续费手续费
	 * @return
	 */
	public List<SysSettings> getGameParam() {
		List<SysSettings> sysSettings = settingsMapper.getGameParam();
		return sysSettings;
	}
}
