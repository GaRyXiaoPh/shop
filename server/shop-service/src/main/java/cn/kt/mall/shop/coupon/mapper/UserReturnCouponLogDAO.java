package cn.kt.mall.shop.coupon.mapper;

import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.shop.coupon.entity.UserAssetBaseEntity;
import cn.kt.mall.shop.coupon.entity.UserReleaseCouponLogEntity;
import cn.kt.mall.shop.coupon.entity.UserReturnCouponLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserReturnCouponLogDAO {

    //获取所有返还的记录信息
    List<UserReturnCouponLogEntity> getUserReturnCouponLogList();

    //根据Id修改日志表
    int updateUserReturnCouponLogById(@Param("id") String id, @Param("currentReleaseNum") Integer currentReleaseNum,
                                      @Param("sendFinish") Integer sendFinish, @Param("afterAmount")BigDecimal afterAmount);

    //用户消费返还推荐人优惠券
    int insertUserReturnCouponLogById(UserReturnCouponLogEntity userReturnCouponLogEntity);

    UserReturnCouponLogEntity getUserReturnCouponLog(@Param("userId") String userId, @Param("tradeId") String tradeId);

    List<UserReleaseCouponLogEntity> getUserReleaseCouponLogEntityList(@Param("userId") String userId, @Param("couponLogId") String couponLogId);

    List<UserReleaseCouponLogEntity> getUserReleaseCouponLogEntityBaseList(@Param("userId") String userId);
    //获取初始化彩票积分释放总数
    UserAssetEntity getUserReleaseBaseAmount(@Param("userId") String userId);
    //批量修改

    int updatePatchUserReturnCouponLog(@Param("list") List<UserReturnCouponLogEntity> list);



}
