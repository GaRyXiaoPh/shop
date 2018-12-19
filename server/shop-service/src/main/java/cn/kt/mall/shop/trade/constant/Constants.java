package cn.kt.mall.shop.trade.constant;

import java.math.BigDecimal;

public class Constants {
	public static final int SCALE = 4;//保留位数
	public static final BigDecimal BASE_FREE = new BigDecimal("0");
	public final class TradeStatus {
		public static final String TRADE_NOTPAY = "0"; // 待客户支付
		public static final String TRADE_NOTSEND = "1"; // 客户支付成功，商家待发货
		public static final String TRADE_NOTRECV = "2"; // 商家发货成功，客户待收货
		public static final String TRADE_RECVED = "3"; // 客户已收货
		public static final String TRADE_PROBLEM = "4"; // 客户申请售后中
		public static final String TRADE_BACK_MONEY = "5"; // 客户申请退款中
		public static final String TRADE_BACK = "6"; // 客户申请退货中
		public static final String TRADE_PAY_FAIL = "8"; // 订单支付失败
		public static final String TRADE_TIMEOUT = "9"; // 订单支付超时
		public static final String TRADE_CANCEL = "10"; // 取消订单, 待支付待发货可以取消
		public static final String TRADE_DONE = "12"; // 已完成 订单完结（所有流程都走完，后续无流程可走）
	}

	public final class TradeLogCode {
		public static final short LOG_CREATE = 0;
		public static final String LOG_CREATE_VALUE = "买家创建订单成功";
		public static final short LOG_PAY = 1;
		public static final String LOG_PAY_VALUE = "买家支付订单成功";
		public static final short LOG_GOODS_START = 2;
		public static final String LOG_GOODS_START_VALUE = "卖家发货成功";
		public static final short LOG_GOODS_RECEVED = 3;
		public static final String LOG_GOODS_RECEVED_VALUE = "买家收货成功";
		public static final short LOG_GOODS_PROBLEM = 4;
		public static final String LOG_GOODS_PROBLEM_VALUE = "买家申请售后";
		public static final short LOG_MONEY_BACK = 5;
		public static final String LOG_MONEY_BACK_VALUE = "买家申请退款";
		public static final short LOG_GOODS_BACK = 6;
		public static final String LOG_GOODS_BACK_VALUE = "买家申请退货";
		public static final short LOG_GOODS_REMIND = 7;
		public static final String LOG_GOODS_REMIND_VALUE = "买家催单";
		public static final short LOG_TRADE_COMMENT = 8;
		public static final String LOG_TRADE_COMMENT_VALUE = "买家评论";
		public static final short LOG_TIMEOUT = 9;
		public static final String LOG_TIMEOUT_VALUE = "支付超时";
		public static final short LOG_CANCEL = 10;
		public static final String LOG_CANCEL_VALUE = "取消订单";
		public static final short LOG_DONE = 12;
		public static final String LOG_DONE_VALUE = "订单完成";
		public static final short LOG_DELETE = 13;
		public static final String LOG_DELETE_VALUE = "订单删除";
	}

	public final class TradeCommentType {
		public static final short COMMENT_0 = 0;// 回复
		public static final short COMMENT_1 = 1;// 评论
	}

	public final class TradeDoingStatus {
		public static final short WAITING = 0;
		public static final short SUCCESS = 1;
		public static final short FAIL = 2;
		public static final short DOING = 3;
	}
}
