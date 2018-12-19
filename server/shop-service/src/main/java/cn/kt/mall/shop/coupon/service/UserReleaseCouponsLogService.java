package cn.kt.mall.shop.coupon.service;

import cn.kt.mall.shop.coupon.entity.UserCouponLogEntity;
import cn.kt.mall.shop.coupon.entity.UserReleaseCouponLogEntity;
import cn.kt.mall.shop.coupon.mapper.UserReturnCouponLogDAO;
import cn.kt.mall.shop.coupon.repository.UserCouponLogPatchMapper;
import cn.kt.mall.shop.coupon.repository.UserReleaseCouponLogEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserReleaseCouponsLogService {
    @Autowired
    private UserReleaseCouponLogEntityMapper releaseCouponLogEntityMapper;
    @Autowired
    private UserReturnCouponLogDAO userReturnCouponLogDAO;
    @Autowired
    private UserCouponLogPatchMapper userCouponLogPatchMapper;

    public int addUserReleaseCouponLog(UserReleaseCouponLogEntity entity){
        return  releaseCouponLogEntityMapper.addUserReleaseCouponLogEntity(entity);
    }
    public List<UserReleaseCouponLogEntity> getUserReleaseCouponLogEntityList(String userId , String couponLogId ){
        return  userReturnCouponLogDAO.getUserReleaseCouponLogEntityList(userId,couponLogId);
    }

    public int addPatchUserReleaseCouponLogEntity(List<UserReleaseCouponLogEntity> list){
        return releaseCouponLogEntityMapper.addPatchUserReleaseCouponLogEntity(list);
    }

    public int updatePatchUserCouponLog(List<UserCouponLogEntity> list){
        return userCouponLogPatchMapper.updatePatchuserCouponLogList(list);
    }
}
