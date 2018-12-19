package cn.kt.mall.task.service;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.mapper.UserAssetDAO;
import cn.kt.mall.shop.coupon.entity.UserCouponEntity;

import cn.kt.mall.shop.coupon.service.UserCouponEntityService;
import cn.kt.mall.shop.coupon.vo.UserAssertAndStateMentVO;
import cn.kt.mall.shop.coupon.vo.UserAssetEntityVO;
import cn.kt.mall.task.ReleaseTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PatchService {

    @Autowired
    UserAssetDAO userAssetDAO;

    @Autowired
    UserCouponEntityService userCouponEntityService;


    private static Map<String,UserAssetEntity> userPopcAssetMap = new HashMap<>();

    private static Map<String,Map<String,UserCouponEntity>> userCouponMap = new HashMap<>();

   // private static Map<String,User>

    @Transactional
    public UserAssertAndStateMentVO verifyUserAsset(String userId, String currency,
                                                    BigDecimal deltaAvailableBalance,
                                                    BigDecimal deltaReservedBalance,
                                                    TradeType tradeType, String referenceId) {

        UserAssertAndStateMentVO vo = new UserAssertAndStateMentVO();

        UserAssetEntity userAssetEntity = getUserAssetEntityByUserId(userId);
        if(userAssetEntity==null){
            return  null;
        }
        UserAssetEntityVO userAssetEntityVO = new UserAssetEntityVO();
        userAssetEntityVO.setUserId(userId);
        userAssetEntityVO.setCurrencyType(currency);
        userAssetEntityVO.setReleaseNum(deltaAvailableBalance);

        UserAssetEntity assetNew = new UserAssetEntity();
        assetNew.setUserId(userId);
        assetNew.setCurrency(currency);

        if (deltaReservedBalance != null && deltaReservedBalance.compareTo(BigDecimal.ZERO) != 0) {
            assetNew.setReservedBalance(userAssetEntity.getReservedBalance().add(deltaReservedBalance));
            if(assetNew.getReservedBalance().compareTo(BigDecimal.ZERO)< 0){
                return null;
            }
        }

        if (deltaAvailableBalance != null && deltaAvailableBalance.compareTo(BigDecimal.ZERO) != 0) {
            assetNew.setAvailableBalance(userAssetEntity.getAvailableBalance().add(deltaAvailableBalance));
            if(assetNew.getAvailableBalance().compareTo(BigDecimal.ZERO) < 0){
                return null;
            }
        }

        //纪录流水
        StatementEntity statementEntity = new StatementEntity(userId, currency,
                userAssetEntity.getAvailableBalance(),
                assetNew.getAvailableBalance(),
                deltaAvailableBalance,
                userAssetEntity.getReservedBalance(),
                assetNew.getReservedBalance(),
                deltaReservedBalance, tradeType.getType(), referenceId);

        if (deltaReservedBalance == null || deltaReservedBalance.compareTo(BigDecimal.ZERO) == 0) {
            statementEntity.setReservedAfter(statementEntity.getReservedBefore());
            statementEntity.setReservedChange(new BigDecimal("0"));

        }

        if (deltaAvailableBalance == null || deltaAvailableBalance.compareTo(BigDecimal.ZERO) == 0) {
            statementEntity.setAvailableAfter(statementEntity.getAvailableBefore());
            statementEntity.setAvailableChange(new BigDecimal("0"));
        }
        vo.setUserAssetEntityVO(userAssetEntityVO);
        vo.setStatementEntity(statementEntity);
        return vo;
    }


    public  void initUserAssetBaseList(){

    }



    /**
     * 初始popc化数据
     */
    public  void initUserPopcAssetMap(){

        List<UserAssetEntity> userAssetList = userAssetDAO.getAssetAllByCurrency("popc");

          for(UserAssetEntity data:userAssetList){
              userPopcAssetMap.put(data.getUserId(),data);
          }
    }

    /**
     * 初始化回归数据清空
     */
    public static void removeUserPopcAssetMap(){
        if(userPopcAssetMap!=null&&userPopcAssetMap.size()>0){
            userPopcAssetMap = new HashMap<>();
        }
    }


    public UserAssetEntity getUserAssetEntityByUserId(String userId) {
        if (userPopcAssetMap != null && userPopcAssetMap.size() > 0) {
            return userPopcAssetMap.get(userId);
        }else{
            return null;
        }
    }

    /**
     * 初始化玩家各种优惠劵数据到缓存
     */
    public  void initUserCouponEntity(){
        System.out.println("11111111111111111111111111");
       List<UserCouponEntity> userCouponEntityList =userCouponEntityService.getUserCouponEntityUserIdAndTypeList();
       System.out.println("222222222222222222"+userCouponEntityList.size());
        if(userCouponEntityList!=null&&userCouponEntityList.size()>0){

            for(UserCouponEntity data:userCouponEntityList){

               if(userCouponMap.containsKey(data.getUserId())){
                   Map<String,UserCouponEntity> userCouponEntityMap = userCouponMap.get(data.getUserId());
                   userCouponEntityMap.put(data.getCouponId(),data);
                   userCouponMap.put(data.getUserId(),userCouponEntityMap);
               }else{
                   Map<String,UserCouponEntity> userCouponEntityMap = new HashMap<>();
                   userCouponEntityMap.put(data.getCouponId(),data);
                   userCouponMap.put(data.getUserId(),userCouponEntityMap);

               }
            }
        }
    }


    /**
     * 根据用户Id和优惠券Id获取玩家该优惠劵的所有数据
     */
    public UserCouponEntity getUserCouponEntityByUserIpAndCouponId(String userId,String couponId){
        if(userCouponMap!=null&&userCouponMap.size()>0){
            if(userCouponMap.get(userId)!=null&&userCouponMap.get(userId).size()>0) {
                return userCouponMap.get(userId).get(couponId);
            }
        }else{
            return null;
        }
        return null;
    }

    public void removeCouponMap(){
        userCouponMap = new HashMap<>();
    }

}
