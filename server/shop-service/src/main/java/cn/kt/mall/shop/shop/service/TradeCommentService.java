package cn.kt.mall.shop.shop.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.util.DateUtil;
import cn.kt.mall.shop.shop.entity.ShopCommentEntity;
import cn.kt.mall.shop.shop.mapper.ShopCommentDAO;
import cn.kt.mall.shop.shop.vo.ShopCommentVO;
import cn.kt.mall.shop.trade.constant.Constants;
import cn.kt.mall.shop.trade.entity.TradeEntity;
import cn.kt.mall.shop.trade.entity.TradeLogEntity;
import cn.kt.mall.shop.trade.mapper.TradeDAO;
import cn.kt.mall.shop.trade.mapper.TradeLogDAO;

@Service
public class TradeCommentService {

	@Autowired
	private ShopCommentDAO shopCommentDAO;
	@Autowired
	TradeDAO tradeDAO;
	@Autowired
	TradeLogDAO tradeLogDAO;
	@Autowired
	UserDAO userDAO;

	// 分页获取订单评价
	public PageVO<ShopCommentVO> getShopComment(String shopId, String searchName, String startTime, String endTime,
			int pageNo, int pageSize) {
		int srcPageNo = pageNo;
		if (pageNo >= 1)
			pageNo = pageNo - 1;
		int offset = pageNo * pageSize;
		Date start = DateUtil.getDateTime(startTime);
		Date end = DateUtil.getDateTime(endTime);
		int count = shopCommentDAO.listByPageCount(shopId, null, searchName, start, end);
		List<ShopCommentVO> result = shopCommentDAO.listByPage(shopId, null, searchName, start, end, offset, pageSize);
		return new PageVO<>(srcPageNo, pageSize, count, result);
	}

	// 订单评价
	@Transactional
	public void commentShopTrade(String userId, String tradeId, String text, Double score, List<String> images,
			Short anonymity) {
		A.check(score < 0 || score > 5, "评分范围为0-5分");
		// 获取订单详细，判断该笔订单是否可以评论
		TradeEntity entity = tradeDAO.getTradeById(userId, tradeId);
		A.check(entity == null, "无效订单");
		A.check(!Constants.TradeStatus.TRADE_RECVED.equals(entity.getStatus()), "订单还未确认收货，不能评论");
		tradeDAO.updateTradeStatusById(tradeId, Constants.TradeStatus.TRADE_DONE);
		ShopCommentEntity shopCommentEntity = new ShopCommentEntity(entity.getShopId(), tradeId, userId, text, score,
				images);
		shopCommentEntity.setAnonymity(anonymity);
		shopCommentDAO.insert(shopCommentEntity);

		// 更新店铺评分
		shopCommentDAO.updateShopPoint(entity.getShopId());

		tradeLogDAO.insert(new TradeLogEntity(entity.getShopId(), entity.getId(), (short) 1,
				Constants.TradeLogCode.LOG_TRADE_COMMENT, Constants.TradeLogCode.LOG_TRADE_COMMENT_VALUE));
	}

	// 回复订单评价
	public void replyComment(Long pid, String tradeId, String userId, String text) {
		// 获取订单详细，判断该笔订单是否可以回复
		TradeEntity entity = tradeDAO.getTradeById(null, tradeId);
		A.check(entity == null, "无效订单");
		ShopCommentEntity shopCommentEntity = shopCommentDAO.getMyCommentByTradeId(entity.getShopId(), tradeId, null);
		A.check(shopCommentEntity == null, "无效的评论对象");
		shopCommentDAO.insert(new ShopCommentEntity(pid, entity.getShopId(), tradeId, userId, text));
	}
}
