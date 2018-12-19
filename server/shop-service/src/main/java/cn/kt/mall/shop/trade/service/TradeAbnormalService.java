package cn.kt.mall.shop.trade.service;

import java.util.List;

import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.enums.AssetType;
import cn.kt.mall.common.wallet.service.UserAssetService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.JSONUtil;
import cn.kt.mall.common.wallet.service.LemRateService;
import cn.kt.mall.common.wallet.service.StatementService;
import cn.kt.mall.mq.send.TradeMQSend;
import cn.kt.mall.shop.config.SysConfig;
import cn.kt.mall.shop.trade.constant.Constants;
import cn.kt.mall.shop.trade.entity.TradeEntity;
import cn.kt.mall.shop.trade.entity.TradeLogEntity;
import cn.kt.mall.shop.trade.mapper.TradeDAO;
import cn.kt.mall.shop.trade.mapper.TradeLogDAO;
import cn.kt.mall.shop.trade.vo.TradeAbnormalRespVO;
import cn.kt.mall.shop.trade.vo.TradeManageReqVO;
import cn.kt.mall.shop.trade.vo.TradeRefundVO;

/**
 * 异常订单
 * 
 * @author TS
 *
 */
public class TradeAbnormalService {

	@Autowired
	private TradeDAO tradeDAO;
	@Autowired
	private TradeLogDAO tradeLogDAO;
	@Autowired
	private UserAssetService userAssetService;
	@Autowired
	private SysConfig sysConfig;
	@Autowired
	private LemRateService lemRateService;
	@Autowired
	private StatementService statementService;
	@Autowired
	private TradeMQSend tradeMQSend;

