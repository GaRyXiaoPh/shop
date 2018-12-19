package cn.kt.mall.management.admin.service;

import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.management.admin.vo.*;
import cn.kt.mall.management.admin.vo.CashRecordVO;
import cn.kt.mall.shop.shop.vo.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资金业务接口类
 * Created by  on 2017/12/21.
 */
public interface FundOperationService {

    /**
     * 查询POPC解冻记录
     * @param type
     * @param beginTime
     * @param endTime
     * @param minNum
     * @param maxNum
     * @param pageNo
     * @param pageSize
     * @return
     */
    CommonPageVO<UnfreezeLogVO> getPopcList(String type, String beginTime, String endTime, BigDecimal minNum, BigDecimal maxNum, int pageNo, int pageSize);

    /**
     * 获取店铺销售记录列表
     * @param shopType
     * @param beginTime
     * @param endTime
     * @param shopNo
     * @param shopName
     * @param pageNo
     * @param pageSize
     * @return
     */
    CommonPageVO<ShopRespVO> getShopList(String shopType, String beginTime, String endTime, String shopNo, String shopName, int pageNo, int pageSize);


    /**
     *资金记录报表
     * @param opreatorUser
     * @param operationType
     * @param beginTime
     * @param endTime
     * @param phone
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */
    CommonPageVO<CashRecordVO> getCashRecordList(String opreatorUser,
                                                 String operationType,
                                                 String beginTime,
                                                 String endTime,
                                                 String phone,
                                                 String status,
                                                 int pageNo, int pageSize);


    //CommonPageVO<ShopStatisticsVO> getShopPerformanceList(Date startTime, Date endTime, String shopName, String userName, int pageNo, int pageSize);


    /**
     * 系统操作日志
     * @param account
     * @param startTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    CommonPageVO<UserOperatorLogVO> getOperationLog(String account, String startTime, String endTime, int pageNo, int pageSize);

    /**
     * 查询所有符合条件的资金变更记录
     * @param cashRecordReqVO
     * @return
     */
    List<CashRecordVO> getFundOperationList(CashRecordReqVO cashRecordReqVO);

    /**
     * 根据店铺id查询详情
     * @param shopId
     * @param startTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    CommonPageVO<ShopSalesVO> getShopSalesRecord(String shopId, String startTime, String endTime, int pageNo, int pageSize);

    /**
     * 查询转让记录
     * @param beginTime
     * @param endTime
     * @param rollOutAccount
     * @param rollInAccount
     * @param pageNo
     * @param pageSize
     * @return
     */
    CommonPageVO<CouponTransferVO> getCouponTransfer(String beginTime, String endTime, String rollOutAccount, String rollInAccount, int pageNo, int pageSize);

    List<CouponTransferVO> getCouponList(CouponTransferReqVO couponTransferReqVO);

    /**
     * 查询赠送记录
     * @param beginTime
     * @param endTime
     * @param iphone
     * @param ids
     * @param pageNo
     * @param pageSize
     * @return
     */
    CommonPageVO<CouponVO> getGivingRecord(String beginTime, String endTime, String iphone, List<String> ids, int pageNo, int pageSize);

    /**
     * 查询历史收益记录
     * @param beginTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    CommonPageVO<HistoryProfitVO> getHistoryProfit(String beginTime, String endTime, int pageNo, int pageSize);

    /**
     * 导出赠送记录
     * @param couponGiveReqVO
     * @return
     */
    List<CouponVO> getGivingList(CouponGiveReqVO couponGiveReqVO);

    /**
     * 导出系统操作日志
     * @param userOperatorLogReqVO
     * @return
     */
    List<UserOperatorLogVO> getOperatorLogList(UserOperatorLogReqVO userOperatorLogReqVO);

    /**
     * 导出历史盈利数据
     * @param historyProfitReqVO
     * @return
     */
    List<HistoryProfitVO> getHistoryProfitList(HistoryProfitReqVO historyProfitReqVO);
    /**
     * 导出历史盈利数据
     * @return
     */
    List<HeadVO> searchHead();
}
