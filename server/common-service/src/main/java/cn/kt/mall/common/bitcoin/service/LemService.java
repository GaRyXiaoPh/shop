package cn.kt.mall.common.bitcoin.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.kt.mall.common.wallet.service.UserAssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.kt.mall.common.bitcoin.entity.BtcAddressEntity;
import cn.kt.mall.common.bitcoin.entity.BtcConfEntity;
import cn.kt.mall.common.bitcoin.entity.BtcTransactionEntity;
import cn.kt.mall.common.bitcoin.helper.LEMHelper;
import cn.kt.mall.common.bitcoin.mapper.BtcAddressDAO;
import cn.kt.mall.common.bitcoin.mapper.BtcConfDAO;
import cn.kt.mall.common.bitcoin.mapper.BtcTransactionDAO;

/**
 * Created by Administrator on 2018/1/29.
 */

@Service
public class LemService {

	private static final Logger logger = LoggerFactory.getLogger(LemService.class);
	private static int BlockInterval = 6;
	@Autowired
	private LEMHelper lemHelper;
	@Autowired
	private BtcAddressDAO btcAddressDAO;
	@Autowired
	private BtcConfDAO btcConfDAO;
	@Autowired
	private BtcTransactionDAO btcTransactionDAO;
	@Autowired
	private UserAssetService userAssetService;

	public String createAddress(String strUserID) {
		String address = lemHelper.getAccountAddress(strUserID);
		if (address != null) {
			if (btcAddressDAO.getAddress(address) == null) {
				BtcAddressEntity addressEntity = new BtcAddressEntity(strUserID, lemHelper.getCoinName(), address);
				btcAddressDAO.add(addressEntity);
			}
		}
		return address;
	}

	public String getAddress(String strUserID) {
		return lemHelper.getAccountAddress(strUserID);
	}

	public String sendFrom(String strUserID, String toAddress, double amount) {
		return lemHelper.sendFrom(strUserID, toAddress, amount);
	}

	public String sendToAddress(String toAddress, double amount) {
		return lemHelper.sendToAddress(toAddress, amount);
	}

	// //解析区块----------------------------------------------------------------
	/*public void StatBlock() {
		BtcConfEntity confEntity = btcConfDAO.getValue(lemHelper.getCoinName());
		int curBlock = (confEntity == null ? 0 : Integer.parseInt(confEntity.getValueex()));
		int lastBlock = lemHelper.getBlockCount();

		try {
			for (; curBlock < lastBlock - BlockInterval; curBlock++) {
				List<String> txids = lemHelper.getTransactionHashesByBlock(curBlock);
				for (int i = 0; i < txids.size(); i++) {
					Map<String, Object> mTrans = lemHelper.getTransactionEx(txids.get(i));
					if (mTrans == null)
						continue;
					SaveRawTransaction(mTrans, curBlock);
				}
				btcConfDAO.add(new BtcConfEntity(lemHelper.getCoinName(), String.valueOf(curBlock), "莱姆币统计"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void SaveRawTransaction(Map<String, Object> mTrans, int blockNum) {
		if (!mTrans.containsKey("timereceived"))
			return;
		if (!mTrans.containsKey("details"))
			return;
		if (!mTrans.containsKey("txid"))
			return;

		String hash = mTrans.get("txid").toString();
		String timereceived = mTrans.get("timereceived").toString();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> details = (List<Map<String, Object>>) mTrans.get("details");
		for (int i = 0; i < details.size(); i++) {
			Map<String, Object> detail = details.get(i);
			if (!detail.containsKey("category") || !detail.containsKey("address") || !detail.containsKey("amount")) {
				continue;
			}
			String category = detail.get("category").toString();
			String address = detail.get("address").toString();
			String amount = detail.get("amount").toString();

			// 只统计receive的交易
			if (!"receive".equals(category))
				continue;

			// 确定是否是此节点的地址
			BtcAddressEntity addressEntity = btcAddressDAO.getAddress(address);
			if (addressEntity == null)
				continue;
			// 交易记录存在则过滤
			BtcTransactionEntity tmpEntity = btcTransactionDAO.getTransaction(hash);
			if (tmpEntity != null)
				continue;

			BtcTransactionEntity transactionEntity = new BtcTransactionEntity(hash, blockNum, null, address, amount,
					new Date(Long.parseLong(timereceived) * 1000));
			walletLemService.transIn(hash, address, new BigDecimal(amount));
			btcTransactionDAO.add(transactionEntity);
		}
	}*/
}
