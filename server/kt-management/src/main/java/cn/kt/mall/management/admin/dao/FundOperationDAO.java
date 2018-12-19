package cn.kt.mall.management.admin.dao;

import cn.kt.mall.common.wallet.vo.UserRechargeLogVO;
import cn.kt.mall.management.admin.vo.*;
import cn.kt.mall.shop.shop.vo.HistoryProfitReqVO;
import cn.kt.mall.shop.shop.vo.HistoryProfitVO;
import cn.kt.mall.shop.shop.vo.ShopSalesVO;
import cn.kt.mall.shop.shop.vo.ShopStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface FundOperationDAO {

    /**
     * 查找POPC解冻列表
     * @param type
     * @param beginTime
     * @param endTime
     * @param minNum
     * @param maxNum
     * @param rowBounds
     * @return
     */
    List<UnfreezeLogVO> getPopcList(@Param("type") String type,
                                    @Param("beginTime") String beginTime,
                                    @Param("endTime") String endTime,
                                    @Param("minNum") BigDecimal minNum,
                                    @Param("maxNum") BigDecimal maxNum, RowBounds rowBounds);

    /**
     * 查找店铺列表
     * @param shopType
     * @param shopNo
     * @param shopName
     * @param rowBounds
     * @return
     */
    List<ShopRespVO> getShopList(@Param("shopType") String shopType,
                                 @Param("shopNo") String shopNo,
                                 @Param("shopName") String shopName, RowBounds rowBounds);

    /**
     * 统计店铺业绩
     * @param shopId
     * @param beginTime
     * @param endTime
     * @return
     */
    BigDecimal getPerformance(@Param("shopId") String shopId,
                              @Param("beginTime") String beginTime,
                              @Param("endTime") String endTime);

    /**
     * 查询全部销售额
     * @return
     */
    BigDecimal getAllPerformance();

    /**
     * 查询资金明细
     * @param opreatorUser
     * @param beginTime
     * @param endTime
     * @param phone
     * @param status
     * @param rowBounds
     * @return
     */
    List<CashRecordVO> getFundDetailList(@Param("opreatorUser")String opreatorUser,
                                         @Param("operationType")String operationType,
                                         @Param("beginTime")String beginTime,
                                         @Param("endTime")String endTime,
                                         @Param("phone")String phone,
                                         @Param("status")String status, RowBounds rowBounds);


    List<CashRecordVO> getFundDetailList(CashRecordReqVO cashRecordReqVO);
    /**
     * 查询操作日志
     * @param account
     * @param startTime
     * @param endTime
     * @param rowBounds
     * @return
     */
    List<UserOperatorLogVO> getOperationLog(@Param("account") String account,
                                            @Param("startTime") String startTime,
                                            @Param("endTime") String endTime, RowBounds rowBounds);
    List<UserOperatorLogVO> getOperationLog(UserOperatorLogReqVO userOperatorLogReqVO);

    /**
     *
     * @param cashRecordReqVO
     * @return
     */
    List<CashRecordVO> getCashList(CashRecordReqVO cashRecordReqVO);

    /**
     * 查询店铺业绩合并接口
     * @return
     */
    List<ShopStatisticsVO> getShopStatisticsList( @Param("shopType")String shopType,
                                                  @Param("beginTime")String beginTime,
                                                  @Param("endTime")String endTime,
                                                  @Param("shopNo")String shopNo,
                                                  @Param("shopName")String shopName,
                                                  @Param("userName")String userName, RowBounds rowBounds);

    /**
     * 根据shopId查询详情
     * @param shopId
     * @param startTime
     * @param endTime
     * @param rowBounds
     * @return
     */
    List<ShopSalesVO> shopSalesRecord(@Param("shopId")String shopId,
                                      @Param("startTime")String startTime,
                                      @Param("endTime")String endTime,
                                      RowBounds rowBounds);

    /**
     * 查询优惠券转让记录
     * @param rollInAccount
     * @param rollOutAccount
     * @param beginTime
     * @param endTime
     * @param rowBounds
     * @return
     */
    List<CouponTransferVO> getCouponTransfer(@Param("rollInAccount")String rollInAccount,
                                             @Param("rollOutAccount")String rollOutAccount,
                                             @Param("beginTime")String beginTime,
                                             @Param("endTime")String endTime, RowBounds rowBounds);
    List<CouponTransferVO> getCouponTransfer(CouponTransferReqVO couponTransferReqVO);

    /**
     * 查询赠送记录
     * @param beginTime
     * @param endTime
     * @param iphone
     * @param ids
     * @param rowBounds
     * @return
     */
    List<CouponVO> getGivingRecord(@Param("beginTime")String beginTime,
                                   @Param("endTime")String endTime,
                                   @Param("iphone")String iphone,
                                   @Param("ids")List<String> ids, RowBounds rowBounds);
    List<CouponVO> getGivingRecord(CouponGiveReqVO couponGiveReqVO);

    /**
     * 查询日期
     * @param beginTime
     * @param endTime
     * @param rowBounds
     * @return
     */
    List<HistoryProfitVO> getTradeTimeList(@Param("beginTime")String beginTime,
                                           @Param("endTime")String endTime,
                                           RowBounds rowBounds);
    List<HistoryProfitVO> getTradeTimeList(HistoryProfitReqVO historyProfitReqVO);

    List<HistoryProfitVO> getInHistoryProfit( @Param("createTime")String createTime);

    List<HistoryProfitVO> getOutHistoryProfit(@Param("createTime")String createTime);

    //根据时间在表tb_user_return_coupon_log查询优惠券
    BigDecimal getCouponCount(@Param("createTime")String createTime);
}
