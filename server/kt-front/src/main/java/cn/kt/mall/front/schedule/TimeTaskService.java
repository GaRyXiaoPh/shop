package cn.kt.mall.front.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.kt.mall.common.bitcoin.service.LemService;
import cn.kt.mall.common.wallet.service.LemRateService;
import cn.kt.mall.common.wallet.service.StatementService;

//定时任务服务

@Component
public class TimeTaskService {

	@Autowired
	private LemRateService lemRateService;
	@Autowired
	private StatementService statementService;

	@Autowired
	private LemService lemService;

	// 每2分钟执行一次未确认的区块交易记录
	@Scheduled(cron = "0 0/2 * * * *")
	public void StatBlock() throws Exception {
		//lemService.StatBlock();
	}

	// 每小时更新一次莱姆币兑换率
	@Scheduled(cron = "0 0/1 * * * *")
	public void updateLemRateByHttps() {
		//lemRateService.getLemRateByHttps();
	}

	// 每小时进行一次转账更新
	@Scheduled(cron = "0 0/1 * * * *")
	public void updateTransForm() {
		//statementService.transForm();
	}

}
