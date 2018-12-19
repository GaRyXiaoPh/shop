package cn.kt.mall.common.wallet.service;


import cn.kt.mall.common.asserts.A;

import cn.kt.mall.common.shop.vo.ShopSalesAmountVO;
import cn.kt.mall.common.user.dao.UserDAO;

import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.wallet.base.TradeType;

import cn.kt.mall.common.wallet.entity.ShopEntity;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;

import cn.kt.mall.common.wallet.mapper.ShopScalesDAO;
import cn.kt.mall.common.wallet.mapper.UnfreezeDAO;
import cn.kt.mall.common.wallet.mapper.UserAssetDAO;
import cn.kt.mall.common.wallet.mapper.UserRechargeLogDAO;

import io.shardingjdbc.core.api.HintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
public class ShopSalesAmountService {

    @Autowired
    UserAssetDAO userAssetDAO;
    @Autowired
    UserRechargeLogDAO userRechargeLogDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    StatementService statementService;
    @Autowired
    private UnfreezeDAO nfreezeDAO;
    @Autowired
    private ShopScalesDAO shopScalesDAO;


    /**
     * 充值、扣除、会员消费时修改指定用户的资产及其店铺的销售额度
     *
     * @param userId
     * @param currency              资产类型
     * @param deltaAvailableBalance 可用余额的变动金额，负数表示减少
     * @param deltaReservedBalance  冻结余额的变动金额，负数表示减少
     * @return currentUserId        当前登陆人ID
     */
    @Transactional
    public void updateShopSales(String userId,
                                BigDecimal amount,
                                TradeType tradeType,
                                String referenceId) {
        //https://github.com/sharding-sphere/sharding-sphere/issues/767 暂时不支持for update走主库
        //HintManager 会自动关闭
        try (HintManager hintManager = HintManager.getInstance()) {
            //强制走主库
            hintManager.setMasterRouteOnly();

            ShopEntity shop = shopScalesDAO.getMyShop(userId);
/*        if(!tradeType.getType().equals(1)){//会员消费不扣除店铺信用金
            UserAssetEntity userAssetEntity = userAssetDAO.getAssetByUserIdAndTypeForUpdate(currency, userId);
            A.check(userAssetEntity == null, "资产不存在");

            UserAssetEntity assetNew = new UserAssetEntity();
            assetNew.setUserId(userId);
            assetNew.setCurrency(currency);

            if (deltaReservedBalance != null && deltaReservedBalance.compareTo(BigDecimal.ZERO) != 0) {
                assetNew.setReservedBalance(userAssetEntity.getReservedBalance().add(deltaReservedBalance));
                A.check(assetNew.getReservedBalance().compareTo(BigDecimal.ZERO) < 0, "冻结余额不足");
            }

            if (deltaAvailableBalance != null && deltaAvailableBalance.compareTo(BigDecimal.ZERO) != 0) {
                assetNew.setAvailableBalance(userAssetEntity.getAvailableBalance().add(deltaAvailableBalance));
                A.check(assetNew.getAvailableBalance().compareTo(BigDecimal.ZERO) < 0, "可用余额不足");
            }

            int rows = userAssetDAO.updateAssetByUserIdAndMoneny(assetNew);
            A.check(rows == 0, "修改资产失败");

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

            statementService.addStatement(statementEntity);
        }*/

            //如果该用户有店铺则改变其店铺销售额度
            if (shop != null) {
                ShopEntity shopNew = new ShopEntity();
                shopNew.setId(shop.getId());
                if (amount != null && amount.compareTo(BigDecimal.ZERO) != 0) {
                    shopNew.setShopSalesAmount(shop.getShopSalesAmount().add(amount));
                    //A.check(shopNew.getShopSalesAmount().compareTo(BigDecimal.ZERO) < 0, "店铺销售额度不足");
                }
                int row = shopScalesDAO.updateShopSalesAmount(shopNew);
                A.check(row == 0, "修改店铺销售额度失败");
                ShopSalesAmountVO shopSalesAmountVO = new ShopSalesAmountVO();
                shopSalesAmountVO.setId(IDUtil.getUUID());
                shopSalesAmountVO.setUserId(userId);
                shopSalesAmountVO.setShopSalesAmountBefore(shop.getShopSalesAmount());
                shopSalesAmountVO.setShopSalesAmountChange(amount);
                shopSalesAmountVO.setShopSalesAmountAfter(shopNew.getShopSalesAmount());
                shopSalesAmountVO.setOperateType(tradeType.getType().toString());
                shopSalesAmountVO.setReferenceId(referenceId);
                shopScalesDAO.addShopScalesLog(shopSalesAmountVO);
            }
        }
    }
    public void checkShopSalesAmount(String shopId){
        //商铺信用金低于1000提示库存不足
        //BigDecimal shopPointBase = sysSettingsService.getShopPointBase();
        ShopEntity shopEntityMessage = shopScalesDAO.getShopEntityByShopId(shopId);
        A.check(shopEntityMessage.getShopSalesAmount().compareTo(new BigDecimal(0)) <= 0, "店铺该商品库存不足");
    }

}
