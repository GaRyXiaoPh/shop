package cn.kt.mall.common.wallet.mapper;

import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserAssetDAO {
    //添加资产
    int addAsset(UserAssetEntity entity);

    // 获取资产列表
    List<UserAssetEntity> getAssetByUserId(@Param("userId") String userId);

    List<UserAssetEntity> getAssetToRelease(@Param("currency") String currency, RowBounds rowBounds);

    BigDecimal getTotalReservedBalance(@Param("currency") String currency);

    UserAssetEntity getAssetByUserIdAndCurrency(@Param("userId") String userId, @Param("currency") String currency);
    //获取初始优惠券数据
    UserAssetEntity getAssetBaseByUserIdAndCurrency(@Param("userId") String userId, @Param("currency") String currency);

    /**
     * 更新
     *
     * @param userId
     * @param currency              资产代码
     * @param deltaAvailableBalance 可用余额的变化数量， 负数代表减少余额
     * @param deltaReservedBalance  冻结金额的变化数量， 负数代表减少
     */
    int updateAssetByUserId(@Param("userId") String userId,
                            @Param("currency") String currency,
                            @Param("deltaAvailableBalance") BigDecimal deltaAvailableBalance,
                            @Param("deltaReservedBalance") BigDecimal deltaReservedBalance);

    //根据用户ID查询用户资产类型 -- 锁住这一行来进行修改
    UserAssetEntity getAssetByUserIdAndTypeForUpdate(@Param("currency") String currency, @Param("userId") String userId);

    int updateAssetByUserIdAndMoneny(UserAssetEntity userAssetEntity);

    List<UserAssetEntity> getAssetAllByCurrency(@Param("currency") String currency);
}
