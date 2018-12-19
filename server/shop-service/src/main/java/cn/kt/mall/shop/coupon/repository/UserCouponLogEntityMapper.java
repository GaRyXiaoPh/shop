package cn.kt.mall.shop.coupon.repository;


import java.util.List;

import cn.kt.mall.shop.coupon.entity.UserCouponLogEntity;
import cn.kt.mall.shop.coupon.entity.UserCouponLogEntityExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface UserCouponLogEntityMapper {
    int countByExample(UserCouponLogEntityExample example);

    int deleteByExample(UserCouponLogEntityExample example);

    int deleteByPrimaryKey(String id);

    int insertSelective(UserCouponLogEntity record);

    List<UserCouponLogEntity> selectByExample(UserCouponLogEntityExample example, RowBounds page);

    List<UserCouponLogEntity> selectByExample(UserCouponLogEntityExample example);

    UserCouponLogEntity selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") UserCouponLogEntity record, @Param("example") UserCouponLogEntityExample example);

    int updateByPrimaryKeySelective(UserCouponLogEntity record);
}