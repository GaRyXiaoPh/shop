package cn.kt.mall.management.admin.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.user.vo.*;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.entity.UserRechargeLog;
import cn.kt.mall.common.wallet.enums.AssetType;
import cn.kt.mall.common.wallet.mapper.UserAssetDAO;
import cn.kt.mall.common.wallet.mapper.UserRechargeLogDAO;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.common.wallet.vo.UserRechargeLogVO;
import cn.kt.mall.management.admin.dao.TbCouponDAO;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.vo.ShopSalesAndTimeVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.github.pagehelper.PageInfo;
import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.DateUtil;
import cn.kt.mall.common.wallet.mapper.StatementDAO;
import cn.kt.mall.management.admin.vo.IndexVO;
import cn.kt.mall.offline.dao.GoodsDAO;
import cn.kt.mall.offline.dao.OrderDAO;
import cn.kt.mall.shop.config.SysConfig;
import cn.kt.mall.shop.good.mapper.GoodDAO;
import cn.kt.mall.shop.shop.constant.Constants;
import cn.kt.mall.shop.shop.mapper.ShopDAO;
import cn.kt.mall.shop.trade.mapper.TradeDAO;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserAssetService userAssetService;
    @Autowired
    private UserAssetDAO userAssetDAO;
    @Autowired
    private StatementDAO statementDAO;
    @Autowired
    private ShopDAO shopDAO;
    @Autowired
    private SysConfig sysConfig;
    @Autowired
    private TradeDAO tradeDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private GoodDAO goodDAO;
    @Autowired
    private GoodsDAO goodsDAO;
    @Autowired
    private UserRechargeLogDAO userRechargeLogDAO;
    @Autowired
    private TbCouponDAO tbCouponDAO;

    /**
     * 会员恢复(批量)
     *
     * @param userIds 会员id集合：使用,分隔
     */
    @Transactional
    public void passUser(String userIds) {
        A.check(StringUtils.isEmpty(userIds), "请选择会员");
        String[] userIdArr = userIds.split(",");
        List<String> idsList = null;
        if (userIdArr.length > 0) {
            idsList = Arrays.asList(userIdArr);
        }
        int rows = userDAO.editUserStatus(idsList, "0");
        A.check(rows <= 0, "会员恢复失败");
    }

    /**
     * 会员禁用(批量)
     *
     * @param userIds 会员id集合：使用,分隔
     */
    @Transactional
    public void failUser(String userIds) {
        A.check(StringUtils.isEmpty(userIds), "请选择会员");
        String[] userIdArr = userIds.split(",");
        List<String> idsList = null;
        if (userIdArr.length > 0) {
            idsList = Arrays.asList(userIdArr);
        }
        int rows = userDAO.editUserStatus(idsList, "1");
        A.check(rows <= 0, "会员禁用失败");
    }

    /**
     * 查询会员等级(用于筛选)
     */
    public List<String> queryUserLevel() {
        List<String> levels = userDAO.queryUserLevel();
        A.check((null == levels), "查询等级失败");
        return levels;
    }

    /**
     * 会员管理分页查询
     *
     * @param userMobile          用户手机号
     * @param referrerMobile      推荐人手机号
     * @param level               用户等级
     * @param shopType            店铺类型--2:零售店  3:批发店
     * @param shopMobile          所属商铺电话
     * @param status              用户状态,0已启用，1已禁用
     * @param certificationStatus 实名审核状态，0未审核，1已通过，2已拒绝，3未实名
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageVO<UserManageRespVO> listUserData(String userMobile, String referrerMobile, String level, String shopType,
                                                 String shopMobile, String status, String certificationStatus, int pageNo, int pageSize) {
        int srcPageNo = pageNo;
        if (pageNo > 0) {
            pageNo = pageNo - 1;
        }
        int offset = pageNo * pageSize;
        // 查询总页数
        int count = userDAO.manageUserAndShopCount(userMobile, referrerMobile, level, shopType, shopMobile, status, certificationStatus);
        // 分页查询会员管理
        List<UserManageRespVO> list = userDAO.manageUserAndShop(userMobile, referrerMobile, level, shopType, shopMobile, status, certificationStatus, offset, pageSize);
        return new PageVO<>(srcPageNo, pageSize, count, list);
    }

    /**
     * 新会员管理分页查询
     *
     * @param userMobile
     * @param status
     * @param certificationStatus
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageVO<UserManageRespVO> queryUserList(String userMobile, String status, String certificationStatus, int pageNo, int pageSize) {
        int srcPageNo = pageNo;
        if (pageNo > 0) {
            pageNo = pageNo - 1;
        }
        int offset = pageNo * pageSize;
        int count = userDAO.queryUserListCount(userMobile, status, certificationStatus);
        List<UserEntity> list = userDAO.queryUserList(userMobile, status, certificationStatus, offset, pageSize);
        List<UserManageRespVO> voList = new ArrayList<>();
        if (!list.isEmpty()) {
            // 推荐人
            List<UserEntity> userList = userDAO.getByIdList(list.stream().map(l -> l.getReferrer()).collect(Collectors.toList()));
            Map<String, UserEntity> id2UserMap = userList.stream().collect(Collectors.toMap(UserEntity::getId, m -> m));

            List<UserEntity> parentUserList = userDAO.getByIdList(list.stream().map(l -> l.getPid()).collect(Collectors.toList()));
            Map<String, UserEntity> id2ParentUserMap =
                    parentUserList.stream().collect(Collectors.toMap(UserEntity::getId, m -> m));
            voList.addAll(UserManageRespVO.fromEntity(list));

            for (UserManageRespVO u : voList) {
                // 用户等级
                String userLevel = userDAO.queryUserLevelByUserID(u.getId());
                u.setLevel(StringUtils.isEmpty(userLevel) ? "0级" : userLevel + "级");
                // 实名认证状态
                String cerStatus = u.getCertificationStatus();
                u.setCertificationStatus(StringUtils.isEmpty(cerStatus) ? "3" : cerStatus);
                UserEntity referrer = id2UserMap.get(u.getReferrer());
                if (referrer != null) {
                    u.setReferrerMobile(referrer.getMobile());
                    u.setReferrerTrueName(referrer.getTrueName());
                }
                //添加用户资金信息
                UserAssetEntity userAssetVO = userAssetService.getUserAssetByCurrency(u.getId(), AssetType.CREDIT.getStrType());
                if (userAssetVO != null) {
                    u.setAvailableBalance(userAssetVO.getAvailableBalance());
                }
                // 判断当前人员是否有店铺
                List<ShopEntity> myShopList = shopDAO.getMyShopEntity(u.getId());
                ShopEntity shop = null;
                if (myShopList != null && myShopList.size() > 0) {
                    // 显示自己店铺
                    shop = myShopList.get(0);
                    u.setShopUser(shop.getShopName());
                    u.setShopRank(shop.getShopRank() + "级");
                    u.setShopType(shop.getShopLevel());
                } else {
                    // 显示上级店铺
                    List<ShopEntity> shopList = shopDAO.getMyShopEntity(u.getPid());
                    if (shopList != null && shopList.size() > 0) {
                        shop = shopList.get(0);
                        UserEntity parentUser = id2ParentUserMap.get(shop.getUserId());
                        if (parentUser != null) {
                            u.setShopUser(shop.getShopName());
                            u.setShopRank(shop.getShopRank() + "级");
                            u.setShopType(shop.getShopLevel());
                        }
                    }
                }
                if (shop != null) {
                    u.setShopMobile(shop.getMobile());
                    u.setShopLevel(shop.getShopLevel());
                }
            }
        }
        return new PageVO<>(srcPageNo, pageSize, count, voList);
    }

    /**
     * 获取会员基本信息
     *
     * @param userId 用户id
     * @return
     */
    public UserManageBaseVO getUserBaseInfo(String userId) {
        UserEntity userEntity = userDAO.getUserBaseInfo(userId);
        A.check(userEntity == null, "会员不存在");
        UserManageBaseVO userManageBaseVO = new UserManageBaseVO();
        // 用户姓名
        userManageBaseVO.setTrueName(userEntity.getTrueName());
        // 用户账号（手机号码）
        userManageBaseVO.setMobile(userEntity.getMobile());
        // 用户等级
        String userLevel = userDAO.queryUserLevelByUserID(userId);
        userManageBaseVO.setLevel(StringUtils.isEmpty(userLevel) ? "0级" : userLevel + "级");
        // 会员推荐人信息
        if (!StringUtils.isBlank(userEntity.getReferrer())) {
            UserEntity referrerEntity = userDAO.getUserBaseInfo(userEntity.getReferrer());
            if (referrerEntity != null) {
                // 推荐人姓名
                userManageBaseVO.setReferrerName(referrerEntity.getTrueName());
                // 推荐人电话
                userManageBaseVO.setReferrerMobile(referrerEntity.getMobile());
            }
        }
        // 添加所属商铺信息
        List<ShopEntity> shop = shopDAO.getMyShopEntity(userId);
        // 店铺id，用于获取店铺的销售业绩
        String shopId = null;
        // 若本人有店铺则添加本人店铺信息为所属店铺信息，并添加所属店长信息为本人
        if (shop.size() > 0) {
            shopId = shop.get(0).getId();
            // 所属商铺ID
            userManageBaseVO.setShopNo(shop.get(0).getShopNo());
            // 所属店铺名称
            userManageBaseVO.setPName(shop.get(0).getShopName());
            // 商铺级别， 2:形象店 3:旗舰店
            userManageBaseVO.setShopLevel(shop.get(0).getShopLevel());
            // 所属店铺手机号
            userManageBaseVO.setPMobile(userEntity.getMobile());
        } else {
            //若本人无店铺则查找本人pid店铺信息为所属店铺信息，并添加所属店长信息为pid
            List<ShopEntity> shops = shopDAO.getMyShopEntity(userEntity.getPid());
            if (shops.size() > 0) {
                shopId = shops.get(0).getId();
                // 所属商铺ID
                userManageBaseVO.setShopNo(shops.get(0).getShopNo());
                // 所属店铺名称
                userManageBaseVO.setPName(shops.get(0).getShopName());
                // 商铺级别， 2:形象店 3:旗舰店
                userManageBaseVO.setShopLevel(shops.get(0).getShopLevel());
                // 所属店铺手机号
                UserEntity shopEntity = userDAO.getById(shops.get(0).getUserId());
                if (shopEntity != null) {
                    userManageBaseVO.setPMobile(shopEntity.getMobile());
                }
            }
        }
        // 可用信用金
        BigDecimal availableBalance = userDAO.queryAvailableBalance(userId);
        userManageBaseVO.setAvailableBalance(availableBalance);
        // 店铺销售业绩
        ShopSalesAndTimeVO shopSalesAndTimeVO = shopDAO.getShopSalesAndPointAndCoupon(shopId);
        if (null != shopSalesAndTimeVO) {
            userManageBaseVO.setSalePerformance(shopSalesAndTimeVO.getSale());
        }
        // 团队人数
        userManageBaseVO.setReferrerCounts(userEntity.getTeamCount());
        return userManageBaseVO;
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
     * @return
     */
  /* private Map<String, Object> getUserReferee(String pid, Map<String, Object> hashMap) {
        //首次递归查询自身
        List<UserEntity> userEntityList = userDAO.getUserByReferee(pid);
        if (userEntityList.size() > 0) {
            for (UserEntity userEntity : userEntityList) {
                hashMap.put(userEntity.getId(), userEntity);
                this.getUserReferee(userEntity.getId(), hashMap);
            }
        }
        //根据子节点
        return hashMap;
    }*/

    private void updateSuperior(UserManageBaseVO vo, UserEntity user) {
        if (!StringUtils.isBlank(user.getPid())) {
            if (user.getId().equalsIgnoreCase(user.getPid())) {
                return;
            }

            UserEntity superiorEntity = userDAO.getById(user.getPid());
            if (superiorEntity != null) {
                //updateBaseFromSuperiorEntity(vo, superiorEntity);
                //updateSuperior(vo,superiorEntity);
                vo.setPName(superiorEntity.getTrueName());
                vo.setPMobile(superiorEntity.getMobile());
            }
        }
    }

	/*private void updateBaseFromSuperiorEntity(UserManageBaseVO vo, UserEntity user) {
        if (user.getLevel().equalsIgnoreCase(UserLevel.L2.getSort() + "")) {
			vo.setL2SuperiorName(user.getTrueName());
			vo.setL2SuperiorMobile(user.getMobile());
		} else if (user.getLevel().equalsIgnoreCase(UserLevel.L3.getSort() + "")) {
			vo.setL3SuperiorName(user.getTrueName());
			vo.setL3SuperiorMobile(user.getMobile());
		} else if (user.getLevel().equalsIgnoreCase(UserLevel.L4.getSort() + "")) {
			vo.setL4SuperiorName(user.getTrueName());
			vo.setL4SuperiorMobile(user.getMobile());
		}
	}*/

    public CommonPageVO<ReferrerRespVO> getRefereeUserByUserId(String userId, int pageNo, int pageSize) {
        List<UserEntity> list = userDAO.getRefereeUserByUserId(userId, new RowBounds(pageNo, pageSize));
        List<ReferrerRespVO> voList = ReferrerRespVO.fromEntity(list);
        PageInfo<ReferrerRespVO> pageInfo = new PageInfo<>(voList);
        return CommonUtil.copyFromPageInfo(pageInfo, voList);
    }

    /**
     * 获取会员优惠券列表
     *
     * @param userId 会员id
     * @return
     */
    public List<UserCouponVO> getUserCouponList(String userId) {
        A.check(StringUtils.isEmpty(userId), "会员不存在");
        return tbCouponDAO.getUserCouponList(userId);
    }

    public IndexVO getIndexInfo() {
        int totalUser = userDAO.countAllUser();// 统计所有用户
        int totalOnlineUser = shopDAO.countByType(Constants.ShopType.SHOP_ONLINE);// 线上商户总数
        int totalOfflineUser = shopDAO.countByType(Constants.ShopType.SHOP_OFFLINE);// 线下商户总数
        int totalShopUser = shopDAO.countByType("3");// 线上&线下商户总数
        int totalSimpleUser = shopDAO.countByType("0");// 普通会员数
        BigDecimal sysLem = userAssetService.getUserAssetByCurrency(sysConfig.getSysAccount(), AssetType.CREDIT.getStrType()).getAvailableBalance();// 平台账号莱姆币
        //TODO
        BigDecimal userTotalLem = new BigDecimal("0");//userAssetService.countByAllUser(sysConfig.getSysAccount());// 所有会员账户金额
        BigDecimal totalOnlineLem = new BigDecimal("0");//walletLemService.countByUserType(sysConfig.getSysAccount(),
        //Constants.ShopType.SHOP_ONLINE);// 线上商户账户金额
        BigDecimal totalOfflineLem = new BigDecimal("0");//.countByUserType(sysConfig.getSysAccount(),
        //Constants.ShopType.SHOP_OFFLINE);// 地面商户账户金额
        BigDecimal sysFreeLem = statementDAO.countByUserIdAndStatus(sysConfig.getSysAccount(), (short) 6);// 平台费用
        BigDecimal tradeOnlineTotalLem = tradeDAO.manageCount(null, null);// 平台商城交易总额
        BigDecimal tradeOfflineTotalLem = orderDAO.manageCount(null, null);// 平台商圈交易总额
        Date date = new Date();
        Date startDate = DateUtil.minusTime(date, "6d");
        Date[] dates = DateUtil.getDateStartAndEnd(date);
        BigDecimal tradeOnlineLem = tradeDAO.manageCount(dates[0], dates[1]);// 平台商城交易总额
        BigDecimal tradeOfflineLem = orderDAO.manageCount(dates[0], dates[1]);// 平台商圈交易总额

        List<UserCountVO> userRegisterList = userDAO.registerCount(DateUtil.getDateString(startDate),
                DateUtil.getDateString(date));// 七日注册数据统计
        userRegisterList = buildUserCountData(userRegisterList, startDate, date);

        // 地面商品上架数量
        int offlineGoodPass = goodsDAO.countByStatus(1);
        // 网上商品上架数量
        int onlineGoodPass = goodDAO.countByShopIdAndStatus(null,
                cn.kt.mall.shop.good.constant.Constants.GoodStatus.GOOD_PASS);
        // 地面商品审核数量
        int offlineGoodApply = goodsDAO.countByStatus(2);
        // 网上商品审核数量
//		int onlineGoodPassApply = goodDAO.countByShopIdAndStatus(null,
//				cn.kt.mall.shop.good.constant.Constants.GoodStatus.GOOD_APPLY);

        return new IndexVO(totalUser, totalOnlineUser, totalOfflineUser, totalShopUser, totalSimpleUser, sysLem,
                userTotalLem, totalOnlineLem, totalOfflineLem, sysFreeLem, tradeOnlineTotalLem, tradeOfflineTotalLem,
                tradeOnlineLem, tradeOfflineLem, userRegisterList, offlineGoodPass, onlineGoodPass, offlineGoodApply,
                null);
    }

    private static List<UserCountVO> buildUserCountData(List<UserCountVO> todayCountList, Date startTime,
                                                        Date endTime) {
        Map<String, UserCountVO> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(todayCountList)) {
            for (UserCountVO userCountVO : todayCountList) {
                map.put(userCountVO.getDay(), userCountVO);
            }
        } else {
            todayCountList = new ArrayList<>();
        }
        for (Date currentDate = startTime; !currentDate.after(endTime); currentDate = DateUtil.plusTime(currentDate,
                "1d")) {
            String key = DateUtil.getDateString(currentDate);
            UserCountVO vo = map.get(key);
            if (vo == null) {
                vo = new UserCountVO(key, 0);
                todayCountList.add(vo);
            }
        }
        return todayCountList.stream().sorted(Comparator.comparing(UserCountVO::getDay)).collect(Collectors.toList());
    }

    @Transactional
    public void increaseBalance(String userId, String currency, BigDecimal amount) {

        UserAssetEntity entityBefore = userAssetDAO.getAssetByUserIdAndTypeForUpdate(currency, userId);

        if (entityBefore == null) {
            userAssetService.initAsset(userId, currency);
            entityBefore = userAssetDAO.getAssetByUserIdAndTypeForUpdate(currency, userId);
        }


        UserRechargeLog log = new UserRechargeLog();
        log.setCreateTime(new Date());
        log.setId(IDUtil.getUUID());
        log.setUserId(userId);
        log.setRechargeType(currency);
        log.setRechargeAmount(amount);
        userRechargeLogDAO.addUserRechargeLog(log);

        userAssetService.updateUserAsset(userId, currency, amount, null, TradeType.RECHARGE, log.getId());

    }

    @Transactional
    public void reduceBalance(String userId, String currency, BigDecimal amount) {
        UserAssetEntity entityBefore = userAssetDAO.getAssetByUserIdAndTypeForUpdate(currency, userId);
        A.check(entityBefore == null, "资产类型不存在");


        UserRechargeLog log = new UserRechargeLog();
        log.setCreateTime(new Date());
        log.setId(IDUtil.getUUID());
        log.setUserId(userId);
        log.setRechargeType(currency);
        log.setRechargeAmount(amount);
        userRechargeLogDAO.addUserRechargeLog(log);

        userAssetService.updateUserAsset(userId, currency, amount.negate(), null, TradeType.REDUCE, log.getId());
    }

    public CommonPageVO<UserRechargeLogVO> getBalanceHistory(String userId, Integer type, String currency, int pageNo, int pageSize) {
        UserRechargeLogVO reqVO = new UserRechargeLogVO();
        reqVO.setUserId(userId);
        List<UserRechargeLogVO> voList = userRechargeLogDAO.getUserRechargeLogListByUserId(reqVO, new RowBounds(pageNo, pageSize));

        PageInfo<UserRechargeLogVO> pageInfo = new PageInfo<>(voList);
        return CommonUtil.copyFromPageInfo(pageInfo, voList);
    }

    public UserEntity getByMobile(String mobile) {
        return userDAO.getByMobile(mobile);
    }

    /**
     * 修改用户实名认证状态
     *
     * @param userIds             用户id集合，使用,分隔
     * @param certificationStatus 实名认证状态，0未审核，1已通过，2已拒绝，3未实名
     * @return
     */
    public boolean updateCertificationStatus(String userIds, String certificationStatus) {
        A.check(StringUtils.isEmpty(userIds), "参数错误");
        String[] userIdArr = userIds.split(",");
        List<String> idsList = null;
        if (userIdArr.length > 0) {
            idsList = Arrays.asList(userIdArr);
        }
        int rows = userDAO.updateCertificationStatus(idsList, certificationStatus);
        if (rows <= 0) {
            return false;
        }
        return true;
    }

    public BigDecimal queryAvailableBalance(String userId) {
        return userDAO.queryAvailableBalance(userId);
    }
}
