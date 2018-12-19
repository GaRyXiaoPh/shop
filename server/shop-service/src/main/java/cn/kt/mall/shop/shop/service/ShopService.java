package cn.kt.mall.shop.shop.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.kt.mall.common.exception.BusinessException;
import cn.kt.mall.common.shop.vo.ShopSalesAmountVO;
import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.mapper.ShopScalesDAO;
import cn.kt.mall.common.wallet.mapper.UserAssetDAO;
import cn.kt.mall.common.wallet.mapper.UserRechargeLogDAO;
import cn.kt.mall.common.wallet.service.ShopSalesAmountService;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.common.wallet.vo.CashRecordVO;
import cn.kt.mall.shop.good.mapper.GoodDAO;
import cn.kt.mall.shop.good.service.GoodService;
import cn.kt.mall.shop.good.vo.GoodCouponCenterReqVO;
import cn.kt.mall.shop.good.vo.GoodRespVO;
import cn.kt.mall.shop.logistics.vo.ShopTransportVO;
import cn.kt.mall.shop.shop.vo.*;
import cn.kt.mall.shop.trade.entity.TradeItemEntity;
import cn.kt.mall.shop.trade.mapper.ShopTradeDAO;
import com.github.pagehelper.Page;

import org.apache.commons.lang3.StringUtils;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageInfo;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.sms.service.SmsService;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.wallet.mapper.StatementDAO;
import cn.kt.mall.shop.city.service.CityService;
import cn.kt.mall.shop.city.vo.CityRespVO;
import cn.kt.mall.shop.collect.mapper.CollectDAO;
import cn.kt.mall.shop.config.SysConfig;
import cn.kt.mall.shop.good.entity.GoodTypeEntity;
import cn.kt.mall.shop.good.mapper.GoodPropertyDAO;
import cn.kt.mall.shop.shop.constant.Constants;
import cn.kt.mall.shop.shop.entity.ShopAuthdataEntity;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.mapper.ShopDAO;

@Service
public class ShopService {
    @Value("${sys.shop.check_location:true}")
    private boolean checkShopLocation;

    @Autowired
    ShopDAO shopDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CollectDAO collectDAO;
    @Autowired
    private GoodPropertyDAO goodPropertyDAO;
    @Autowired
    private CityService cityService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private StatementDAO statementDAO;
    @Autowired
    private SysConfig sysConfig;
    @Autowired
    private GoodDAO goodDAO;
    @Autowired
    private ShopTradeDAO shopTradeDAO;
    @Autowired
    private UserRechargeLogDAO userRechargeLogDAO;

    @Autowired
    private UserAssetDAO userAssetDAO;
    @Autowired
    private GoodService goodService;
    @Autowired
    private ShopScalesDAO shopScalesDAO;

    // 获取店铺 --- 无鉴权，无需登录
    public ShopVO getShopByShopId(String shopId, String userId) {
        ShopEntity shopEntity = shopDAO.getShopEntityByShopId(shopId);
        A.check(shopEntity == null
                || !cn.kt.mall.common.constant.Constants.STATE_SUCCESS.equals(shopEntity.getStatus()), "店铺非法或不存在");

        ShopVO shopVO = ShopVO.fromEntity(shopEntity);
        UserEntity user = userDAO.getById(shopEntity.getUserId());
        A.check(user == null, "店铺信息异常");
        UserAssetEntity userAssetEntity = userAssetDAO.getAssetByUserIdAndCurrency(user.getId(), "point");
        shopVO.setPoint(userAssetEntity.getAvailableBalance());
        shopVO.setName(user.getTrueName());

        shopVO.setUserMobile(user.getMobile());
        if (StringUtils.isEmpty(userId) || collectDAO.getShopCollectEntity(userId, shopId) == null) {
            shopVO.setCollectType(cn.kt.mall.common.constant.Constants.DEFAULT_0);
        } else {
            shopVO.setCollectType(cn.kt.mall.common.constant.Constants.DEFAULT_1);
        }

        shopVO.setGoodCounts(goodDAO.getGoodByShopIdAndGoodStatusCount(shopVO.getId()));
        return shopVO;
    }

    public AuthDataVO getShopAuthData(String userId, String shopId) {
        AuthDataVO authDataVO = new AuthDataVO();
        ShopEntity shopEntity = shopDAO.getShopEntityByShopId(shopId);
        A.check(shopEntity == null || !shopEntity.getUserId().equals(userId), "店铺非法或不存在");
        ShopAuthdataEntity shopAuthdataEntity = shopDAO.getMyShopAuthdataEntity(shopId);
        A.check(shopAuthdataEntity == null, "店铺认证数据不存在");
        ShopAuthdataDetailsVO shopAuthdataDetailsVO = new ShopAuthdataDetailsVO();
        BeanUtils.copyProperties(shopAuthdataEntity, shopAuthdataDetailsVO);
        CityRespVO cityRespVO = cityService.getDataById(shopAuthdataEntity.getProvince(), shopAuthdataEntity.getCity(),
                shopAuthdataEntity.getCounty());
        if (cityRespVO != null) {
            shopAuthdataDetailsVO.setProvinceValue(cityRespVO.getProvinceValue());
            shopAuthdataDetailsVO.setCityValue(cityRespVO.getCityValue());
            shopAuthdataDetailsVO.setCountyValue(cityRespVO.getCountyValue());
        }
        ShopVO shopVO = new ShopVO();
        BeanUtils.copyProperties(shopEntity, shopVO);
        // 线上商家
        if (shopEntity.getShopType().equals(Constants.ShopType.SHOP_ONLINE)) {
            GoodTypeEntity goodTypeEntity = goodPropertyDAO.getGoodTypeById(shopEntity.getShopTag());
            if (goodTypeEntity != null) {
                shopVO.setShopTagValue(goodTypeEntity.getName());
            }
        }
        authDataVO.setBase(shopVO);
        authDataVO.setAuthData(shopAuthdataDetailsVO);
        return authDataVO;
    }

