package cn.kt.mall.shop.coupon.mapper;

import cn.kt.mall.shop.coupon.entity.UserCouponLogEntity;
import cn.kt.mall.shop.coupon.vo.UserCouponSearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReleaseCouponsDAO {
    //释放购买送的优惠劵
    int  getReleaseUserCoupobLogByTimeCount(UserCouponSearchVO searchVO);

    List<UserCouponLogEntity> getReleaseUserCouponsLogListByTime(UserCouponSearchVO userCouponSearchVO);
}
