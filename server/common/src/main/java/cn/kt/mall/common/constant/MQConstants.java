package cn.kt.mall.common.constant;

public class MQConstants {

	// 确认收货超时队列
	public static final String TRADE_DONE_GOODS_TIMEOUT_QUEUE = "trade_done_goods_timeout_queue";
	// 订单支付超时队列
	public static final String TRADE_PAY_BATCH_TIMEOUT_QUEUE = "trade_pay_batch_timeout_queue";
	// 批量转账队列
	public static final String PAY_BATCH_TRADE_QUEUE = "pay_batch_trade_queue";
	// 转账队列
	public static final String PAY_TRADE_QUEUE = "pay_trade_queue";
	// 退款操作超时队列
	public static final String DRAWBACK_TIMEOUT_QUEUE = "drawback_timeout_queue";
	// 退货操作超时队列
	public static final String REFUND_TIMEOUT_QUEUE = "refund_timeout_queue";
	// 拒绝退款&拒绝退货，确认收货超时队列
	public static final String REFUSE_TRADE_QUEUE = "refuse_trade_queue";

	// 红包超时领取队列
	public static final String RED_TIMEOUT_QUEUE = "red_timeout_queue";

}
