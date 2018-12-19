package cn.kt.mall.mq.send;

import cn.kt.mall.common.constant.MQConstants;
import cn.kt.mall.mq.MQMgr;
import cn.kt.mall.mq.MQProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送mq消息
 */
public class TradeMQSend {

	private static final Logger logger = LoggerFactory.getLogger(TradeMQSend.class);
	@Autowired
	private MQMgr mqMgr;

	// 发送确认收货超时消息
	public void sendDelayDoneGoodsMessage(String tradeId) {
		Map<String, String> map = new HashMap<>();
		map.put("tradeId", tradeId);
		MQProperty mqProperty = new MQProperty(MQConstants.TRADE_DONE_GOODS_TIMEOUT_QUEUE, map,
				MQProperty.MQ_QUEUE_TYPE_ADD);
		mqProperty.setDelayTime(15 * 24 * 60 * 60 * 1000);// 距离收货时间过了15天设置需自动确认收货
		int num = mqMgr.send(mqProperty);
		logger.info("<sendDelayDoneGoodsMessage> send mq result:" + num);
		logger.info("<sendDelayDoneGoodsMessage> delayTime:" + 15 * 24 * 60 * 60 * 1000);
	}

	// 订单超时延迟消息
	public int sendDelayPayBatchMessage(String interiorNo) {
		Map<String, String> map = new HashMap<>();
		map.put("interiorNo", interiorNo);
		MQProperty mqProperty = new MQProperty(MQConstants.TRADE_PAY_BATCH_TIMEOUT_QUEUE, map,
				MQProperty.MQ_QUEUE_TYPE_ADD);
		mqProperty.setDelayTime(1 * 1 * 60 * 60 * 1000);// 支付超时间隔为1天
		int num = mqMgr.send(mqProperty);
		logger.info("<sendDelayPayBatchMessage> send mq result:" + num);
		logger.info("<sendDelayPayBatchMessage> delayTime:" + 1 * 24 * 60 * 60 * 1000);
		return num;
	}

//	// 退款处理延迟消息
//	public int sendDelayDrawBackMessage(String tradeId) {
//		Map<String, String> map = new HashMap<>();
//		map.put("tradeId", tradeId);
//		MQProperty mqProperty = new MQProperty(MQConstants.DRAWBACK_TIMEOUT_QUEUE, map, MQProperty.MQ_QUEUE_TYPE_ADD);
//		mqProperty.setDelayTime(7 * 24 * 60 * 60 * 1000);// 退款处理超时时间为7天
//		int num = mqMgr.send(mqProperty);
//		logger.info("<sendDelayDrawBackMessage> send mq result:" + num);
//		logger.info("<sendDelayDrawBackMessage> delayTime:" + 7 * 24 * 60 * 60 * 1000);
//		return num;
//	}

	// 退货处理延迟消息
	public int sendDelayRefundMessage(String tradeId) {
		Map<String, String> map = new HashMap<>();
		map.put("tradeId", tradeId);
		MQProperty mqProperty = new MQProperty(MQConstants.REFUND_TIMEOUT_QUEUE, map, MQProperty.MQ_QUEUE_TYPE_ADD);
		mqProperty.setDelayTime(15 * 24 * 60 * 60 * 1000);// 退货超时时间为15天
		int num = mqMgr.send(mqProperty);
		logger.info("<sendDelayRefundMessage> send mq result:" + num);
		logger.info("<sendDelayRefundMessage> delayTime:" + 15 * 24 * 60 * 60 * 1000);
		return num;
	}

	// 拒绝退款&拒绝退货，15天之后自动确认收货
	public int sendDelayRefuseMessage(String tradeId) {
		Map<String, String> map = new HashMap<>();
		map.put("tradeId", tradeId);
		MQProperty mqProperty = new MQProperty(MQConstants.REFUSE_TRADE_QUEUE, map, MQProperty.MQ_QUEUE_TYPE_ADD);
		mqProperty.setDelayTime(15 * 24 * 60 * 60 * 1000);// 拒绝退款&拒绝退货，订单自动确认收货时间为15天
		int num = mqMgr.send(mqProperty);
		logger.info("<sendDelayRefuseRefundMessage> send mq result:" + num);
		logger.info("<sendDelayRefuseRefundMessage> delayTime:" + 15 * 24 * 60 * 60 * 1000);
		return num;
	}
}
