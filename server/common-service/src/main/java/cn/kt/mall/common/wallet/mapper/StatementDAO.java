package cn.kt.mall.common.wallet.mapper;

import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.vo.CountStatementVO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface StatementDAO {

	// 添加交易流水
	int add(StatementEntity entity);
	// 添加交易流水
	int addStatementGive(StatementEntity entity);
	// 查询交易流水
	int getStatementCount(@Param("userId") String userId);

	List<StatementEntity> getStatement(@Param("userId") String userId, @Param("offset") int offset,
			@Param("pageSize") int pageSize);

	int updateStatement(@Param("id") String id, @Param("hash") String hash, @Param("status") Short status);

	StatementEntity getById(@Param("id") String id);

	List<StatementEntity> getStatementListByStartDate(@Param("startTime") Date startTime);

	void updateByHash(@Param("hash") String hash, @Param("status") Short status);

	List<CountStatementVO> countStatement(@Param("shopId") String shopId, @Param("startMonth") String startMonth,
			@Param("endMonth") String endMonth, RowBounds rowBounds);

	BigDecimal countByStatementLem(@Param("shopId") String shopId);

	BigDecimal countByUserIdAndStatus(@Param("userId") String userId, @Param("tradeType") Short tradeType);

	List<CountStatementVO> countStatementByDay(@Param("shopId") String shopId, @Param("startMonth") String startMonth,
			@Param("endMonth") String endMonth);

	List<StatementEntity> getUserStatementDetailList(@Param("userId") String userId);

	List<StatementEntity> getuserCouponConsumeDetailList(@Param("userId") String userId, RowBounds rowBounds);

	List<StatementEntity> getUserAssetBaseReleaseStatementDetailList(@Param("userId") String userId);

	List<StatementEntity> getUserStatementTransferDetailList(@Param("userId") String userId, RowBounds rowBounds);

	/**
	 * 查询我的资余额明细
	 * @param userId
	 * @return
	 */
	List<StatementEntity> getMyAssetStatementList(String userId);
	/**
	 * 查询基础优惠券释放总数
	 * @param userId
	 * @return
	 */
	UserAssetEntity getAssetEntityBaseReleaseAmount(@Param("userId") String userId);
}
