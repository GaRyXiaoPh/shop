package cn.kt.mall.mq.send;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.kt.mall.common.constant.MQConstants;
import cn.kt.mall.mq.MQMgr;
import cn.kt.mall.mq.MQProperty;

public class RedMQSend {
	private static final Logger logger = LoggerFactory.getLogger(RedMQSend.class);
	@Autowired
	private MQMgr mqMgr;

	// 发送红包超时消息
	public void sendDelayRedTimeOutMessage(String redId, Long delayTime) {
		Map<String, String> map = new HashMap<>();
		map.put("redId", redId);
		MQProperty mqProperty = new MQProperty(MQConstants.RED_TIMEOUT_QUEUE, map,
				MQProperty.MQ_QUEUE_TYPE_ADD);
		mqProperty.setDelayTime(delayTime + 1 * 24 * 60 * 60 * 1000);// 红包超时时间为24小时
		int num = mqMgr.send(mqProperty);
		logger.info("<sendDelayRedTimeOutMessage> send mq result:" + num);
		logger.info("<sendDelayRedTimeOutMessage> delayTime:" + delayTime + 1 * 24 * 60 * 60 * 1000);
	}
}
