package cn.kt.mall.management.admin.service;

import cn.kt.mall.management.admin.entity.CouponTimeConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CouponTimeConfigService {

     List<CouponTimeConfigEntity> getCouponTimeConfigList();

     void addCouponTimeConfig(List<String> dateList);

     void delCouponTimeConfig(List<String> idList);


}
