package cn.kt.mall.management.admin.dao;

import cn.kt.mall.management.admin.entity.CouponTimeConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CouponTimeConfigDAO {

     List<CouponTimeConfigEntity> getCouponTimeConfigList();

     int addCouponTimeConfig(CouponTimeConfigEntity couponTimeConfigEntity);

     int delCouponTimeConfig(String id);


}
