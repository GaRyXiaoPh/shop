package cn.kt.mall.shop.coupon.repository;

import cn.kt.mall.shop.coupon.entity.UserCouponLogEntity;
import cn.kt.mall.shop.coupon.vo.CouponsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface UserCouponLogPatchMapper {

    int updatePatchuserCouponLogList(@Param("list") List<UserCouponLogEntity> listValue);

}