    // 获取我开通的店铺
    public List<ShopVO> getShop(String userId) {
        List<ShopVO> result = new ArrayList<>();
        // 先获取基本资料
        List<ShopEntity> ll = shopDAO.getMyShopEntity(userId);
        if (CollectionUtils.isEmpty(ll)) {
            return result;
        } else {
            for (ShopEntity shopEntity : ll) {
                ShopVO shopVO = new ShopVO();
                BeanUtils.copyProperties(shopEntity, shopVO);
                UserEntity user = userDAO.getById(shopEntity.getUserId());
                shopVO.setUserMobile(user.getMobile());
                result.add(shopVO);
            }
        }
        return result;
    }

    // 申请开通线上商城
    @Transactional
    public void applyShopOnline(String userId, ApplyShopVO applyShopVO) {
        if (checkShopLocation) {
            A.check(cityService.getDataById(applyShopVO.getProvince(), applyShopVO.getCity(),
                    applyShopVO.getCounty()) == null, "省市区上传不合法");
        }

        List<ShopEntity> ll = shopDAO.getMyOnLineShopEntity(userId);
        if (!CollectionUtils.isEmpty(ll)) {
            for (int i = 0; i < ll.size(); i++) {
                ShopEntity shopEntity = ll.get(i);
                // 判断是否正在申请
                A.check(Constants.ShopStatus.SHOP_APPLY.equals(shopEntity.getStatus()), "已经申请开通网上商家,请等待");
                A.check(Constants.ShopStatus.SHOP_PASS.equals(shopEntity.getStatus()), "已经开通网上商家，无需重复开通");
            }
        }

        // 申请商城
        String shopId = IDUtil.getUUID();
        shopDAO.addShopEntity(applyShopVO.getShopEntity(userId, shopId, Constants.ShopType.SHOP_ONLINE));

        // 申请认证材料
        shopDAO.addShopAuthdataEntity(applyShopVO.getShopAuthdataEntity(userId, shopId));
    }

    // 申请开通线下商城
    @Transactional
    public void applyShopOffline(String userId, ApplyShopVO applyShopVO) {
        A.check(cityService.getDataById(applyShopVO.getProvince(), applyShopVO.getCity(),
                applyShopVO.getCounty()) == null, "省市区上传不合法");
        List<ShopEntity> ll = shopDAO.getMyOffLineShopEntity(userId);
        if (!CollectionUtils.isEmpty(ll)) {
            for (int i = 0; i < ll.size(); i++) {
                ShopEntity shopEntity = ll.get(i);
                // 判断是否正在申请
                A.check(Constants.ShopStatus.SHOP_APPLY.equals(shopEntity.getStatus()), "已经申请开通地面商家,请等待");
                A.check(Constants.ShopStatus.SHOP_PASS.equals(shopEntity.getStatus()), "已经开通地面商家，无需重复开通");
            }
        }

        // 申请商城
        String shopId = IDUtil.getUUID();
        shopDAO.addShopEntity(applyShopVO.getShopEntity(userId, shopId, Constants.ShopType.SHOP_OFFLINE));

        // 申请认证材料
        shopDAO.addShopAuthdataEntity(applyShopVO.getShopAuthdataEntity(userId, shopId));
    }

    @Transactional
    public void updateSelective(ShopEntity shopEntity) {
        int rows = shopDAO.updateSelective(shopEntity);
        A.check(rows != 1, "更新失败");
    }

    @Transactional
    public void passShop(String mark, String shopId) {
        A.check(StringUtils.isBlank(shopId), "id不能为空");
        ShopEntity shop = shopDAO.getShopEntityByShopId(shopId);
        A.check(shop == null, "该店铺不存在");
        UserEntity user = userDAO.getById(shop.getUserId());
        A.check(user == null, "无效的注册用户");
        A.check(!Constants.ShopStatus.SHOP_APPLY.equals(shop.getStatus()), "只有待审核的商家才能进行审核操作");
        ShopEntity shopEntity = new ShopEntity();
        shopEntity.setId(shopId);
        shopEntity.setMark(mark);
        shopEntity.setStatus(Constants.ShopStatus.SHOP_PASS);
        int rows = shopDAO.updateSelective(shopEntity);
        A.check(rows != 1, "更新失败");
        smsService.sendInfoMessage(shop.getUserId(), user.getMobile(),
                String.format("您发起审核的<%s>已经通过审核，更多信息请登录<%s>", shop.getShopName(), sysConfig.getLemHomeUrl()));
    }

    @Transactional
    public void failShop(String mark, String shopId) {
        A.check(StringUtils.isBlank(shopId), "id不能为空");
        ShopEntity shop = shopDAO.getShopEntityByShopId(shopId);
        A.check(shop == null, "该店铺不存在");
        UserEntity user = userDAO.getById(shop.getUserId());
        A.check(user == null, "无效的注册用户");
        A.check(!Constants.ShopStatus.SHOP_APPLY.equals(shop.getStatus()), "只有待审核的商家才能进行审核操作");
        ShopEntity shopEntity = new ShopEntity();
        shopEntity.setId(shopId);
        shopEntity.setMark(mark);
        shopEntity.setStatus(Constants.ShopStatus.SHOP_FAIL);
        int rows = shopDAO.updateSelective(shopEntity);
        A.check(rows != 1, "更新失败");
        smsService.sendInfoMessage(shop.getUserId(), user.getMobile(),
                String.format("您发起审核的商家<%s>经审核不通过，失败原因：<%s>", shop.getShopName(), mark));
    }

