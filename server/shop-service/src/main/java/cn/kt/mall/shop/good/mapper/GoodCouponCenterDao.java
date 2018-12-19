package cn.kt.mall.shop.good.mapper;

import cn.kt.mall.shop.coupon.vo.CouponsVO;
import cn.kt.mall.shop.good.entity.GoodCouponCenterEntity;
import cn.kt.mall.shop.good.vo.GoodCouponCenterReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodCouponCenterDao {
    List<GoodCouponCenterReqVO> getGoodCollectEntityByShopIdAndGoodId(@Param("shopId") String shopId, @Param("goodId") String goodNo);
    List<GoodCouponCenterEntity> getGoodCollectEntityByGoodIds(@Param("goodIds") List<String> goodIds);
    List<GoodCouponCenterEntity> getGoodCollectEntityByGoodId(@Param("goodId") String goodId);
    GoodCouponCenterEntity getGoodCouponCenterEntityByGoodIdAndCouponId(@Param("goodId") String goodId,@Param("couponId") String couponId);
    CouponsVO getGoodCouponCenterEntityByCouponName();
}
