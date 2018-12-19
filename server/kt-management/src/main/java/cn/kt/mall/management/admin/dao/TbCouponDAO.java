package cn.kt.mall.management.admin.dao;


import cn.kt.mall.common.user.vo.UserCouponVO;
import cn.kt.mall.management.admin.entity.CouponEntity;
import cn.kt.mall.management.admin.vo.ExtractVO;
import cn.kt.mall.management.admin.vo.UserStatementVO;
import cn.kt.mall.shop.coupon.entity.UserCouponEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface TbCouponDAO {

    int deleteCouponByIds(@Param("idsList") List<String> idsList);

    int  addCoupon(CouponEntity couponsEntity);

    int updateCouponById(@Param("couponName") String couponName,
                         @Param("ratio") BigDecimal ratio,
                         @Param("couponType") Integer couponType,@Param("id") String id,
                         @Param("sendDays") Integer sendDays,
                         @Param("isSend") Integer isSend,@Param("isDocking") Integer isDocking
    );


    int updateCoupon4sendDays(@Param("id") String id,@Param("sendDays") Integer sendDays);


    List<CouponEntity> getCouponsList();

    CouponEntity getCouponEntityById(@Param("id") String id);

    List<CouponEntity> getCouponEntityBySendDay();

    /**
     * 查询消费返券记录
     * @param beginTime
     * @param endTime
     * @param rowBounds
     * @return
     */
    List<UserStatementVO> getReturnCoupon(@Param("beginTime") String beginTime,
                                          @Param("endTime") String endTime, RowBounds rowBounds);

    /**
     * 查询游戏彩票提取记录
     * @param beginTime
     * @param endTime
     * @param mobile
     * @param type
     * @param status
     * @param rowBounds
     * @return
     */
    List<ExtractVO> getExtractList(@Param("beginTime")String beginTime,
                                   @Param("endTime")String endTime,
                                   @Param("mobile")String mobile,
                                   @Param("type")String type,
                                   @Param("status")String status,
                                   @Param("operateType")String operateType,RowBounds rowBounds);
    //查询导出游戏彩票提取记录数据
    List<ExtractVO> getExtractList(ExtractVO extractVO);

    List<CouponEntity> getCouponListByIsDocking(@Param("isDocking")String isDocking);
    //根据主键查询提取优惠券记录
    ExtractVO getExtractById(@Param("id")String id);

    /**
     * 根据优惠券id与用户id查询用户该优惠券可用数
     * @param couponId
     * @param userId
     * @return
     */
    UserCouponEntity getUserCouponByUserIdAndTypeForUpdate(@Param("couponId")String couponId, @Param("userId")String userId);
    /**
     * 根据优惠券id与用户id更新用户该优惠券可用数
     * @return
     */
    int updateCouponByUserIdAndCouponNum(UserCouponEntity userCoupon);

    /**
     * 更新优惠券提取记录
     * @return
     */
    int updateLogStatus(ExtractVO extractVO);

    /**
     * 获取会员优惠券列表
     * @param userId 会员id
     * @return
     */
    List<UserCouponVO> getUserCouponList(@Param("userId") String userId);

    //插入使用记录
    int insertExtract(ExtractVO extractVO);

    /**
     * 根据优惠券id，userID查询用户优惠券关联表
     * @param couponId
     * @param userId
     * @return
     */
    UserCouponEntity getUserCouponByUserIdAndType(@Param("couponId")String couponId, @Param("userId")String userId);
}
