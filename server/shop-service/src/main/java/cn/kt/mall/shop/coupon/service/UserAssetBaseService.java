package cn.kt.mall.shop.coupon.service;

import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.shop.coupon.entity.UserAssetBaseEntity;
import cn.kt.mall.shop.coupon.entity.UserReturnCouponLogEntity;
import cn.kt.mall.shop.coupon.mapper.UserAssetBaseDAO;
import cn.kt.mall.shop.coupon.mapper.UserReturnCouponLogDAO;
import cn.kt.mall.shop.coupon.vo.UserAssetBaseSearchVO;
import cn.kt.mall.shop.coupon.vo.UserAssetEntityVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserAssetBaseService {
    @Autowired
    private UserAssetBaseDAO userAssetBaseDAO;

   public List<UserAssetBaseEntity> getUserAssetBaseLsitByassetType(UserAssetBaseSearchVO searchVo){
       return userAssetBaseDAO.getUserAssetBaseLsitByassetType(searchVo);
   }


    //根据Id修改日志表
    public int updateUserAssetBaseLsitById( String userId,Integer currentReleaseNum,
                                      Integer sendFinish,BigDecimal afterAmount){

        UserAssetBaseEntity userAssetBaseEntity = new UserAssetBaseEntity();
        userAssetBaseEntity.setAfterAmount(afterAmount);
        userAssetBaseEntity.setUserId(userId);
        userAssetBaseEntity.setCurrentReleseNum(currentReleaseNum);
        userAssetBaseEntity.setSendFinish(sendFinish);
       return userAssetBaseDAO.updateUserAssetBaseByUserId(userAssetBaseEntity);
    }

    public int updatePatchUserAssetBase(List<UserAssetBaseEntity> list){
        return userAssetBaseDAO.updatePatchUserAssetBase(list);
    }

    public int addPatchStatementEntity(List<StatementEntity> list){
        return userAssetBaseDAO.addPatchStatementEntity(list);
    }

    public int updatePatchAssetByUserIdAndMoneny(List<UserAssetEntityVO> list){
        return userAssetBaseDAO.updatePatchAssetByUserIdAndMoneny(list);
    }


    /**
     * 根据优惠劵类型获取该类型的总数量
     */
    public int  getUserAssetBaseCountByAssetType(UserAssetBaseSearchVO searchVO){
        return userAssetBaseDAO.getUserAssetBaseCountByAssetType(searchVO);
    }

}
