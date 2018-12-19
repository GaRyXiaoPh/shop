package cn.kt.mall.common.wallet.mapper;

import cn.kt.mall.common.wallet.entity.UserRechargeLog;
import cn.kt.mall.common.wallet.vo.CashRecordVO;
import cn.kt.mall.common.wallet.vo.UserFundsVO;
import cn.kt.mall.common.wallet.vo.UserRechargeLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserRechargeLogDAO {

    int addUserRechargeLog(UserRechargeLog userRechargeLog);

    List<UserRechargeLogVO> getUserRechargeLogListByUserId(UserRechargeLogVO userRechargeLogVO, RowBounds rowBounds);

    /**
     * 资金管理专用分页
     */
    List<UserFundsVO> getUserFundsByUserId(@Param("userId") String userId, @Param("rechargeType") String rechargeType, @Param("operationType") String operationType, @Param("mobile") String mobile, @Param("beginTime") String beginTime,
                                           @Param("endTime") String endTime, RowBounds rowBounds);

    /**
     * 根据日志id 查询该条记录
     *
     * @param id
     * @return
     */
    UserRechargeLogVO getUserRechargeLogListById(String id);

    /**
     * 更新日志状态
     *
     * @param id
     * @return
     */
    int updateLogStatus(@Param("id") String id, @Param("status") String status);


    /**
     * 查询资金明细
     *
     * @param beginTime
     * @param endTime
     * @param phone
     * @param status
     * @param rowBounds
     * @return
     */
    List<CashRecordVO> getFundDetailListByShop(
            @Param("userList") List<String> userList,
            @Param("operatorUserPhone") String operatorUserPhone,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime,
            @Param("phone") String phone,
            @Param("status") String status, RowBounds rowBounds);


    /**
     * 查询资金明细
     *
     * @param beginTime
     * @param endTime
     * @param phone
     * @param status
     * @return
     */
    List<CashRecordVO> getFundDetailListByShopExport(
            @Param("userList") List<String> userList,
            @Param("operatorUserPhone") String operatorUserPhone,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime,
            @Param("phone") String phone,
            @Param("status") String status);


}
