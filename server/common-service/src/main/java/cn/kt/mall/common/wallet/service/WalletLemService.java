package cn.kt.mall.common.wallet.service;

import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.wallet.entity.WalletLemEntity;
import cn.kt.mall.common.wallet.mapper.WalletLemDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletLemService {

	private static Logger logger = LoggerFactory.getLogger(WalletLemService.class);
	@Autowired
	WalletLemDAO walletLemDAO;

	public WalletLemEntity queryWalletAddress(String userId) {
		return walletLemDAO.queryWalletAddress(userId);
	}

	//新增提币地址
	public void addWalletAddress(WalletLemEntity walletLemEntity){
		walletLemEntity.setId(IDUtil.getUUID());
		walletLemDAO.addWalletAddress(walletLemEntity);
	}

	//修改提币地址
	public void updateWalletAddress(WalletLemEntity walletLemEntity){
		walletLemDAO.updateWalletAddress(walletLemEntity);
	}

	// 删除币地址
	public void delWallet(String id) {
		walletLemDAO.delWallet(id);
	}
}
