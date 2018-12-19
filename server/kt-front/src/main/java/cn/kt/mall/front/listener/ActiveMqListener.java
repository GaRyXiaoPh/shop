package cn.kt.mall.front.listener;

import java.util.Map;

import javax.jms.ObjectMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.kt.mall.common.constant.MQConstants;
import cn.kt.mall.common.exception.BusinessException;
import cn.kt.mall.mq.util.MQListener;
import cn.kt.mall.shop.trade.service.TradeAbnormalService;
import cn.kt.mall.shop.trade.service.TradeService;


public class ActiveMqListener {
	@Autowired
	private MQListener mq;
	@Autowired
	private TradeService tradeService;
	@Autowired
	private TradeAbnormalService tradeAbnormalService;

	private final static Logger LOGGER = LoggerFactory.getLogger(ActiveMqListener.class);

	// 监听订单支付超时队列
	@SuppressWarnings("unchecked")
	public void startTradeTimeOutDelayQueue() {
		mq.listener(MQConstants.TRADE_PAY_BATCH_TIMEOUT_QUEUE, message -> {
			try {
				ObjectMessage msg = (ObjectMessage) message;
				Map<String, String> objectMap = (Map<String, String>) msg.getObject();
				String interiorNo = objectMap.get("interiorNo");
				if (StringUtils.isNotEmpty(interiorNo)) {
					tradeService.timeOutTrade(interiorNo);
					// 手动发送回执
					message.acknowledge();
				}
			} catch (Exception e) {
				LOGGER.error("监听订单支付超时队列", e.getMessage());
			}
		});
	}

	// 监听确认收货超时队列
	@SuppressWarnings("unchecked")
	public void startDelayDoneGoodsMessage() {
		mq.listener(MQConstants.TRADE_DONE_GOODS_TIMEOUT_QUEUE, message -> {
			try {
				ObjectMessage msg = (ObjectMessage) message;
				Map<String, String> objectMap = (Map<String, String>) msg.getObject();
				String tradeId = objectMap.get("tradeId");
				if (StringUtils.isNotEmpty(tradeId)) {
					tradeService.recvTrade(null, tradeId, "确认订单超时，系统自动确认收货");
					// 手动发送回执
					message.acknowledge();
				}
			} catch (BusinessException e) {
				message.acknowledge();
				LOGGER.info("监听确认收货超时队列，处理结果<{}>", e.getMessage());
			} catch (Exception e) {
				LOGGER.error("监听确认收货超时队列", e.getMessage());
			}
		});
	}

	// 监听退款超时队列、超时未处理自动同意退款
//	@SuppressWarnings("unchecked")
//	public void startDelayDrawBackMessage() {
//		mq.listener(MQConstants.DRAWBACK_TIMEOUT_QUEUE, message -> {
//			try {
//				ObjectMessage msg = (ObjectMessage) message;
//				Map<String, String> objectMap = (Map<String, String>) msg.getObject();
//				String tradeId = objectMap.get("tradeId");
//				if (StringUtils.isNotEmpty(tradeId)) {
//					tradeAbnormalService.agreeDrawBack(null, tradeId, "商家处理退款超时，系统自动处理");
//					// 手动发送回执
//					message.acknowledge();
//				}
//			} catch (BusinessException e) {
//				message.acknowledge();
//				LOGGER.info("监听退款处理超时队列，处理结果<{}>", e.getMessage());
//			} catch (Exception e) {
//				LOGGER.error("监听退款处理超时队列失败");
//			}
//		});
//	}

	// 监听退货超时队列、超时未处理自动确认退货
	@SuppressWarnings("unchecked")
	public void startDelayRefundMessage() {
		mq.listener(MQConstants.REFUND_TIMEOUT_QUEUE, message -> {
			try {
				ObjectMessage msg = (ObjectMessage) message;
				Map<String, String> objectMap = (Map<String, String>) msg.getObject();
				String tradeId = objectMap.get("tradeId");
				if (StringUtils.isNotEmpty(tradeId)) {
					//tradeAbnormalService.sureRefund(null, tradeId, "商家确认收货超时，系统自动处理");
					// 手动发送回执
					message.acknowledge();
				}
			} catch (BusinessException e) {
				message.acknowledge();
				LOGGER.info("监听确认退货超时队列，处理结果<{}>", e.getMessage());
			} catch (Exception e) {
				LOGGER.error("监听退货确认收货队列失败");
			}
		});
	}

	// 监听退货确认收货队列、超时未处理自动确认收货
	@SuppressWarnings("unchecked")
	public void startDelayRefuseMessage() {
		mq.listener(MQConstants.REFUSE_TRADE_QUEUE, message -> {
			try {
				ObjectMessage msg = (ObjectMessage) message;
				Map<String, String> objectMap = (Map<String, String>) msg.getObject();
				String tradeId = objectMap.get("tradeId");
				if (StringUtils.isNotEmpty(tradeId)) {
					tradeService.refuseRecvTrade(null, tradeId, "商家拒绝退款退货，确认收货超时，系统自动确认收货");
					// 手动发送回执
					message.acknowledge();
				}
			} catch (BusinessException e) {
				message.acknowledge();
				LOGGER.info("监听确认收货超时队列，处理结果<{}>", e.getMessage());
			} catch (Exception e) {
				LOGGER.error("监听确认退货超时队列失败");
			}
		});
	}
	
	/**
	 * 监听红包超时消息队列
	 */
	/*public void startRedDelayMessage() {
		mq.listener(MQConstants.RED_TIMEOUT_QUEUE, message -> {
			try {
				ObjectMessage msg = (ObjectMessage) message;
				Map<String, String> objectMap = (Map<String, String>) msg.getObject();
				String redId = objectMap.get("redId");
				if (redId != null) {
					redPaperService.timeOut(redId);
					// 手动发送回执
					message.acknowledge();
				}
			} catch (Exception e) {
				LOGGER.error("监听红包超时消息队列失败");
			}
		});	
	}*/

}