	/**
	 * 问题订单搜索
	public CommonPageVO<TradeAbnormalRespVO> problemTradeList(TradeManageReqVO tradeManageReqVO, int pageNo,
			int pageSize) {
		List<TradeAbnormalRespVO> result = tradeDAO.drawTradeList(tradeManageReqVO, new RowBounds(pageNo, pageSize));
		PageInfo<TradeAbnormalRespVO> pageInfo = new PageInfo<>(result);
		return CommonUtil.copyFromPageInfo(pageInfo, result);
	}

	// 同意退款
	@Transactional
	public void agreeDrawBack(String shopId, String tradeId, String mark) {
		TradeEntity tradeEntity = tradeDAO.getTradeById(null, tradeId);
		A.check(tradeEntity == null, "订单不存在");
		A.check(!tradeEntity.getStatus().equals(Constants.TradeStatus.TRADE_BACK_MONEY), "买家没申请退款，不能执行退款相关操作");
		TradeLogEntity tradeLogEntity = tradeLogDAO.getById(shopId, tradeId, Constants.TradeLogCode.LOG_MONEY_BACK);
		A.check(tradeLogEntity == null || tradeLogEntity.getStatus() != 0, "订单数据异常或已经处理");
//		tradeDAO.updateTradeStatusById(tradeId, Constants.TradeStatus.TRADE_DONE);
		TradeLogEntity shopTradeLogEntity = new TradeLogEntity();
		shopTradeLogEntity.setShopId(shopId);
		shopTradeLogEntity.setTradeId(tradeId);
		shopTradeLogEntity.setLogCode(Constants.TradeLogCode.LOG_MONEY_BACK);
		shopTradeLogEntity.setLogValue2(mark);
		shopTradeLogEntity.setStatus((short) 1);
		tradeLogDAO.updateSelectiveById(shopTradeLogEntity);
		// 减去冻结的实际支付数量的莱姆币
		userAssetService.updateUserAsset(tradeEntity.getBuyUserId(), AssetType.CREDIT.getStrType(), tradeEntity.getCoined(), tradeEntity.getCoined().negate(), TradeType.TRANSFER_BACK, tradeId);



	}

	// 拒绝退款
	@Transactional
	public void refuseDrawBack(String shopId, String tradeId, String mark) {
		TradeEntity tradeEntity = tradeDAO.getTradeById(null, tradeId);
		A.check(tradeEntity == null, "订单不存在");
		A.check(!tradeEntity.getStatus().equals(Constants.TradeStatus.TRADE_BACK_MONEY), "买家没申请退款，不能执行退款相关操作");
		TradeLogEntity tradeLogEntity = tradeLogDAO.getById(shopId, tradeId, Constants.TradeLogCode.LOG_MONEY_BACK);
		A.check(tradeLogEntity == null || tradeLogEntity.getStatus() != Constants.TradeDoingStatus.WAITING,
				"订单数据异常或已经处理");
		TradeLogEntity shopTradeLogEntity = new TradeLogEntity();
		shopTradeLogEntity.setShopId(shopId);
		shopTradeLogEntity.setTradeId(tradeId);
		shopTradeLogEntity.setLogCode(Constants.TradeLogCode.LOG_MONEY_BACK);
		shopTradeLogEntity.setLogValue2(mark);
		shopTradeLogEntity.setStatus((short) Constants.TradeDoingStatus.FAIL);
		tradeLogDAO.updateSelectiveById(shopTradeLogEntity);
	}

	// 同意退货
	@Transactional
	public void agreeRefund(String shopId, String tradeId, TradeRefundVO tradeRefundVO, String mark) {
		TradeEntity tradeEntity = tradeDAO.getTradeById(null, tradeId);
		A.check(tradeEntity == null, "订单不存在");
		A.check(!tradeEntity.getStatus().equals(Constants.TradeStatus.TRADE_BACK), "买家没申请退货，不能执行退货相关操作");
		TradeLogEntity tradeLogEntity = tradeLogDAO.getById(shopId, tradeId, Constants.TradeLogCode.LOG_GOODS_BACK);
		A.check(tradeLogEntity == null || tradeLogEntity.getStatus() != Constants.TradeDoingStatus.WAITING,
				"订单数据异常或已经处理");
		TradeLogEntity shopTradeLogEntity = new TradeLogEntity();
		shopTradeLogEntity.setShopId(shopId);
		shopTradeLogEntity.setTradeId(tradeId);
		shopTradeLogEntity.setLogCode(Constants.TradeLogCode.LOG_GOODS_BACK);
		// 卖家同意退货保存的联系信息
		shopTradeLogEntity.setLogValue1(JSONUtil.toJSONString(tradeRefundVO));
		shopTradeLogEntity.setLogValue2(mark);
		shopTradeLogEntity.setStatus((short) Constants.TradeDoingStatus.DOING);
		tradeLogDAO.updateSelectiveById(shopTradeLogEntity);
		int rows = tradeMQSend.sendDelayRefundMessage(tradeId);
		A.check(rows != 1, "操作失败");
	}

	// 确认到货
	@Transactional
	public void sureRefund(String shopId, String tradeId, String mark) {
		TradeEntity tradeEntity = tradeDAO.getTradeById(null, tradeId);
		A.check(tradeEntity == null, "订单不存在");
		A.check(!tradeEntity.getStatus().equals(Constants.TradeStatus.TRADE_BACK), "买家没申请退货，不能执行退货相关操作");
		TradeLogEntity tradeLogEntity = tradeLogDAO.getById(shopId, tradeId, Constants.TradeLogCode.LOG_GOODS_BACK);
		A.check(tradeLogEntity == null || tradeLogEntity.getStatus() != Constants.TradeDoingStatus.DOING,
				"订单数据异常或已经处理");
//		tradeDAO.updateTradeStatusById(tradeId, Constants.TradeStatus.TRADE_DONE);
		TradeLogEntity shopTradeLogEntity = new TradeLogEntity();
		shopTradeLogEntity.setShopId(shopId);
		shopTradeLogEntity.setTradeId(tradeId);
		shopTradeLogEntity.setLogCode(Constants.TradeLogCode.LOG_GOODS_BACK);
		shopTradeLogEntity.setStatus(Constants.TradeDoingStatus.SUCCESS);
		tradeLogDAO.updateSelectiveById(shopTradeLogEntity);
		// 减去冻结的实际支付数量的莱姆币
		userAssetService.updateUserAsset(tradeEntity.getBuyUserId(), AssetType.CREDIT.getStrType(),
				tradeEntity.getCoined(), tradeEntity.getCoined().negate(), TradeType.TRANSFER_IN_SYS, tradeId);

	}

	// 拒绝退货
	@Transactional
	public void refuseRefund(String shopId, String tradeId, String mark) {
		TradeEntity tradeEntity = tradeDAO.getTradeById(null, tradeId);
		A.check(tradeEntity == null, "订单不存在");
		A.check(!tradeEntity.getStatus().equals(Constants.TradeStatus.TRADE_BACK), "买家没申请退货，不能执行退货相关操作");
		TradeLogEntity tradeLogEntity = tradeLogDAO.getById(shopId, tradeId, Constants.TradeLogCode.LOG_GOODS_BACK);
		A.check(tradeLogEntity == null || tradeLogEntity.getStatus() != Constants.TradeDoingStatus.WAITING,
				"订单数据异常或已经处理");
		TradeLogEntity shopTradeLogEntity = new TradeLogEntity();
		shopTradeLogEntity.setShopId(shopId);
		shopTradeLogEntity.setTradeId(tradeId);
		shopTradeLogEntity.setLogCode(Constants.TradeLogCode.LOG_GOODS_BACK);
		shopTradeLogEntity.setLogValue2(mark);
		shopTradeLogEntity.setStatus((short) Constants.TradeDoingStatus.FAIL);
		tradeLogDAO.updateSelectiveById(shopTradeLogEntity);
	}*/
}
