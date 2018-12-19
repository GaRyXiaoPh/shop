package cn.kt.mall.shop.coupon.mapper;

import cn.kt.mall.shop.coupon.entity.CouponEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponDAO {
    CouponEntity getCouponList();
    CouponEntity getCouponEntityById(String id);
}
