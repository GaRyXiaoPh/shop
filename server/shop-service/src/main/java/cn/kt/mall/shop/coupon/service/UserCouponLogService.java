package cn.kt.mall.shop.coupon.service;

import cn.kt.mall.shop.coupon.entity.UserCouponLogEntity;
import cn.kt.mall.shop.coupon.repository.UserCouponLogEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;

@Service
public class UserCouponLogService {

    @Autowired
    private UserCouponLogEntityMapper userCouponLogEntityMapper;

    public int addUserCouponLogEntity(UserCouponLogEntity userCouponLogEntity){
       return userCouponLogEntityMapper.insertSelective(userCouponLogEntity);

    }

    public int uodateUserCouponLogEntity(UserCouponLogEntity userCouponLogEntity){
        return  userCouponLogEntityMapper.updateByPrimaryKeySelective(userCouponLogEntity);
    }


}
