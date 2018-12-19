package cn.kt.mall.common.wallet.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.kt.mall.common.wallet.entity.WalletLemEntity;

@Mapper
public interface WalletLemDAO {
	// 查询钱包地址
	WalletLemEntity queryWalletAddress(@Param("userId") String userId);
	// 添加钱包
	int addWalletAddress(WalletLemEntity entity);

	// 修改钱包
	int updateWalletAddress(WalletLemEntity entity);

	// 删除币地址
	void delWallet(@Param("id") String id);

	// 更新币数量
	int updateCoin(@Param("userId") String userId, @Param("coin") BigDecimal coin);

	int updateCoinByNum(@Param("userId") String userId, @Param("num") BigDecimal num);

	// 更新现金数量
	int updateCoinFrozen(@Param("userId") String userId, @Param("coinFrozen") BigDecimal coinFrozen);

	int updateCoinFrozenByNum(@Param("userId") String userId, @Param("num") BigDecimal num);

	int frozenCoin(@Param("userId") String userId, @Param("num") BigDecimal num);

	int thawCoin(@Param("userId") String userId, @Param("num") BigDecimal num);

	// 获取钱包
	WalletLemEntity getWallet(@Param("userId") String userId);

	// 获取钱包根据地址
	WalletLemEntity getWalletByAddress(@Param("coinAddress") String coinAddress);

	BigDecimal countByAllUser(@Param("userId") String userId);

	BigDecimal countByUserType(@Param("userId") String userId, @Param("type") String type);
}
