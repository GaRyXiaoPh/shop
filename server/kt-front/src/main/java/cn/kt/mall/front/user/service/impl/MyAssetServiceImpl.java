package cn.kt.mall.front.user.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.user.model.UserInfo;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.enums.AssetType;
import cn.kt.mall.common.wallet.mapper.StatementDAO;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.front.password.service.TransactionPasswordService;
import cn.kt.mall.front.user.mapper.MyAssetMapper;
import cn.kt.mall.front.user.service.MyAssetService;
import cn.kt.mall.front.user.service.UserService;
import cn.kt.mall.front.user.vo.MyAssetInfoVO;
import cn.kt.mall.front.user.vo.MyAssetVO;
import cn.kt.mall.shop.cash.service.CashService;
import cn.kt.mall.shop.coupon.entity.CouponEntity;
import cn.kt.mall.shop.coupon.entity.UserCouponEntity;
import cn.kt.mall.shop.coupon.mapper.CouponsDAO;
import cn.kt.mall.shop.coupon.service.CouponService;
import cn.kt.mall.shop.coupon.service.UserCouponEntityService;
import cn.kt.mall.shop.coupon.vo.CouponTransferVO;
import cn.kt.mall.shop.coupon.vo.ExtractVO;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.mapper.ShopDAO;
import com.github.pagehelper.PageInfo;
import io.shardingjdbc.core.api.HintManager;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的资产service实现类
 * @author gwj
 */
@Service("myAssetService")
public class MyAssetServiceImpl implements MyAssetService {

    @Autowired
    private MyAssetMapper myAssetMapper;
    @Autowired
    private UserAssetService userAssetService;
    @Autowired
    private CashService cashService;
    @Autowired
    private SysSettingsService sysSettingsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserCouponEntityService userCouponEntityService;
    @Autowired
    private CouponService couponService;
    @Autowired
    TransactionPasswordService transactionPasswordService;
    @Autowired
    private CouponsDAO couponsDAO;
    @Autowired
    private ShopDAO shopDAO;
    @Autowired
    private StatementDAO statementDAO;


    /**
     * 查询我的资产（现金+popc）
     * @param userId
     * @return
     */
    public MyAssetVO queryMyAsset(String userId) {
        MyAssetVO reqMyAssetVO = new MyAssetVO();
        List<MyAssetVO> list = myAssetMapper.queryMyAsset(userId);
        reqMyAssetVO.setUserId(userId);
        // popc和cny的比例，需要等凯撒接口开发完成后才能
        reqMyAssetVO.setPopcForCny(sysSettingsService.getCurrencyPrice("popc") + "");
        if(list != null || list.size() > 0) {
            for(MyAssetVO myAssetVO : list) {
                if(myAssetVO.getCurrency().equals("point")) {
                    // 现金
                    double availableBalance = myAssetVO.getAvailableBalance();
                    double reservedBalance = myAssetVO.getReservedBalance();
                    reqMyAssetVO.setAvailableBalance(availableBalance);
                    reqMyAssetVO.setReservedBalance(reservedBalance);
                    reqMyAssetVO.setTotlePoint(sum(availableBalance ,reservedBalance));
                    continue;
                } else if(myAssetVO.getCurrency().equals("popc")) {
                    // popc
                    double availablePOPC = myAssetVO.getAvailableBalance();
                    double reservedPOPC = myAssetVO.getReservedBalance();
                    reqMyAssetVO.setAvailablePOPC(availablePOPC);
                    reqMyAssetVO.setReservedPOPC(reservedPOPC);
                    reqMyAssetVO.setTotlePOPC(sum(availablePOPC ,reservedPOPC));
                    continue;
                }
             }
        }
        return reqMyAssetVO;
    }
    /**
     * 查询我的余额
     * @param userId
     * @return
     */
    public UserAssetEntity getMyAsset(String userId) {
        UserAssetEntity userAssetEntity = myAssetMapper.getMyAsset(userId);
        if(userAssetEntity != null){
            List<StatementEntity> myAssetStatementList = statementDAO.getMyAssetStatementList(userId);
            userAssetEntity.setStatementEntityList(myAssetStatementList);
        }
        return userAssetEntity;
    } /**
     * 查询我的优惠券资产
     * @param userId
     * @return
     */
    public UserAssetEntity getMyAssetPopc(String userId) {
        UserAssetEntity userAssetEntity = myAssetMapper.getMyAssetPopc(userId);
        return userAssetEntity;
    }
    /**
     * 申请提币
     * @param userId
     * @param count 提币数量
     */
    public void applyPopcTurnOut(String userId,double count) {
        A.check(count <= 0,"请输入有效的数字");
        // 插入提币记录
        String cashId = cashService.addCash(userId, new BigDecimal(count));
        // 处理用户可用popc和冻结popc数量
        userAssetService.updateUserAsset(userId,"popc",new BigDecimal("-"+count),new BigDecimal(count),TradeType.WITHDRAWAL, cashId);
    }

