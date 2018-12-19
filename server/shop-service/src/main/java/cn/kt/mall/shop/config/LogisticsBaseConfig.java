
package cn.kt.mall.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LogisticsBaseConfig {

	@Value("${logistics.kuaidi100.query.url:http://poll.kuaidi100.com/poll/query.do}")
	private String logisticsKuaidi100QueryUrl;
	@Value("${logistics.kuaidi100.subscribe.url:http://poll.kuaidi100.com/poll}")
	private String logisticsKuaidi100SubscribeUrl;
	@Value("${logistics.kuaidi100.customer:5FC82B9B6D3FDAF2EB564EE2F86A471E}")
	private String logisticsKuaidi100Customer;
	@Value("${logistics.kuaidi100.key:VivYuIfr8219}")
	private String logisticsKuaidi100Key;
	@Value("${logistics.kuaidi100.callback:http://120.79.145.234:8087/callback/logistics/kuaidi100}")
	private String logisticsKuaidi100CallBack;
	@Value("${logistics.kuaidi100.start:false}")
	private boolean start;

	public String getLogisticsKuaidi100QueryUrl() {
		return logisticsKuaidi100QueryUrl;
	}

	public String getLogisticsKuaidi100SubscribeUrl() {
		return logisticsKuaidi100SubscribeUrl;
	}

	public String getLogisticsKuaidi100Customer() {
		return logisticsKuaidi100Customer;
	}

	public String getLogisticsKuaidi100Key() {
		return logisticsKuaidi100Key;
	}

	public String getLogisticsKuaidi100CallBack() {
		return logisticsKuaidi100CallBack;
	}

	public boolean isStart() {
		return start;
	}

}
