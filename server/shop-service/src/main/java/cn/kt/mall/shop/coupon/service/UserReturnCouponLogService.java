package cn.kt.mall.shop.coupon.service;

import cn.kt.mall.shop.coupon.entity.UserReturnCouponLogEntity;
import cn.kt.mall.shop.coupon.mapper.UserReturnCouponLogDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserReturnCouponLogService {
    @Autowired
    private UserReturnCouponLogDAO userReturnCouponLogDAO;

    /**
     * 获取所有未发送完的用玩家赠送信息日志记录表
     * @return
     */
    public List<UserReturnCouponLogEntity> getUserReturnCouponLogList(){
        return userReturnCouponLogDAO.getUserReturnCouponLogList();
    }


    public int updateUserReturnCouponLogEntity(String id, Integer currentReleaseNum,
                                               Integer sendFinish, BigDecimal afterAmount){
        return userReturnCouponLogDAO.updateUserReturnCouponLogById(id,currentReleaseNum,sendFinish,afterAmount);
    }

    public UserReturnCouponLogEntity getUserReturnCouponLog(String userId,String tradeId){
        return userReturnCouponLogDAO.getUserReturnCouponLog(userId,tradeId);
    }

    public int updatePatchUserReturnCouponLogEntity(List<UserReturnCouponLogEntity> list){
        return userReturnCouponLogDAO.updatePatchUserReturnCouponLog(list);
    }

}