    public CommonPageVO<ShopAuthManageVO> listAuthData(String userName, Date startTime, Date endTime, String shopType,
                                                       String status, int pageNo, int pageSize) {
        List<ShopAuthManageVO> list = shopDAO.listAuthData(userName, startTime, endTime, shopType, status,
                new RowBounds(pageNo, pageSize));
        PageInfo<ShopAuthManageVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    // 获取用户开通的店铺的信息
    public List<ShopVO> manageShopList(String userId) {
        List<ShopVO> result = new ArrayList<>();
        // 先获取基本资料
        List<ShopEntity> ll = shopDAO.getMyShopEntity(userId);
        if (CollectionUtils.isEmpty(ll)) {
            return result;
        } else {
            for (ShopEntity shopEntity : ll) {
                ShopVO shopVO = new ShopVO();
                BeanUtils.copyProperties(shopEntity, shopVO);
                UserEntity user = userDAO.getById(shopEntity.getUserId());
                shopVO.setUserMobile(user.getMobile());
                shopVO.setIncomeLem(statementDAO.countByStatementLem(shopEntity.getId()));
                result.add(shopVO);
            }
        }
        return result;
    }

    // 店铺搜索，like 名称 && 商品类型
    public CommonPageVO<GoodRespVO> searchShops(String shopId, String userId, String shopName, String status,
                                                Date startTime, Date endTime, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<ShopEntity> list = new ArrayList<>();
        List<GoodRespVO> result = new ArrayList<>();
        if (userId != null) {
            UserEntity userEntity = userDAO.getById(userId);
            A.check(userEntity == null, "用户不存在:");
            if (userEntity.getLevel().equals("2") || userEntity.getLevel().equals("3") || userEntity.getLevel().equals("4")) {//准站长，站长，中心主任登陆时自己的商品显示Plus标记
                shopId = shopDAO.getShopIdByUserId(userId);
                list = shopDAO.searchShops(shopId, shopName, status, null, null, rowBounds);
            } else {//会员登陆
                shopId = shopDAO.getShopIdByUserId(userEntity.getPid());
                list = shopDAO.searchShops(shopId, shopName, status, null, null, rowBounds);
            }

        } else {
            list = shopDAO.searchShops(shopId, shopName, status, null, null, rowBounds);
        }
        List<GoodRespVO> goodRespVOList = new ArrayList<>();
        for (ShopEntity shopEntity : list) {
            if (shopEntity.getPidFlag() == 1) {
                List<GoodRespVO> goodRespVOList2 = goodDAO.searchGoodsFrontByShopIdPid(shopEntity.getId());
                goodRespVOList.addAll(goodRespVOList2);
            } else {
                List<GoodRespVO> goodRespVOList2 = goodDAO.searchGoodsFrontByShopId(shopEntity.getId());
                goodRespVOList.addAll(goodRespVOList2);
            }

        }
       /* List<GoodRespVO> result = new ArrayList<>();
        List<ShopEntity> list = shopDAO.searchShops(shopId, shopName, status, startTime, endTime, rowBounds);
        List<GoodRespVO> goodRespVOList = new ArrayList<>();
        for(ShopEntity shopEntity : list ){
            List<GoodRespVO> goodRespVOList2 = goodDAO.searchGoodsFrontByShopId(shopEntity.getId());
            goodRespVOList.addAll(goodRespVOList2);
        }
       if (userId != null) {
            UserEntity userEntity = userDAO.getById(userId);
            if (userEntity.getLevel().equals("2") || userEntity.getLevel().equals("3")) {  //站长，中心主任登陆时自己的商品显示Plus标记
                for (GoodRespVO goodRespVO : goodRespVOList) {
                    if (userEntity.getId().equals(goodRespVO.getUserId())) {
                        goodRespVO.setPidFlag(1);
                    }
                }
            } else {
                for (GoodRespVO goodRespVO : goodRespVOList) {
                    if (userEntity.getPid().equals(goodRespVO.getUserId())) {
                        goodRespVO.setPidFlag(1);
                    }
                }
            }
            Collections.sort(goodRespVOList, new Comparator<GoodRespVO>() {
                public int compare(GoodRespVO o1, GoodRespVO o2) {
                    if (o1.getPidFlag() < o2.getPidFlag()) {
                        return 1;
                    }
                    if (o1.getPidFlag() == o2.getPidFlag()) {
                        return 0;
                    }
                    return -1;
                }
            });
        }*/
        result.addAll(goodRespVOList);
        PageInfo<GoodRespVO> pageInfo = new PageInfo<>(result);
        return CommonUtil.copyFromPageInfo(pageInfo, result);

      /*  for (ShopVO shopRespVO : list) {
            List<GoodCouponCenterReqVO> goodCouponCenterEntityList = goodCouponCenterDao.getGoodCollectEntityByShopIdAndGoodId(goodRespVO.getShopId(), goodRespVO.getId());
            if(goodCouponCenterEntityList.size()>0){
                shopRespVO.setCouponEntityList(goodCouponCenterEntityList);
            }
        }*/
    }

    // 店铺内搜索商品 根据商品名称模糊查询
    public CommonPageVO<GoodRespVO> searchShopGoods(String shopId, String userId, String goodName, String goodType, String status,
                                                    String searchMode, Date startTime, Date endTime, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<GoodRespVO> list = new ArrayList<>();
        if (userId != null) {
            UserEntity userEntity = userDAO.getById(userId);
            A.check(userEntity == null, "用户不存在:");
            if (userEntity.getLevel().equals("2") || userEntity.getLevel().equals("3") || userEntity.getLevel().equals("4")) {//准站长，站长，中心主任登陆时自己的商品显示Plus标记
                String myShopId = shopDAO.getShopIdByUserId(userId);
                list = goodDAO.searchShopGood(shopId, goodName, goodType, status, searchMode, startTime, endTime, rowBounds);
                for (GoodRespVO goodRespVO : list) {
                    if (!shopId.equals(myShopId)) {
                        goodRespVO.setPidFlag(0);
                    }
                }
            } else {//会员登陆
                String shopIdPid = shopDAO.getShopIdByUserId(userEntity.getPid());
                list = goodDAO.searchShopGood(shopId, goodName, goodType, status, searchMode, startTime, endTime, rowBounds);
                for (GoodRespVO goodRespVO : list) {
                    if (!shopId.equals(shopIdPid)) {
                        goodRespVO.setPidFlag(0);
                    }
                }
            }

        } else {
            list = goodDAO.searchShopGood(shopId, goodName, goodType, status, searchMode, startTime, endTime, rowBounds);
            for (GoodRespVO goodRespVO : list) {
                goodRespVO.setPidFlag(0);
            }
        }
       /* for (GoodRespVO goodRespVO : list) {
            List<GoodCouponCenterReqVO> goodCouponCenterEntityList = goodCouponCenterDao.getGoodCollectEntityByShopIdAndGoodId(goodRespVO.getShopId(), goodRespVO.getId());
            if (goodCouponCenterEntityList.size() > 0) {
                goodRespVO.setCouponEntityList(goodCouponCenterEntityList);
            }
        }*/

        PageInfo<GoodRespVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    public CommonPageVO<ShopVO> getShopByShopNoAndLevel(String shopNo, String name, String level, String startTime, String endTime, String shopName, String mobile,
                                                        String status, int pageNo, int pageSize) {
        List<ShopEntity> shopEntity = shopDAO.getShopByShopNoAndLevel(shopNo, name, level, startTime, endTime, shopName, mobile, status, new RowBounds(pageNo, pageSize));

//        if (!shopEntity.isEmpty()) {
//            List<UserEntity> userList = userDAO.getByIdList(shopEntity.stream().map(s -> s.getUserId()).collect(Collectors.toList()));
//            Map<String, UserEntity> userId2Map = userList.stream().collect(Collectors.toMap(UserEntity::getId, Function.identity()));
//
//            for (ShopEntity s : shopEntity) {
//                UserEntity user = userId2Map.get(s.getUserId());
//                if (user != null) {
//                    s.setName(user.getTrueName());
//                }
//            }
//        }


        List<ShopVO> voList = ShopVO.fromEntity(shopEntity);

        for (ShopVO s : voList) {
            BigDecimal big = shopTradeDAO.getTotalIncomeForShop(s.getId());
            if (big == null || big.compareTo(BigDecimal.ZERO) <= 0) {
                s.setIncomeLem(BigDecimal.ZERO);
            } else {
                s.setIncomeLem(big);

            }
            s.setGoodCounts(goodDAO.getGoodByShopIdAndGoodStatusCount(s.getId()));
        }


        PageInfo<ShopVO> pageInfo = new PageInfo<>(voList);
        pageInfo.setTotal(((Page) shopEntity).getTotal());
        return CommonUtil.copyFromPageInfo(pageInfo, voList);

    }

    @Transactional
    public void updateShopLevel(String shopId, Integer level) {
        ShopEntity newEntity = new ShopEntity();
        newEntity.setId(shopId);
        newEntity.setShopLevel(level + "");
        shopDAO.updateSelective(newEntity);
    }

    @Transactional
    public void updateOrderOperation(String shopId, Boolean enable) {
        ShopEntity newEntity = new ShopEntity();
        newEntity.setId(shopId);
        newEntity.setWhetherLogistics(enable ? "1" : "0");
        shopDAO.updateSelective(newEntity);
    }

    @Transactional
    public void updateWithdrawalOperation(String shopId, Boolean enable) {
        ShopEntity newEntity = new ShopEntity();
        newEntity.setId(shopId);
        newEntity.setWhetherPay(enable ? "1" : "0");
        shopDAO.updateSelective(newEntity);
    }

    @Transactional
    public String createShop(ShopVO shopVO, String userId) throws BusinessException, SQLException {
        UserEntity user = userDAO.getByMobile(shopVO.getMobile());
        A.check(user == null, "用户不存在:" + shopVO.getMobile());
        List<ShopEntity> oldEntity = shopDAO.getMyOnLineShopEntity(user.getId());
        A.check(!oldEntity.isEmpty(), "此用户已经存在门店");
        String shopId = shopDAO.getShopIdByShopNo(shopVO.getShopNo());
        A.check(shopId != null, "同样编号的门店已经存在");


        ShopEntity entity = new ShopEntity();
        BeanUtils.copyProperties(shopVO, entity);
        entity.setUserId(user.getId());
        entity.setId(IDUtil.getUUID());
        entity.setShopPoint("0");
        entity.setShopConsume(BigDecimal.ZERO);
        entity.setStatus(Constants.ShopStatus.SHOP_PASS);
        entity.setShopType(Constants.ShopType.SHOP_ONLINE);
        entity.setName(user.getTrueName());
        BigDecimal big = userDAO.queryAvailableBalance(user.getId());
        if (big == null) {
            entity.setShopSalesAmount(BigDecimal.ZERO);
        } else {
            entity.setShopSalesAmount(big);
        }
        entity.setWhetherPay(shopVO.isEnableWithdrawalOperation() ? "1" : "0");
        entity.setWhetherLogistics(shopVO.isEnableOrderOperation() ? "1" : "0");

        int count = shopDAO.addShopEntity(entity);
        A.check(count <= 0, "创建店铺失败");
        //创建店铺查询用户信用金 修改用户销售总额
        if (big.compareTo(BigDecimal.ZERO) > 0) {
            cn.kt.mall.common.wallet.entity.ShopEntity shop = new cn.kt.mall.common.wallet.entity.ShopEntity();
            shop.setId(entity.getId());
            shop.setShopSalesAmount(entity.getShopSalesAmount());
            int row = shopScalesDAO.updateShopSalesAmount(shop);
            A.check(row == 0, "修改店铺销售额度失败");
            ShopSalesAmountVO shopSalesAmountVO = new ShopSalesAmountVO();
            shopSalesAmountVO.setId(IDUtil.getUUID());
            shopSalesAmountVO.setUserId(user.getId());
            shopSalesAmountVO.setShopSalesAmountBefore(BigDecimal.ZERO);
            shopSalesAmountVO.setShopSalesAmountChange(entity.getShopSalesAmount());
            shopSalesAmountVO.setShopSalesAmountAfter(entity.getShopSalesAmount());
            shopSalesAmountVO.setOperateType(TradeType.SHOP_ASSETS.getType().toString());
            shopSalesAmountVO.setReferenceId(userId);
            int counts = shopScalesDAO.addShopScalesLog(shopSalesAmountVO);
            A.check(counts == 0, "添加店铺资金流水失败");
        }


        //修改用户级别, 目前用户级别和门店级别是1-1对应的关系
        Integer newUserLevel = Integer.valueOf(shopVO.getShopLevel()) + 1;
        if (Integer.valueOf(user.getLevel()) < newUserLevel) {
            //如果当前用户的级别比门店级别对应的用户级别低，则调高会员级别
            UserEntity newEntity = new UserEntity();

            int rows = userDAO.updateUserLevel(user.getId(), newUserLevel + "");
            A.check(rows == 0, "无法调整会员级别");
        }

        List<cn.kt.mall.shop.good.entity.GoodEntity> goodsList = goodDAO.getGoodsByKtList("kt");
        if (goodsList != null && goodsList.size() > 0) {
            goodService.addGoodsToShop(entity.getId(), goodsList);
        } else {
            A.check(goodsList == null, "后台暂无商品,不能添加门店");
        }

        return entity.getId();
    }

    /**
     * @param userId   用户ID
     * @param name     店长
     * @param shopName 店铺名称
     * @param pageNo
     * @param pageSize
     * @return
     */
    public CommonPageVO<ShopVO> getShopListByPid(String userId, String name, String shopName, String shopNo, int pageNo, int pageSize) {
        List<String> userIdList = new ArrayList<>();
        List<UserEntity> userEntityList = userDAO.getUserByPid(userId);
        for (UserEntity userEntity : userEntityList) {
            userIdList.add(userEntity.getId());
        }
        if (userEntityList.size() > 0 && userEntityList != null) {
            List<ShopVO> voList= shopDAO.getShopListByPid(userIdList, name, shopName, shopNo, new RowBounds(pageNo, pageSize));
//            List<ShopVO> voList = ShopVO.fromEntity(shopEntityList);
            for (ShopVO shop : voList) {
                ShopVO shopVO = shopDAO.getShopIdGoodCountAndAchievement(shop.getId());
                shop.setGoodCounts(shopVO.getGoodCounts());
                shop.setIncomeLem(shopVO.getIncomeLem());
                //查询店铺用户的信用金
                UserAssetEntity userAssetEntity = userAssetDAO.getAssetByUserIdAndCurrency(shop.getUserId(), "point");
                shop.setPoint(userAssetEntity.getAvailableBalance());
            }

            PageInfo<ShopVO> pageInfo = new PageInfo<>(voList);
            return CommonUtil.copyFromPageInfo(pageInfo, voList);
        } else {
            List<ShopVO> voList = new ArrayList<>();
            PageInfo<ShopVO> pageInfo = new PageInfo<>(voList);
            return CommonUtil.copyFromPageInfo(pageInfo, voList);
        }


    }

    /**
     * 根据shopId获得店铺信息
     */
    public ShopVO getShopListByShopId(String shopId) {
        ShopEntity shopEntity = shopDAO.getShopEntityByShopId(shopId);
        ShopVO shopVO = new ShopVO();
        BeanUtils.copyProperties(shopEntity, shopVO);
        ShopVO vo = shopDAO.getShopIdGoodCountAndAchievement(shopId);
        shopVO.setGoodCounts(vo.getGoodCounts());
        return shopVO;
    }

    public ShopVO getShopIdGoodCountAndAchievement(String shopId) {
        return shopDAO.getShopIdGoodCountAndAchievement(shopId);
    }

    public String getShopByIDorPID(String userId, String userPid) {
        String shopId = shopDAO.getShopByIDorPID(userId, userPid);
        return shopId;
    }

    public CommonPageVO<ShopSalesVO> getShopSalesRecord(String myId, String shopId, String startTime, String endTime, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<ShopSalesVO> list = shopDAO.shopSalesRecord(shopId, startTime, endTime, rowBounds);
        ShopSalesAndTimeVO timeVO = shopDAO.getShopSalesAndPointAndCoupon(shopId);
        for (ShopSalesVO shopSalesVO : list) {
            if (timeVO != null) {
                shopSalesVO.setTotalPrice(timeVO.getSale());
            } else {
                shopSalesVO.setTotalPrice(new BigDecimal(0));
            }
            UserEntity u = userDAO.getById(myId);
            if (!u.getPid().equals(shopSalesVO.getBuyUserId())) {
                shopSalesVO.setRelationship("无");
            } else {
                shopSalesVO.setRelationship("下级");
            }
        }

        //查询关系
        PageInfo<ShopSalesVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    /**
     * 查询商品销售记录---导出
     *
     * @param shopTradeGoodSalesReqVO
     * @return
     */
    public List<ShopTradeGoodSalesVO> getShopTradeGoodSalesList(ShopTradeGoodSalesReqVO shopTradeGoodSalesReqVO) {
        List<ShopTradeGoodSalesVO> list = new ArrayList<>();
        try {
            list = shopDAO.shopSalesRecordExport(shopTradeGoodSalesReqVO);
            for (ShopTradeGoodSalesVO vo : list) {
                if (vo.getShopLevel().equals("2")) {
                    vo.setShopLevel("零售店");
                } else if (vo.getShopLevel().equals("3")) {
                    vo.setShopLevel("批发店");
                } else vo.setShopLevel("未知类型");
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
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
     * 查询店铺商品销量
     *
     * @param shopId
     * @param startTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    public CommonPageVO<ShopTradeGoodSalesVO> getShopGoodsCountSales(String shopId, String goodName, String startTime, String endTime, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        ShopTradeGoodSalesVO vo = shopDAO.getShopGoodsSales(shopId);
        List<ShopTradeGoodSalesVO> list = shopDAO.getShopGoodsCountSales(shopId, goodName, startTime, endTime, rowBounds);
        for (ShopTradeGoodSalesVO salesVO : list) {
            if (vo != null) {
                salesVO.setShopTotalPoint(vo.getShopTotalPoint());
            } else {
                salesVO.setShopTotalPoint(new BigDecimal(0));
            }

        }
        PageInfo<ShopTradeGoodSalesVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    /**
     * admin---查询店铺商品销量
     *
     * @param shopType
     * @param shopName
     * @param skuName
     * @param startTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    public CommonPageVO<ShopTradeGoodSalesVO> getShopGoodsCountSale(String shopType, String shopName, String skuName, String startTime, String endTime, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<ShopTradeGoodSalesVO> list = shopDAO.shopSalesRecordExport(shopType, shopName, skuName, startTime, endTime, rowBounds);
        // BigDecimal totalPrice = new BigDecimal("0");
        ShopTradeGoodSalesReqVO shopTradeGoodSalesReqVO = new ShopTradeGoodSalesReqVO(startTime,endTime,shopType,skuName,shopName);
        //List<ShopTradeGoodSalesVO> listVo = shopDAO.shopSalesRecordExport(shopTradeGoodSalesReqVO);
        BigDecimal totalPrice =  shopDAO.shopSalesRecordTotalPrice(shopTradeGoodSalesReqVO);
        /*for (ShopTradeGoodSalesVO vo : listVo) {
            totalPrice = totalPrice.add(vo.getPrice());
        }*/

        for (ShopTradeGoodSalesVO salesVO : list) {
            salesVO.setTotalPoint(totalPrice);
        }
        PageInfo<ShopTradeGoodSalesVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    /**
     * @param myId
     * @param startTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    public CommonPageVO<ShopStatisticsVO> getShopStatisticsList(String myId, String startTime, String endTime, String mobile, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        if (myId != null) {
            //查询我的直属下属
            List<String> shopIds = userDAO.getUserShopIdByPid(myId);
            ShopEntity shopEntity = shopDAO.getMyShopEntity(myId).get(0);
            shopIds.add(shopEntity.getId());
            //查询直属下级
            List<ShopStatisticsVO> list = shopDAO.getShopStatisticsList(shopIds, startTime, endTime, mobile, rowBounds);
            for (ShopStatisticsVO vo : list) {
                List<UserEntity> userSize = userDAO.getUserByPid(vo.getUserId());
                if (userSize == null || userSize.size() <= 0) {
                    vo.setTeamCount(new BigDecimal(0));
                } else {
                    vo.setTeamCount(new BigDecimal(userSize.size()));
                }
                UserEntity user = userDAO.getById(vo.getUserId());
                if (user == null) {
                    vo.setPersonCount("0");
                } else {
                    vo.setPersonCount(user.getTeamCount() + "");
                }
            }
            PageInfo<ShopStatisticsVO> pageInfo = new PageInfo<>(list);
            return CommonUtil.copyFromPageInfo(pageInfo, list);

        }
        return null;
    }

    /**
     * 查询业绩与商铺合并后列表
     *
     * @return
     */
    public CommonPageVO<ShopStatisticsVO> getShopStatisticss(String shopType, String beginTime, String endTime, String shopNo, String shopName, String userName, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<ShopStatisticsVO> list = shopDAO.getShopStatisticss(shopType, beginTime, endTime, shopNo, shopName, userName, rowBounds);
        for (ShopStatisticsVO vo : list) {
            //获取团队总人数
            /*Map<String, Object> UserMap = new HashMap<String, Object>();
            UserMap = this.getUserPid(vo.getUserId(), UserMap);
            if (UserMap != null) {
                vo.setPersonCount(String.valueOf(UserMap.size()));
            }*/
            //获取团队增长人数：直属下属
            int userSize = userDAO.getCountByPid(vo.getUserId());
            //List<UserEntity> userSize = userDAO.getUserByPid(vo.getUserId());
            if (userSize <= 0) {
                vo.setTeamCount(new BigDecimal(0));
            } else {
                vo.setTeamCount(new BigDecimal(userSize));
            }
        }
        PageInfo<ShopStatisticsVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    /**
     * 获取自资金明细
     *
     * @param beginTime
     * @param endTime
     * @param phone
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */

    public CommonPageVO<CashRecordVO> getFundDetailListByShop(String myId, String operatorUserPhone, String beginTime, String endTime, String phone, String status, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<String> userIdList = new ArrayList<>();
        userIdList.add(myId);
        List<UserEntity> userEntityList = userDAO.getUserByPid(myId);
        for (UserEntity userEntity : userEntityList) {
            userIdList.add(userEntity.getId());
        }
//        Map<String, Object> UserMap = new HashMap<String, Object>();
//        UserMap = this.getUserPid(myId, UserMap);
//        for (String key : UserMap.keySet()) {
//            userIdList.add(key);
//        }
        List<cn.kt.mall.common.wallet.vo.CashRecordVO> list = userRechargeLogDAO.getFundDetailListByShop(userIdList, operatorUserPhone, beginTime, endTime, phone, status, rowBounds);
        for (CashRecordVO cashRecordVO : list) {
            cashRecordVO = getInfo(cashRecordVO);
        }
        PageInfo<cn.kt.mall.common.wallet.vo.CashRecordVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    /**
     * @param userId   用户ID
     * @param name     店长
     * @param shopName 店铺名称
     * @return
     */
    public List<ShopVO> getShopListByPidAndExport(String userId, String name, String shopName, String shopNo) {
        List<String> userIdList = new ArrayList<>();
        List<UserEntity> userEntityList = userDAO.getUserByPid(userId);
        for (UserEntity userEntity : userEntityList) {
            userIdList.add(userEntity.getId());
        }
        if (userEntityList.size() > 0 && userEntityList != null) {
            List<ShopVO> voList = shopDAO.getShopListByPidAndExport(userIdList, name, shopName, shopNo);
//            List<ShopVO> voList = ShopVO.fromEntity(shopEntityList);
            for (ShopVO shop : voList) {
                ShopVO shopVO = shopDAO.getShopIdGoodCountAndAchievement(shop.getId());
                shop.setGoodCounts(shopVO.getGoodCounts());
                shop.setIncomeLem(shopVO.getIncomeLem());
                //查询店铺用户的信用金
                UserAssetEntity userAssetEntity = userAssetDAO.getAssetByUserIdAndCurrency(shop.getUserId(), "point");
                shop.setPoint(userAssetEntity.getAvailableBalance());
            }

//            if (!shopEntityList.isEmpty()) {
//                List<UserEntity> userList = userDAO.getByIdList(shopEntityList.stream().map(s -> s.getUserId()).collect(Collectors.toList()));
//                Map<String, UserEntity> userId2Map = userList.stream().collect(Collectors.toMap(UserEntity::getId, Function.identity()));
//
//                for (ShopVO s : voList) {
//                    UserEntity user = userId2Map.get(s.getUserId());
//                    if (user != null) {
//                        s.setName(user.getTrueName());
//                    }
//                }
//            }

            return voList;
        } else {
            List<ShopVO> voList = new ArrayList<>();
            return voList;
        }


    }

    /**
     * 导出店铺
     *
     * @param shopStatisticsReqVO
     * @return
     */
    public List<ShopStatisticsVO> getStatisticsList(ShopStatisticsReqVO shopStatisticsReqVO) {
        List<ShopStatisticsVO> list = shopDAO.getShopStatisticss(shopStatisticsReqVO);
        for (ShopStatisticsVO vo : list) {
            //获取团队总人数
            /*Map<String, Object> UserMap = new HashMap<String, Object>();
            UserMap = this.getUserPid(vo.getUserId(), UserMap);
            if (UserMap != null) {
                vo.setPersonCount(String.valueOf(UserMap.size()));
            }*/
            //获取团队增长人数：直属下属
            int userSize = userDAO.getCountByPid(vo.getUserId());
            //List<UserEntity> userSize = userDAO.getUserByPid(vo.getUserId());
            if (userSize <= 0) {
                vo.setTeamCount(new BigDecimal(0));
            } else {
                vo.setTeamCount(new BigDecimal(userSize));
            }
            //翻译店铺类型
            if (vo.getShopLevel().equals("2")) {
                vo.setShopLevel("零售店");
            } else if (vo.getShopLevel().equals("3")) {
                vo.setShopLevel("批发店");
            } else vo.setShopLevel("未知类型");
            if (vo != null) {
                if (vo.getName() != null) {
                    vo.setName(vo.getName());
                    if (vo.getMobile() != null) {
                        vo.setName(vo.getName() + vo.getMobile());
                    }
                } else {
                    if (vo.getMobile() != null) {
                        vo.setName(vo.getMobile());
                    }
                }
            }

        }
        return list;
    }

    /**
     * 编辑店铺开启&关闭状态（批量）
     *
     * @param ids    店铺id集合：使用,分隔
     * @param status 状态：1开启，3管理员禁用
     */
    public void editShopStatus(String ids, String status) {
        A.check((null == ids || ids.equals("")), "请选择店铺");
        String[] idArr = ids.split(",");
        List<String> idsList = null;
        if (idArr.length > 0) {
            idsList = Arrays.asList(idArr);
        }
        int updateCount = shopDAO.editShopStatus(status, idsList);
        if (status.equals("1")) {
            A.check(updateCount <= 0, "开启失败");
        } else {
            A.check(updateCount <= 0, "关闭失败");
        }
    }


    public CashRecordVO getInfo(CashRecordVO cashRecordVO) {
        UserEntity user = userDAO.getById(cashRecordVO.getRechargeUser());
        if (user != null) {
            cashRecordVO.setTrueName(user.getTrueName());
            cashRecordVO.setPhone(user.getUsername());
        } else {
            cashRecordVO.setTrueName("未查询到该用户");
        }
        //获取操作人信息
        UserEntity userEntity = userDAO.getById(cashRecordVO.getUserId());
        if (userEntity != null) {
            cashRecordVO.setOpreatorUser(userEntity.getTrueName());
            cashRecordVO.setLoginName(userEntity.getMobile());

        }
        return cashRecordVO;
    }

    // 商城后台-商品销售记录 begin
    public List<ShopTradeGoodSalesVO> getShopGoodsCountSalesExport(String shopId,
                                                                   String goodName,
                                                                   String startTime,
                                                                   String endTime) {
        return shopDAO.getShopGoodsCountSalesExport(shopId, goodName, startTime, endTime);
    }

    public List<ShopSalesVO> shopSalesRecordByShopExport(String myId, String shopId, String startTime, String endTime) {
        List<ShopSalesVO> list = shopDAO.shopSalesRecordByShopExport(shopId, startTime, endTime);
        for (ShopSalesVO shopSalesVO : list) {
            UserEntity u = userDAO.getById(myId);
            if (!u.getPid().equals(shopSalesVO.getBuyUserId())) {
                shopSalesVO.setRelationship("无");
            } else {
                shopSalesVO.setRelationship("下级");
            }

        }
        return list;
    }

    public List<ShopStatisticsVO> getShopStatisticsListByShopExportt(String myId, String startTime, String endTime, String mobile) {
        //查询我的直属下属
        List<String> shopIds = userDAO.getUserShopIdByPid(myId);
        ShopEntity shopEntity = shopDAO.getMyShopEntity(myId).get(0);
        shopIds.add(shopEntity.getId());
        //查询直属下级
        List<ShopStatisticsVO> list = shopDAO.getShopStatisticsListByShopExportt(shopIds, startTime, endTime, mobile);
        for (ShopStatisticsVO vo : list) {
            List<UserEntity> userSize = userDAO.getUserByPid(vo.getUserId());
            if (userSize == null || userSize.size() <= 0) {
                vo.setTeamCount(new BigDecimal(0));
            } else {
                vo.setTeamCount(new BigDecimal(userSize.size()));
            }
            UserEntity user = userDAO.getById(vo.getUserId());
            if (user == null) {
                vo.setPersonCount("0");
            } else {
                vo.setPersonCount(user.getTeamCount() + "");
            }

        }
        return list;

    }


    public List<CashRecordVO> getFundDetailListByShopExport(String myId, String operatorUserPhone, String beginTime, String endTime, String phone, String status) {
        List<String> userIdList = new ArrayList<>();
        userIdList.add(myId);
        List<UserEntity> userEntityList = userDAO.getUserByPid(myId);
        for (UserEntity userEntity : userEntityList) {
            userIdList.add(userEntity.getId());
        }
        List<cn.kt.mall.common.wallet.vo.CashRecordVO> list = userRechargeLogDAO.getFundDetailListByShopExport(userIdList, operatorUserPhone, beginTime, endTime, phone, status);
        for (CashRecordVO cashRecordVO : list) {
            cashRecordVO = getInfo(cashRecordVO);
        }
        return list;
    }

    public String getShopIdByShopNo(String shopNo) {

        return shopDAO.getShopIdByShopNo(shopNo);
    }

    //根据时间分组查询店铺业绩
    public List<ShopSalesAndTimeVO> getShopSalesAndTime(String shopId, String startTime, String endTime) {
        return shopDAO.getShopSalesAndTime(shopId, startTime, endTime);
    }


    //查询店铺总收入
    public ShopSalesAndTimeVO getShopSalesAndPointAndCoupon(String shopId) {
        return shopDAO.getShopSalesAndPointAndCoupon(shopId);
    }

    //修改店铺名称与编号
    public void updateShopName(ShopEntity shop) {
        //1.非空校验
        A.check(StringUtils.isEmpty(shop.getId()), "未接收到参数:主键ID");
        A.check(StringUtils.isEmpty(shop.getShopName()), "商铺名称不能为空");
        A.check(StringUtils.isEmpty(shop.getShopNo()), "商铺ID不能为空");
        //2.店铺编号重复校验
        String shopNoStr = shopDAO.getShopIdByShopNo(shop.getShopNo());
        A.check(!StringUtils.isEmpty(shopNoStr) && !shopNoStr.equals(shop.getId()), "该商品编号已被占用");
        //3.长度校验
        A.check(shop.getShopName().length() > 32,"商铺名称长度过长");
        A.check(shop.getShopNo().length() > 40, "商铺ID长度过长");
        int updateCount = 0;
        updateCount = shopDAO.updateShopInfo(shop);
        A.check(updateCount <= 0, "修改店铺信息失败");
    }
}