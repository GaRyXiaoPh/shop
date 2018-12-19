package cn.kt.mall.shop.coupon.repository;

import cn.kt.mall.shop.coupon.entity.UserReleaseCouponLogEntity;
import cn.kt.mall.shop.coupon.entity.UserReleaseCouponLogEntityExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface UserReleaseCouponLogEntityMapper {

    int addUserReleaseCouponLogEntity(UserReleaseCouponLogEntity record);

    int addPatchUserReleaseCouponLogEntity(@Param("list") List<UserReleaseCouponLogEntity> list);

    //List<UserReleaseCouponLogEntity> getUserReleaseCouponLogEntityList(@Param("userId") String userId,@Param("couponLogId") String couponLogId);
}