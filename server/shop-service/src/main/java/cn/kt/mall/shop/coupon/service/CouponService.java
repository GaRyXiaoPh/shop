package cn.kt.mall.shop.coupon.service;

import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.enums.AssetType;
import cn.kt.mall.common.wallet.mapper.StatementDAO;
import cn.kt.mall.common.wallet.mapper.UserAssetDAO;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.shop.coupon.entity.*;
import cn.kt.mall.shop.coupon.mapper.CouponDAO;
import cn.kt.mall.shop.coupon.mapper.CouponsDAO;
import cn.kt.mall.shop.coupon.mapper.UserReturnCouponLogDAO;
import cn.kt.mall.shop.coupon.vo.CouponVO;
import cn.kt.mall.shop.trade.entity.TradeEntity;
import cn.kt.mall.shop.trade.mapper.TradeDAO;
import cn.kt.mall.shop.trade.vo.TradeVO;
import com.github.pagehelper.PageInfo;
import io.shardingjdbc.core.api.HintManager;
import org.apache.commons.collections.ArrayStack;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CouponService {
    @Autowired
    private CouponDAO couponDAO;
    @Autowired
    private CouponsDAO couponsDAO;
    @Autowired
    private UserAssetService userAssetService;
    @Autowired
    private TradeDAO tradeDAO;
    @Autowired
    private UserReleaseCouponsLogService userReleaseCouponsLogService;
    @Autowired
    private UserReturnCouponLogService userReturnCouponLogService;

    @Autowired
    UserAssetDAO userAssetDAO;

    @Autowired
    UserReturnCouponLogDAO userReturnCouponLogDAO;
    @Autowired
    StatementDAO statementDAO;
    /**
     * 查询全部优惠卷
     *
     * @return
     */
    public CouponEntity getCouponList() {
        return couponDAO.getCouponList();
    }

    /**
     * 查询我的优惠券
     *
     * @return
     */
    public List<CouponEntity> getUserCouponList(String userId) {
        List<CouponEntity>CouponEntityList =  couponsDAO.getSysCouponsList();
        List<CouponVO> userCouponVOList = couponsDAO.getUserCouponVOList(userId);
        //用户初始化优惠券
        UserAssetEntity userAssetEntityBase = userAssetDAO.getAssetBaseByUserIdAndCurrency(userId,"popc");
        //用户初始化优惠券释放总数量
        //UserAssetEntity userAssetEntityBaseReleaseAmount = statementDAO.getAssetEntityBaseReleaseAmount(userId);
        //用户初始化彩票积分
        UserAssetEntity useCouponEntityBase = userAssetDAO.getAssetBaseByUserIdAndCurrency(userId,"coupon");
        //用户初始化彩票积分释放总数量
        //UserAssetEntity useCouponEntityReleaseAmount = userReturnCouponLogDAO.getUserReleaseBaseAmount(userId);
        UserAssetEntity userAssetEntity = userAssetService.getUserAssetByCurrency(userId, AssetType.POPC.getStrType());
        BigDecimal availableBalance = new BigDecimal(0);
        BigDecimal reservedBalance = new BigDecimal(0);
        for(CouponEntity couponEntity : CouponEntityList){
            couponEntity.setCouponNum(new BigDecimal(0));
           if(userCouponVOList.size() == 0){
                if(couponEntity.getCouponName().equals("优惠券")){
                    reservedBalance = userAssetEntity.getReservedBalance();
                    availableBalance = userAssetEntity.getAvailableBalance().setScale(4, RoundingMode.HALF_UP);
                    couponEntity.setCouponNum(availableBalance);
                    couponEntity.setReservedCouponNum(reservedBalance);
                    couponEntity.setUserId(userId);
                    if(userAssetEntityBase == null){
                        couponEntity.setReservedBalanceBase(new BigDecimal(0));
                        couponEntity.setReleaseBalanceBase(new BigDecimal(0));
                    }else{
                        couponEntity.setReservedBalanceBase((userAssetEntityBase.getReservedBalanceBase().subtract(userAssetEntityBase.getAfterAmount())).setScale(4, RoundingMode.HALF_UP));
                        couponEntity.setReleaseBalanceBase(userAssetEntityBase.getAfterAmount().setScale(4, RoundingMode.HALF_UP));
                        couponEntity.setCreateTimeBase(userAssetEntityBase.getCreateTime());
                    }
                    //break;
                }else if(couponEntity.getCouponName().equals("彩票积分")){
                   if(useCouponEntityBase == null){
                       couponEntity.setReservedBalanceBase(new BigDecimal(0));
                       couponEntity.setReleaseBalanceBase(new BigDecimal(0));
                   }else{
                       couponEntity.setReservedBalanceBase((useCouponEntityBase.getReservedBalanceBase().subtract(useCouponEntityBase.getAfterAmount())).setScale(4, RoundingMode.HALF_UP));
                       couponEntity.setReleaseBalanceBase(useCouponEntityBase.getAfterAmount().setScale(4, RoundingMode.HALF_UP));
                       couponEntity.setCreateTimeBase(useCouponEntityBase.getCreateTime());
                   }
                   break;
               }
            }else{
                for(CouponVO couponVO : userCouponVOList){
                    if(couponEntity.getCouponName().equals("优惠券")){
                        reservedBalance = userAssetEntity.getReservedBalance();
                        availableBalance = userAssetEntity.getAvailableBalance().setScale(4, RoundingMode.HALF_UP);
                        couponEntity.setCouponNum(availableBalance);
                        couponEntity.setReservedCouponNum(reservedBalance);
                        couponEntity.setUserId(userId);
                        couponEntity.setUserCouponId(couponVO.getId());
                        if(userAssetEntityBase == null){
                            couponEntity.setReservedBalanceBase(new BigDecimal(0));
                            couponEntity.setReleaseBalanceBase(new BigDecimal(0));
                        }else{
                            //couponEntity.setReservedBalanceBase(userAssetEntityBase.getReservedBalanceBase().subtract(userAssetEntityBase.getAfterAmount()));
                            //couponEntity.setReleaseBalanceBase(userAssetEntityBase.getAfterAmount());
                            couponEntity.setReservedBalanceBase((userAssetEntityBase.getReservedBalanceBase().subtract(userAssetEntityBase.getAfterAmount())).setScale(4, RoundingMode.HALF_UP));
                            couponEntity.setReleaseBalanceBase(userAssetEntityBase.getAfterAmount().setScale(4, RoundingMode.HALF_UP));
                            couponEntity.setCreateTimeBase(userAssetEntityBase.getCreateTime());
                        }
                        break;
                    }else if(couponEntity.getCouponName().equals("彩票积分") && couponEntity.getId().equals(couponVO.getCouponId())){
                        couponEntity.setCouponNum(couponVO.getCouponNum().setScale(4, RoundingMode.HALF_UP));
                        couponEntity.setCdkeyNum(couponVO.getCdkeyNum());
                        couponEntity.setReservedCouponNum(couponVO.getReservedCouponNum());
                        couponEntity.setUserId(userId);
                        couponEntity.setUserCouponId(couponVO.getId());
                        if(useCouponEntityBase == null){
                            couponEntity.setReservedBalanceBase(new BigDecimal(0));
                            couponEntity.setReleaseBalanceBase(new BigDecimal(0));
                        }else{
                            //couponEntity.setReservedBalanceBase(useCouponEntityBase.getReservedBalanceBase().subtract(useCouponEntityBase.getAfterAmount()));
                            //couponEntity.setReleaseBalanceBase(useCouponEntityBase.getAfterAmount());
                            couponEntity.setReservedBalanceBase((useCouponEntityBase.getReservedBalanceBase().subtract(useCouponEntityBase.getAfterAmount())).setScale(4, RoundingMode.HALF_UP));
                            couponEntity.setReleaseBalanceBase(useCouponEntityBase.getAfterAmount().setScale(4, RoundingMode.HALF_UP));
                            couponEntity.setCreateTimeBase(useCouponEntityBase.getCreateTime());
                        }
                        break;
                    }else{
                        if(couponEntity.getId().equals(couponVO.getCouponId())){
                            couponEntity.setCouponNum(couponVO.getCouponNum());
                            couponEntity.setCdkeyNum(couponVO.getCdkeyNum());
                            couponEntity.setReservedCouponNum(couponVO.getReservedCouponNum());
                            couponEntity.setUserId(userId);
                            couponEntity.setUserCouponId(couponVO.getId());
                            break;
                        }
                    }

                }
            }

        }
        return CouponEntityList;
    }
    /**
     * 优惠券详情(不包括优惠券)
     *
     * @return
     */
    public CommonPageVO<UserCouponLogEntity>getUserCouponDetailList(String userId, String couponId, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<UserCouponLogEntity> list = couponsDAO.getUsercouponLogDetailList(userId,couponId,rowBounds);
        PageInfo<UserCouponLogEntity> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }
    /**
     * 优惠券详情明细(不包括优惠券)
     *
     * @return
     */
    public TradeEntity getUserCouponDetailListMessage(String userId, String tradeId, String couponLogId) {
        TradeEntity tradeEntity = tradeDAO.getTradeById(userId,tradeId);
        List<UserReleaseCouponLogEntity> userReleaseCouponLogEntityList = new ArrayList<>();
        userReleaseCouponLogEntityList = userReleaseCouponsLogService.getUserReleaseCouponLogEntityList(userId,couponLogId);
        tradeEntity.setUerReleaseCouponLogEntityList(userReleaseCouponLogEntityList);
        return tradeEntity;
    }
    /**
     * 初始化彩票积分释放记录
     *
     * @return
     */
    public List<UserReleaseCouponLogEntity> getUserCouponDetailListBaseMessageList(String userId) {
        List<UserReleaseCouponLogEntity> userReleaseCouponLogEntityList = new ArrayList<>();
        userReleaseCouponLogEntityList = userReturnCouponLogDAO.getUserReleaseCouponLogEntityBaseList(userId);
        return userReleaseCouponLogEntityList;
    }
   /**
     * 优惠券赠送（消费，推荐）明细详情
     *
     * @return
    */

    public TradeEntity getUserCouponDetailListMessageAsset(String userId, String tradeId,String tradeType,String coupoonId,String goodId) {

        CouponEntity couponEntity = couponsDAO.getCouponById(coupoonId);
        TradeEntity tradeEntity = new TradeEntity();
        List<UserReleaseCouponLogEntity> userReleaseCouponLogEntityList = new ArrayList<>();
        if(tradeType.equals("11")){//消费赠送
            tradeEntity = tradeDAO.getTradeById(userId,tradeId);
            UserCouponLogEntity userCouponLogEntity = couponsDAO.getUsercouponLog(userId,couponEntity.getId(),tradeId,goodId);
            if(userCouponLogEntity != null){
                userReleaseCouponLogEntityList = userReleaseCouponsLogService.getUserReleaseCouponLogEntityList(userId,userCouponLogEntity.getId());
                tradeEntity.setUerReleaseCouponLogEntityList(userReleaseCouponLogEntityList);
            }

        }else{//推荐赠送
            tradeEntity =  tradeDAO.getTradeById(null,tradeId);
            UserReturnCouponLogEntity userReturnCouponLogEntity = userReturnCouponLogService.getUserReturnCouponLog(userId,tradeId);
            if(userReturnCouponLogEntity != null){
                userReleaseCouponLogEntityList = userReleaseCouponsLogService.getUserReleaseCouponLogEntityList(userId,userReturnCouponLogEntity.getId());
                tradeEntity.setUerReleaseCouponLogEntityList(userReleaseCouponLogEntityList);
            }

        }

        return tradeEntity;
    }


    /**
     * 查询优惠卷是否存在
     */

    public CouponEntity getCouponEntityById(String id) {
        return couponDAO.getCouponEntityById(id);
    }
    /**
     * 查询我的指定优惠卷
     *
     * @return
     */
    public UserCouponEntity getUserCouponEntity(String userId,String couponId) {
        try (HintManager hintManager = HintManager.getInstance()) {
            //强制走主库
            hintManager.setMasterRouteOnly();
            return couponsDAO.getUserCouponEntity(userId, couponId);
        }
    }
    /**
     * 查询指定优惠卷
     *
     * @return
     */
    public CouponEntity getCouponById(String couponId) {
        return couponsDAO.getCouponById( couponId);
    }
}
