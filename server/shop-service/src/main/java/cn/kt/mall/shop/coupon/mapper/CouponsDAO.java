package cn.kt.mall.shop.coupon.mapper;

import cn.kt.mall.shop.coupon.entity.CouponEntity;
import cn.kt.mall.shop.coupon.entity.CouponsEntity;
import cn.kt.mall.shop.coupon.entity.UserCouponEntity;
import cn.kt.mall.shop.coupon.entity.UserCouponLogEntity;
import cn.kt.mall.shop.coupon.vo.*;
import cn.kt.mall.shop.good.entity.GoodCouponCenterEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface CouponsDAO {

    int updateCouponStatus(CouponsVO couponsVO);

    int addCoupon(CouponsEntity couponsEntity);

    int updateCoupon(CouponsEntity couponsEntity);

    int getIsOrNotByGoods(String couponsId);

    int deleteCoupons(@Param("idsList") List<String> idsList);

    List<CouponsVO> getCouponsList(@Param("id") String id, RowBounds rowBounds);

    CouponsEntity getCouponsByKey(@Param("id") String id);

    CouponEntity getCouponById(@Param("id") String id);

    CouponEntity getCouponIdBypeciesType(@Param("speciesType") int speciesType);

    int addGoodCouponCenter(GoodCouponCenterEntity goodCouponCenterEntity);

    void delGoodCouponCenter(String goodId);

    List<CouponEntity> getSysCouponsList();
    //根据优惠券名称获取优惠券
    CouponEntity getCouponByName(@Param("couponName") String couponName);

    List<CouponVO> getUserCouponVOList(String userId);

    int insertUserCouponLog(UserCouponLogEntity userCouponLogEntity);

    int updateUserCoupon(UserCouponEntity userCouponEntity);

    int updateUserCouponLog(UserCouponLogEntity userCouponLogEntity);

    List<UserCouponLogEntity> getGoodCollectEntityByTradeId(@Param("tradeId") String tradeId);

    //后台订单使用
    List<UserCouponLogEntity> getGoodCollectEntityByTradeIdAndShop(@Param("tradeId") String tradeId);

    //用于优惠券定时发放作用
    List<UserCouponLogEntity> getUserCouponsLogListByTime(UserCouponSearchVO userCouponSearchVO);


    int getUserCoupobLogByTimeCount(UserCouponSearchVO userCouponSearchVO);
    /**
     *
     * @param
     *userId 根据用户id和
     *
     * @param couponId 优惠券Id查询出
     * @return
     */
    UserCouponEntity getUserCouponEntity(@Param("userId") String userId,@Param("couponId") String couponId);

    /**
     * 增加玩家的优惠券兑换统计信息
     */
    int addUserCouponEntity(UserCouponEntity entity);

    /**
     * 修改玩家的优惠券兑换统计信息
     */
    int updateUserCouponEntity(UserCouponEntity entity);

    List<CouponTimeConfigVo> getCouponTimeConfigList();

    List<CouponsVO> getAllTbCouponList();

    List<UserCouponLogEntity> getUsercouponLogDetailList(@Param("userId") String userId,@Param("couponId") String couponId,RowBounds rowBounds);

    UserCouponLogEntity getUsercouponLog(@Param("userId") String userId,@Param("couponId") String couponId,@Param("tradeId") String tradeId,@Param("goodId") String goodId);

    /**
     * 修改玩家的优惠券使用统计信息
     */
    int updateUserCouponBySend(UserCouponEntity entity);

    /**
     * 插入转让记录
     */
    int inserCouponTransfer(CouponTransferVO couponTransferVO);

    /**
     * 插入使用记录
     */
    int inserCouponExtract(ExtractVO extractVO);

    /**
     * 查询使用记录
     */
    List<ExtractVO> getCouponExtractLogList(@Param("userId") String userId,@Param("couponId") String couponId, RowBounds rowBounds);
    /**
     * 查询使用记录
     */
    List<ExtractVO> getCouponAllExtractLogList(@Param("userId") String userId,@Param("couponId") String couponId, RowBounds rowBounds);
    /**
     * 批量修改玩家优惠券汇总表
     */
    int updatePatchUserCouPonList(@Param("list")List<TBUserCouponVO> list);

    /**
     * 批量插入
     */
    int addPatchUserCouPonList(@Param("list")List<UserCouponEntity> list);

    List<UserCouponEntity> getUserCouponEntityList();

    List<UserCouponEntity> getUserCouponEntityUserIdAndTypeList();
}
