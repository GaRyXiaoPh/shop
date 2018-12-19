package cn.kt.mall.common.wallet.service;


import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.user.common.UserConstants;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.common.UserRechargeConstant;
import cn.kt.mall.common.wallet.entity.ShopEntity;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.entity.UserRechargeLog;
import cn.kt.mall.common.wallet.enums.AssetType;
import cn.kt.mall.common.wallet.mapper.ShopScalesDAO;
import cn.kt.mall.common.wallet.mapper.UnfreezeDAO;
import cn.kt.mall.common.wallet.mapper.UserAssetDAO;
import cn.kt.mall.common.wallet.mapper.UserRechargeLogDAO;
import cn.kt.mall.common.wallet.vo.UnfreezeVO;
import cn.kt.mall.common.wallet.vo.UserAssetVO;
import cn.kt.mall.common.wallet.vo.UserFundsVO;
import cn.kt.mall.common.wallet.vo.UserRechargeLogVO;
import com.github.pagehelper.PageInfo;
import io.shardingjdbc.core.api.HintManager;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class UserAssetService {

    @Autowired
    UserAssetDAO userAssetDAO;
    @Autowired
    UserRechargeLogDAO userRechargeLogDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    StatementService statementService;
    @Autowired
    private UnfreezeDAO unfreezeDAO;
    @Autowired
    private ShopScalesDAO shopScalesDAO;


    // 添加资产
    @Transactional
    public void addInternalAsset(String userId) {
        initAsset(userId, AssetType.CREDIT.getStrType());
        initAsset(userId, AssetType.POPC.getStrType());
    }

    public void initAsset(String userId, String currency) {
        UserAssetEntity asset = new UserAssetEntity();
        asset.setId(IDUtil.getUUID());
        asset.setUserId(userId);
        asset.setInternalAsset(true);
        asset.setSpendable(true);
        asset.setWithdrawable(true);
        asset.setAvailableBalance(new BigDecimal("0"));
        asset.setReservedBalance(new BigDecimal("0"));
        asset.setCreateTime(new Date());
        asset.setLastTime(new Date());
        asset.setCurrency(currency);

        int rows = userAssetDAO.addAsset(asset);
        A.check(rows == 0, "初始化" + currency + "资产失败");
    }

    //获取用户指定类型的资产
    public UserAssetEntity getUserAssetByCurrency(String userId, String currency) {
        return userAssetDAO.getAssetByUserIdAndCurrency(userId, currency);
    }

    //获取会员的所有资产
    public List<UserAssetEntity> getUserAsset(String userId) {
        List<UserAssetEntity> userAssetList = userAssetDAO.getAssetByUserId(userId);
        return userAssetList;
    }


    /**
     * 修改指定用户的资产
     *
     * @param userId
     * @param currency              资产类型
     * @param deltaAvailableBalance 可用余额的变动金额，负数表示减少
     * @param deltaReservedBalance  冻结余额的变动金额，负数表示减少
     * @return currentUserId        当前登陆人ID
     */
    @Transactional
    public void updateUserAsset(String userId, String currency,
                                BigDecimal deltaAvailableBalance,
                                BigDecimal deltaReservedBalance,
                                TradeType tradeType,
                                String referenceId) {
        //https://github.com/sharding-sphere/sharding-sphere/issues/767 暂时不支持for update走主库
        //HintManager 会自动关闭
        try (HintManager hintManager = HintManager.getInstance()) {
            //强制走主库
            hintManager.setMasterRouteOnly();

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
        }
    }


    /**
     * 添加充值日志
     *
     * @param userAssetEntity
     * @param currentUserId
     * @param operationType
     * @param big
     */
    private String addUserRechargeLog(UserAssetEntity userAssetEntity, String currentUserId, String operationType, BigDecimal big, String otherUserId, String remarks, String status) {
        UserRechargeLog log = new UserRechargeLog();
        log.setId(IDUtil.getUUID());
        log.setRechargeType(userAssetEntity.getCurrency());
        log.setUserId(currentUserId);
        log.setRechargeAmount(big);
        log.setRechargeUser(otherUserId);
        log.setOperationType(operationType);
        log.setStatus(status);
        log.setCustomerType(userAssetEntity.getCustomerType());
        if (remarks == null) {
            remarks = " ";
        }
        log.setRemarks(remarks);
        int logCount = userRechargeLogDAO.addUserRechargeLog(log);
        A.check(logCount <= 0, "添加资产日志失败");

        return log.getId();
    }

    public CommonPageVO<UserRechargeLogVO> getUserRechargeLogListByUserId(UserRechargeLogVO userRechargeLogVO, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<UserRechargeLogVO> userRechargeLogVOList = userRechargeLogDAO.getUserRechargeLogListByUserId(userRechargeLogVO, rowBounds);
        PageInfo<UserRechargeLogVO> pageInfo = new PageInfo<>(userRechargeLogVOList);
        return CommonUtil.copyFromPageInfo(pageInfo, userRechargeLogVOList);
    }

    public List<UserAssetEntity> getUserAssetEntityList(String userId) {
        return userAssetDAO.getAssetByUserId(userId);
    }

    /**
     * 商铺后台管理 分页专用
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public CommonPageVO<UserFundsVO> getUserFundsByUserId(String userId, String rechargeType, String operationType, String mobile, String beginTime, String endTime, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<UserFundsVO> userFundsVOList = userRechargeLogDAO.getUserFundsByUserId(userId, rechargeType, operationType, mobile, beginTime, endTime, rowBounds);
        PageInfo<UserFundsVO> pageInfo = new PageInfo<>(userFundsVOList);
        return CommonUtil.copyFromPageInfo(pageInfo, userFundsVOList);
    }


    /**
     * 商铺后台资金管理充值,扣除
     *
     * @param mobile        被充值用户手机号
     * @param rechargeType  充值类型
     * @param big           数量
     * @param currentUserId 登陆人ID
     * @return
     */
    @Transactional
    public void rechargeOthers(String mobile, String rechargeType, BigDecimal big, String currentUserId, String operationType, String type, String remarks) {
        A.check(!operationType.equals("1") && !operationType.equals("2"), "充值类型错误");
        A.check(big.compareTo(new BigDecimal(0)) <= 0, "充值金额错误");

        //判断充值用户是否为他的下级关系
        UserEntity users = userDAO.getByMobile(mobile);
        A.check(users == null, "充值用户不存在");
        //充值人用户
        UserAssetEntity userAssetEntity = new UserAssetEntity();
        //被充值人用户
        UserAssetEntity rechargeablePerson = new UserAssetEntity();

        // Map<String, Object> UserMap = new HashMap<String, Object>();
        String otherUserId = "";
        //根据userID作为条件 查询PID为当前userid 首次递
        TradeType userStatus = null;

        if (type.equals(UserRechargeConstant.USER_USER_SHOP)) {
            UserEntity userEntity = userDAO.getById(currentUserId);
            A.check(userEntity == null, "用户不存在");
            A.check(!users.getPid().equals(userEntity.getId()), "充值用户不是您的下级,不能充值");
            //A给B充值：A减少可用，增加冻结。B不操作
            if (operationType.equals(UserRechargeConstant.USER_RECHARGE)) {
                //充值人
                otherUserId = currentUserId;
                userAssetEntity.setUserId(otherUserId);
                userAssetEntity.setReservedBalance(BigDecimal.ZERO);
                userAssetEntity.setCurrency(rechargeType);
                userAssetEntity.setOperationType(operationType);
                userAssetEntity.setAvailableBalance(new BigDecimal("-" + big));
                userAssetEntity.setCustomerType(type);
                //被充值人
                rechargeablePerson.setUserId(users.getId());
                rechargeablePerson.setReservedBalance(BigDecimal.ZERO);
                rechargeablePerson.setAvailableBalance(big);
                rechargeablePerson.setCurrency(rechargeType);
                userStatus = TradeType.RECHARGE;
            } else {
                otherUserId =currentUserId;
                //A给B扣除：A不操作，B减少可用，增加冻结
                userAssetEntity.setUserId(otherUserId);
                userAssetEntity.setReservedBalance(BigDecimal.ZERO);
                userAssetEntity.setCurrency(rechargeType);
                userAssetEntity.setOperationType(operationType);
                userAssetEntity.setAvailableBalance( big);
                userAssetEntity.setCustomerType(type);
                //被充值人
                rechargeablePerson.setUserId(users.getId());
                rechargeablePerson.setReservedBalance(BigDecimal.ZERO);
                rechargeablePerson.setAvailableBalance(new BigDecimal("-" + big));
                rechargeablePerson.setCurrency(rechargeType);
                userStatus = TradeType.RECHARGE;
            }
        } else {
            A.check(shopScalesDAO.getMyShop(users.getId()) == null, "该用户没有店铺，不可进行充值或扣除操作！");
            String shopLevel = shopScalesDAO.getMyShop(users.getId()).getShopLevel();
            A.check(!shopLevel.equals("3"), "管理员不能对非批发店进行充值或扣除操作！");
            otherUserId = users.getId();
            //总后台用户直接给给充值用户 加减 冻结数
            userAssetEntity.setUserId(users.getId());
            userAssetEntity.setCurrency(rechargeType);
            userAssetEntity.setOperationType(operationType);
            if (operationType.equals(UserRechargeConstant.USER_DEDUCTION)) {
                userAssetEntity.setAvailableBalance(new BigDecimal("-" + big));
                userAssetEntity.setReservedBalance(big);
            } else {
                userAssetEntity.setReservedBalance((big));
            }
            userAssetEntity.setCustomerType(type);

        }
        if (type.equals(UserRechargeConstant.USER_ADMIN_SHOP) && (operationType.equals(UserRechargeConstant.USER_RECHARGE)||operationType.equals(UserRechargeConstant.USER_DEDUCTION))) {
            TradeType status;
            if (operationType.equals(UserRechargeConstant.USER_RECHARGE)) {
                status = TradeType.RECHARGE_USER;
            } else {
                status = TradeType.REDUCE_USER;
            }
            String logId = this.addUserRechargeLog(userAssetEntity, currentUserId, userAssetEntity.getOperationType(), big, otherUserId, remarks, "0");
            this.updateUserAsset(userAssetEntity.getUserId(), userAssetEntity.getCurrency(),
                    userAssetEntity.getAvailableBalance(), userAssetEntity.getReservedBalance(),
                    status, logId);
        } else {
            TradeType status;
            if (operationType.equals(UserRechargeConstant.USER_RECHARGE)) {
                status = TradeType.RECHARGE_USER;
            } else {
                status = TradeType.REDUCE_USER;
            }

            String logId = this.addUserRechargeLog(userAssetEntity,currentUserId, userAssetEntity.getOperationType(), big, users.getId(), remarks, "1");
            //充值人操作日志
            this.updateUserAsset(userAssetEntity.getUserId(), userAssetEntity.getCurrency(),
                    userAssetEntity.getAvailableBalance(), userAssetEntity.getReservedBalance(),
                    status, logId);
            //被充值人操作日志
            this.updateUserAsset(rechargeablePerson.getUserId(), rechargeablePerson.getCurrency(),
                    rechargeablePerson.getAvailableBalance(), rechargeablePerson.getReservedBalance(),
                    userStatus, logId);
        }


    }

    /**
     * @param pid     父ID
     * @param hashMap 数据集
     * @return
     */
    private Map<String, Object> getUserPid(String pid, Map<String, Object> hashMap) {
        //首次递归查询自身
        List<UserEntity> userEntityList = userDAO.getUserByPid(pid);
        if (userEntityList.size() > 0) {
            for (UserEntity userEntity : userEntityList) {
                hashMap.put(userEntity.getId(), userEntity);
                this.getUserPid(userEntity.getId(), hashMap);
            }
        }
        //根据子节点
        return hashMap;
    }

    /**
     * 根据日志id查询该条日志
     *
     * @param id
     * @return
     */

    public UserRechargeLogVO getUserRechargeLogListById(String id) {
        return userRechargeLogDAO.getUserRechargeLogListById(id);
    }

    @Transactional
    public void releaseCoin(
            String releaseDay,
            String currency,
            BigDecimal totalProfit,
            BigDecimal releaseRatio,
            String userId,
            BigDecimal releaseAmount,
            BigDecimal totalReservedBalance,
            Integer type) {
        //纪录解冻日志
        UnfreezeVO vo = new UnfreezeVO();
        vo.setUserId(userId);
        vo.setId(IDUtil.getUUID());
        vo.setUnfreezeRatio(releaseRatio.toPlainString());
        vo.setProfitCashTotal(totalProfit);
        vo.setUnfreezeCash(releaseAmount);
        vo.setUnfreezeCashTotal(totalReservedBalance);
        vo.setUnfreezeDate(releaseDay);
        vo.setType(type);

        if (releaseAmount.compareTo(BigDecimal.ZERO) > 0) {
            unfreezeDAO.addUnfreezeLog(vo);

            //解冻popc
            updateUserAsset(userId, currency, releaseAmount, releaseAmount.negate(), TradeType.RELEASE, vo.getId());
        }
    }

    //购物赠送优惠券流水表加入商品Id
    @Transactional
    public void updateUserAssetGive(String userId, String currency,
                                    BigDecimal deltaAvailableBalance,
                                    BigDecimal deltaReservedBalance,
                                    TradeType tradeType,
                                    String referenceId,
                                    String goodId) {
        //https://github.com/sharding-sphere/sharding-sphere/issues/767 暂时不支持for update走主库
        //HintManager 会自动关闭
        try (HintManager hintManager = HintManager.getInstance()) {
            //强制走主库
            hintManager.setMasterRouteOnly();
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
            statementEntity.setGoodId(goodId);

            statementService.addStatementGive(statementEntity);
        }
    }
}