    /**
     * 查询我的资产明细记录
     * @param userId
     * @param type 0消费,1提币,2充值,3解冻
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageVO<MyAssetInfoVO> queryMyAssetList(String userId, int type, Integer pageNo, Integer pageSize) {
        List<MyAssetInfoVO> list = new ArrayList<MyAssetInfoVO>();
        int count = 0;
        int srcPageNo = pageNo;
        if ( pageNo > 0 ) {
            pageNo = pageNo-1;
        }
        int offset = pageNo * pageSize;
        switch (type) {
            case 0:
                // 消费
                count = myAssetMapper.queryDealCount(userId);
                list = myAssetMapper.queryDealByUserID(userId, offset, pageSize);
                break;
            case 1:
                // 提币
                count = myAssetMapper.queryCashCount(userId);
                list = myAssetMapper.queryCashByUserID(userId, offset, pageSize);
                break;
            case 2:
                // 充值&扣除
                count = myAssetMapper.queryRechargeCount(userId);
                list = myAssetMapper.queryRechargeByUserID(userId, offset, pageSize);
                break;
            case 3:
                // 解冻
                count = myAssetMapper.queryUnfreezeCount(userId);
                list = myAssetMapper.queryUnfreezeByUserID(userId, offset, pageSize);
                break;
            default:
                break;
        }
        return new PageVO<>(srcPageNo, pageSize, count, list);
    }

    /**
     * double 相加方法，处理精度问题
     * @param d1
    tb_user_recharge_log
     * @param d2
     * @return
     */
    private double sum(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }
   @Transactional
   public void transferMyAsset( BigDecimal transferNumber,String mobile, String couponId, String password,String useType){
       List<SysSettings> sysSettingsList =  sysSettingsService.getTranferConfig();
       CouponEntity couponEntity = couponsDAO.getCouponById( couponId);
       BigDecimal insideRatio = new BigDecimal(0);//站内转费率
       BigDecimal outsideRatio = new BigDecimal(0);//站外转费率
       BigDecimal couponExtract = new BigDecimal(0);//使用费率
       BigDecimal transferNumberTotal = new BigDecimal(0);//转让总数包括手续费
       BigDecimal transferRatio = new BigDecimal(0);//转让费率
       BigDecimal transferFee = new BigDecimal(0);//转让手续费
       BigDecimal couponTtransferPointRatio = new BigDecimal(0);//彩票积分转余额比率
       String relation = "";//转让关系
       for(SysSettings sysSettings: sysSettingsList){
           if(sysSettings.getCode().equals("inside_ratio")){
               insideRatio = new BigDecimal(sysSettings.getLabel());
           }else if(sysSettings.getCode().equals("outside_ratio")){
               outsideRatio =  new BigDecimal(sysSettings.getLabel());
           }else if(sysSettings.getCode().equals("coupon_extract")){
               couponExtract = new BigDecimal(sysSettings.getLabel());
           }else if(sysSettings.getCode().equals("coupon_transfer_point_ratio")){
               couponTtransferPointRatio = new BigDecimal(sysSettings.getLabel());
           }
       }
       UserInfo myUserInfo  = userService.getUserByUserId(SubjectUtil.getCurrent().getId());
       UserInfo userInfo = userService.getUserByUsername(mobile);
       A.check(myUserInfo == null,"当前登录账户不存在");
       A.check(couponEntity == null,"该类型优惠券不存在");
       if(couponEntity.getCouponName().equals("优惠券")){
           A.check(userInfo == null,"到帐帐户不存在");
           A.check(userInfo.getId().equals(SubjectUtil.getCurrent().getId()),"不能转给自己");
           if(myUserInfo.getPid().equals(userInfo.getPid())){//同站
               transferNumberTotal = transferNumber.add(transferNumber.multiply(insideRatio).divide(new BigDecimal(100)));
               relation = "1";
               transferRatio = insideRatio;
               transferFee = transferNumber.multiply(insideRatio).divide(new BigDecimal(100));
           }else if(myUserInfo.getPid().equals(userInfo.getId())){//同站
               transferNumberTotal = transferNumber.add(transferNumber.multiply(insideRatio).divide(new BigDecimal(100)));
               relation = "1";
               transferRatio = insideRatio;
               transferFee = transferNumber.multiply(insideRatio).divide(new BigDecimal(100));
           }else if(myUserInfo.getId().equals(userInfo.getPid())){//同站
               transferNumberTotal = transferNumber.add(transferNumber.multiply(insideRatio).divide(new BigDecimal(100)));
               relation = "1";
               transferRatio = insideRatio;
               transferFee = transferNumber.multiply(insideRatio).divide(new BigDecimal(100));
           }else{//异站
               transferNumberTotal = transferNumber.add(transferNumber.multiply(outsideRatio).divide(new BigDecimal(100)));
               relation = "2";
               transferRatio = outsideRatio;
               transferFee = transferNumber.multiply(outsideRatio).divide(new BigDecimal(100));
           }
        /*   if(myUserInfo){//外站
               transferNumberTotal = transferNumber.add(transferNumber.multiply(outsideRatio).divide(new BigDecimal(100)));
               relation = "2";
               transferRatio = outsideRatio;
               transferFee = transferNumber.multiply(outsideRatio).divide(new BigDecimal(100));
           }else if(!userInfo.getStandNo().equals(myUserInfo.getStandNo())){//外站
               transferNumberTotal = transferNumber.add(transferNumber.multiply(outsideRatio).divide(new BigDecimal(100)));
               relation = "2";
               transferRatio = outsideRatio;
               transferFee = transferNumber.multiply(outsideRatio).divide(new BigDecimal(100));
           }else if(userInfo.getStandNo().equals(myUserInfo.getStandNo())){//内战
               transferNumberTotal = transferNumber.add(transferNumber.multiply(insideRatio).divide(new BigDecimal(100)));
               relation = "1";
               transferRatio = insideRatio;
               transferFee = transferNumber.multiply(insideRatio).divide(new BigDecimal(100));
           }*/
           A.check(userAssetService.getUserAssetByCurrency(SubjectUtil.getCurrent().getId(), AssetType.POPC.getStrType()).getAvailableBalance().compareTo(transferNumberTotal) < 0, "账户优惠券不足" + ":" + transferNumberTotal);
           //减少转出人相应优惠券
           userAssetService.updateUserAsset( SubjectUtil.getCurrent().getId(), AssetType.POPC.getStrType(), transferNumberTotal.negate(), new BigDecimal(0), TradeType.TRANSFEROUT, userInfo.getId());
           //增加转入人相应优惠券
           userAssetService.updateUserAsset(userInfo.getId(), AssetType.POPC.getStrType(), transferNumber, new BigDecimal(0), TradeType.TRANSFERIN, SubjectUtil.getCurrent().getId());
           CouponTransferVO ouponTransferVO = new CouponTransferVO();
           ouponTransferVO.setId(IDUtil.getUUID());
           ouponTransferVO.setAmount(transferNumber);
           ouponTransferVO.setFeeRatio(transferRatio);
           ouponTransferVO.setRelation(relation);
           ouponTransferVO.setRollInUserId(userInfo.getId());
           ouponTransferVO.setCouponId(couponId);
           ouponTransferVO.setRollOutUserId(myUserInfo.getId());
           ouponTransferVO.setFee(transferFee);
           int row = userCouponEntityService.inserCouponTransfer(ouponTransferVO);
           A.check(row < 0, "优惠券转让失败请重试" + ":" + transferNumberTotal);
       }else{
           UserCouponEntity assetNew = new UserCouponEntity();
           if(couponEntity.getCouponName().equals("彩票积分")){
               //https://github.com/sharding-sphere/sharding-sphere/issues/767 暂时不支持for update走主库
               //HintManager 会自动关闭
               try (HintManager hintManager = HintManager.getInstance()) {
                   //强制走主库
                   hintManager.setMasterRouteOnly();
                   UserCouponEntity userCouponEntity = couponsDAO.getUserCouponEntity(SubjectUtil.getCurrent().getId(), couponId);
                   assetNew.setCouponNum(userCouponEntity.getCouponNum());
                   A.check(userCouponEntity == null, "用户无彩票积分");
                   userCouponEntity.setUserId(myUserInfo.getId());
                   userCouponEntity.setCouponId(couponId);
                   if (useType.equals("1")) {//彩票积分使用
                       transferNumberTotal = transferNumber.add(transferNumber.multiply(couponExtract).divide(new BigDecimal(100)));
                       A.check(userCouponEntity.getCouponNum().compareTo(transferNumberTotal) < 0, "彩票积分不足" + ":" + transferNumberTotal);
                       userCouponEntity.setCouponNum(transferNumberTotal);
                       int userCouponEntityRow = couponsDAO.updateUserCouponBySend(userCouponEntity);
                       A.check(userCouponEntityRow < 1, "扣除彩票积分失败" + ":" + transferNumberTotal);
                   } else {//彩票积分转余额
                       A.check(userCouponEntity.getCouponNum().compareTo(transferNumber) < 0, "彩票积分不足" + ":" + transferNumber);
                       //A.check(userInfo == null,"到帐帐户不存在");
                       userCouponEntity.setCouponNum(transferNumber);
                       //增加转入人相应优惠券
                       int userCouponEntityRow = couponsDAO.updateUserCouponBySend(userCouponEntity);
                       A.check(userCouponEntityRow < 1, "扣除彩票积分失败" + ":" + transferNumber);
                   }
                   //int userCouponEntityRow = couponsDAO.updateUserCouponBySend(userCouponEntity);
                   //A.check(userCouponEntityRow < 1, "扣除彩票积分失败" + ":" + transferNumberTotal);
               }
               if(useType.equals("2")){
                   userAssetService.updateUserAsset(SubjectUtil.getCurrent().getId(), AssetType.CREDIT.getStrType(), transferNumber.multiply(couponTtransferPointRatio).divide(new BigDecimal(100)), new BigDecimal(0), TradeType.COUPON_TRANSFER_POINT, SubjectUtil.getCurrent().getId());
               }
               ExtractVO extractVO = new ExtractVO();
               extractVO.setId(IDUtil.getUUID());
               extractVO.setAmount(transferNumber);
               extractVO.setAmountBefore(assetNew.getCouponNum());
               extractVO.setCouponId(couponId);
               extractVO.setUserId(myUserInfo.getId());
               if(useType.equals("1")){
                   extractVO.setAmountAfter(assetNew.getCouponNum().subtract(transferNumberTotal));
                   extractVO.setRatio(couponExtract);
                   extractVO.setPoundage(transferNumber.multiply(couponExtract).divide(new BigDecimal(100)));
                   extractVO.setArriveAccount(mobile);
                   extractVO.setStatus("0");
                   extractVO.setOperateType(1);
               }else{
                   extractVO.setAmountAfter(assetNew.getCouponNum().subtract(transferNumber));
                   extractVO.setRatio(couponTtransferPointRatio);
                   extractVO.setArriveAccount(SubjectUtil.getCurrent().getId());
                   extractVO.setPoundage(new BigDecimal(0));
                   extractVO.setStatus("1");
                   extractVO.setOperateType(2);
               }
              /* extractVO.setId(IDUtil.getUUID());
               extractVO.setAmount(transferNumber);
               extractVO.setAmountAfter(assetNew.getCouponNum().subtract(transferNumberTotal));
               extractVO.setAmountBefore(assetNew.getCouponNum());
               extractVO.setCouponId(couponId);
               extractVO.setRatio(couponExtract);
               extractVO.setUserId(myUserInfo.getId());
               extractVO.setPoundage(transferNumber.multiply(couponExtract).divide(new BigDecimal(100)));
               extractVO.setArriveAccount(mobile);
               extractVO.setStatus("0");*/
               int row = couponsDAO.inserCouponExtract(extractVO);
               A.check(row < 0, "彩票积分使用失败请重试" + ":" + transferNumberTotal);
           }else if(couponEntity.getCouponName().equals("游戏积分")){
               //https://github.com/sharding-sphere/sharding-sphere/issues/767 暂时不支持for update走主库
               //HintManager 会自动关闭
               try (HintManager hintManager = HintManager.getInstance()) {
                   //强制走主库
                   UserCouponEntity userCouponEntity = couponsDAO.getUserCouponEntity(SubjectUtil.getCurrent().getId(), couponId);
                   assetNew.setCouponNum(userCouponEntity.getCouponNum());
                   A.check(userCouponEntity == null, "用户无游戏积分");
                   A.check(userCouponEntity.getCouponNum().compareTo(transferNumberTotal) < 0, "游戏积分不足" + ":" + transferNumberTotal);
                   userCouponEntity.setCouponNum(transferNumberTotal);
                   int userCouponEntityRow = couponsDAO.updateUserCouponBySend(userCouponEntity);
                   A.check(userCouponEntityRow < 1, "扣除游戏积分失败" + ":" + transferNumberTotal);
                   ExtractVO extractVO = new ExtractVO();
                   extractVO.setId(IDUtil.getUUID());
                   extractVO.setAmount(transferNumber);
                   extractVO.setAmountAfter(assetNew.getCouponNum().subtract(transferNumberTotal));
                   extractVO.setAmountBefore(assetNew.getCouponNum());
                   extractVO.setCouponId(couponId);
                   extractVO.setRatio(couponExtract);
                   extractVO.setUserId(myUserInfo.getId());
                   extractVO.setPoundage(transferNumber.multiply(couponExtract).divide(new BigDecimal(100)));
                   extractVO.setStatus("0");
                   extractVO.setArriveAccount(mobile);
                   int row = couponsDAO.inserCouponExtract(extractVO);
                   A.check(row < 0, "游戏积分使用失败请重试" + ":" + transferNumberTotal);
               }
           }
       }
   }
    public void checkShopAsset(String shopId){
        //商铺信用金低于1000提示库存不足
        //BigDecimal shopPointBase = sysSettingsService.getShopPointBase();
        ShopEntity shopEntityMessage = shopDAO.getShopEntityByShopId(shopId);
        A.check(shopEntityMessage.getShopSalesAmount().compareTo(new BigDecimal(0)) <= 0, "店铺该商品库存不足");
    }

}
