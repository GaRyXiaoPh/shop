package cn.kt.mall.shop.trade.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import cn.kt.mall.common.common.entity.LevleConfig;
import cn.kt.mall.common.common.mapper.SysSettingsMapper;
import cn.kt.mall.common.user.vo.UserConsumeVO;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.enums.AssetType;
import cn.kt.mall.common.wallet.mapper.ShopScalesDAO;
import cn.kt.mall.common.wallet.service.ShopSalesAmountService;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.shop.coupon.entity.CouponEntity;
import cn.kt.mall.shop.coupon.entity.UserCouponEntity;
import cn.kt.mall.shop.coupon.entity.UserCouponLogEntity;
import cn.kt.mall.shop.coupon.entity.UserReturnCouponLogEntity;
import cn.kt.mall.shop.coupon.mapper.CouponsDAO;
import cn.kt.mall.shop.coupon.mapper.UserReturnCouponLogDAO;
import cn.kt.mall.shop.coupon.vo.CouponsVO;
import cn.kt.mall.shop.good.entity.GoodCouponCenterEntity;
import cn.kt.mall.shop.good.mapper.GoodCouponCenterDao;
import cn.kt.mall.shop.good.service.GoodService;
import cn.kt.mall.shop.good.vo.GoodPayReqVO;
import cn.kt.mall.shop.logistics.mapper.ShopGoodTransportDAO;
import cn.kt.mall.shop.logistics.vo.ShopTransportVO;
import cn.kt.mall.shop.trade.mapper.ShopTradeDAO;
import cn.kt.mall.shop.trade.vo.*;
import io.shardingjdbc.core.api.HintManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageInfo;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.bitcoin.service.LemService;
import cn.kt.mall.common.common.SerialCodeGenerator;
import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.constant.CommonConstants;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.sms.service.SmsService;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.DateUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.service.LemRateService;
import cn.kt.mall.common.wallet.service.StatementService;
import cn.kt.mall.common.wallet.vo.CountStatementVO;
import cn.kt.mall.mq.send.TradeMQSend;
import cn.kt.mall.shop.address.mapper.AddressDAO;
import cn.kt.mall.shop.address.vo.AddressVO;
import cn.kt.mall.shop.cart.mapper.CartDAO;
import cn.kt.mall.shop.city.service.CityService;
import cn.kt.mall.shop.city.vo.CityRespVO;
import cn.kt.mall.shop.config.SysConfig;
import cn.kt.mall.shop.good.entity.GoodEntity;
import cn.kt.mall.shop.good.mapper.GoodDAO;
import cn.kt.mall.shop.logistics.service.LogisticsService;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.mapper.ShopDAO;
import cn.kt.mall.shop.trade.constant.Constants;
import cn.kt.mall.shop.trade.entity.TradeEntity;
import cn.kt.mall.shop.trade.entity.TradeItemEntity;
import cn.kt.mall.shop.trade.entity.TradeLogEntity;
import cn.kt.mall.shop.trade.mapper.TradeDAO;
import cn.kt.mall.shop.trade.mapper.TradeItemDAO;
import cn.kt.mall.shop.trade.mapper.TradeLogDAO;

@Service
public class TradeService {
    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);
    @Autowired
    UserDAO userDAO;
    @Autowired
    AddressDAO addressDAO;
    @Autowired
    TradeDAO tradeDAO;
    @Autowired
    GoodDAO goodDAO;
    @Autowired
    ShopDAO shopDAO;
    @Autowired
    ShopTradeDAO shopTradeDAO;
    @Autowired
    StatementService statementService;
    @Autowired
    CartDAO cartDAO;
    @Autowired
    TradeItemDAO tradeItemDAO;
    @Autowired
    TradeLogDAO tradeLogDAO;
    @Autowired
    LemRateService lemRateService;
    @Autowired
    LemService lemService;
    @Autowired
    SysConfig sysConfig;
    @Autowired
    UserAssetService userAssetService;

    @Autowired
    private CityService cityService;
    @Autowired
    private LogisticsService logisticsService;
    @Autowired
    private SysSettingsService sysSettingsService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private GoodCouponCenterDao goodCouponCenterDao;
    @Autowired
    private CouponsDAO couponsDAO;
    @Autowired
    private UserReturnCouponLogDAO userReturnCouponLogDAO;
    @Autowired
    private SysSettingsMapper sysSettingsMapper;
    @Autowired
    private ShopGoodTransportDAO shopGoodTransportDAO;
    @Autowired
    private ShopSalesAmountService shopSalesAmountService;
    @Autowired
    private ShopScalesDAO shopScalesDAO;
    @Autowired
    private GoodService goodService;


    private static ReentrantLock lock = new ReentrantLock();

    public String getAddressByTradeId(String tradeId) {
        TradeEntity tradeEntity = tradeDAO.getTradeById(null, tradeId);
        A.check(tradeEntity == null, "订单不存在");
        CityRespVO cityRespVO = cityService.getDataById(tradeEntity.getProvince(), tradeEntity.getCity(),
                tradeEntity.getCounty());
        if (cityRespVO != null) {
            return cityRespVO.getProvinceValue() + cityRespVO.getCityValue() + cityRespVO.getCountyValue()
                    + tradeEntity.getDetailAddress();
        }
        return tradeEntity.getDetailAddress();
    }

    // 获取单个订单详情
    public TradeVO detailsTrade(String shopId, String userId, String tradeId) {
        TradeEntity tradeEntity = tradeDAO.getTradeById(userId, tradeId);
        A.check(tradeEntity == null, "订单不存在");
//        if (StringUtils.isNotEmpty(shopId)) {
//            A.check(!tradeEntity.getShopId().equals(shopId), "订单不存在");
//        }
        ShopEntity shopEntity = shopDAO.getShopEntityByShopId(tradeEntity.getShopId());
        TradeVO tradeVO = new TradeVO();
        BeanUtils.copyProperties(tradeEntity, tradeVO);
        tradeVO.setShopNo(shopEntity.getShopNo());
        tradeVO.setShopEntity(shopEntity);
        UserEntity user = userDAO.getById(tradeEntity.getBuyUserId());
        if (user != null) {
            tradeVO.setBuyUserName(user.getUsername());
            tradeVO.setUserLevel(user.getUserlevel() + "");
        }


        UserEntity shopUser = userDAO.getById(shopEntity.getUserId());
        A.check(shopUser == null, "店长不存在");

        tradeVO.setShopUserTrueName(shopUser.getTrueName());
      /*  CityRespVO cityRespVO = cityService.getDataById(tradeEntity.getProvince(), tradeEntity.getCity(),
                tradeEntity.getCounty());
        if (cityRespVO != null) {
            tradeVO.setProvinceValue(cityRespVO.getProvinceValue());
            tradeVO.setCityValue(cityRespVO.getCityValue());
            tradeVO.setCountyValue(cityRespVO.getCountyValue());
        }*/
        List<TradeItemEntity> tradeItemList = tradeItemDAO.getTradeItemListByTradeIdAndShopId(tradeId, tradeEntity.getShopId());
        List<TradeLogEntity> tradeLogEntityList = tradeLogDAO.getByTradeId(tradeId);
        List<TradeItemVO> itemList = new ArrayList<>();
        List<UserCouponLogEntity> userCouponLogEntityList = couponsDAO.getGoodCollectEntityByTradeId(tradeId);
        for (TradeItemEntity tradeItemEntity : tradeItemList) {
            TradeItemVO tradeItemVO = new TradeItemVO();
            BeanUtils.copyProperties(tradeItemEntity, tradeItemVO);
            List<UserCouponLogEntity> list = new ArrayList<>();
            for (UserCouponLogEntity userCouponLogEntity : userCouponLogEntityList) {
                if (userCouponLogEntity.getGoodid().equals(tradeItemVO.getGoodId())) {
                    list.add(userCouponLogEntity);
                }
            }
            if (!tradeItemVO.getStatus().equals("1")) {
                tradeItemVO.setInvalidFlag("1");
            } else {
                tradeItemVO.setInvalidFlag("0");
            }
            tradeItemVO.setUserCouponLogEntityList(list);
            itemList.add(tradeItemVO);
        }
        //查询历史优惠券
        List<UserCouponLogEntity> userCouponLog = couponsDAO.getGoodCollectEntityByTradeIdAndShop(tradeId);
        if (userCouponLog != null) {
            tradeVO.setCouponExplain(userCouponLog);
        }
        //订单关联优惠卷
        tradeVO.setShopName(shopEntity.getShopName());
        tradeVO.setTradeGoods(itemList);
        tradeVO.setTradeLogEntityList(tradeLogEntityList);
        return tradeVO;
    }

    // 我的订单
    public PageVO<TradeVO> myTrade(String userId, String status, int pageNo, int pageSize) {
        int srcPageNo = pageNo;
        if (pageNo >= 1)
            pageNo = pageNo - 1;
        int offset = pageNo * pageSize;

        int count = tradeDAO.myTradeCount(userId, status);
        List<TradeEntity> tradeList = tradeDAO.myTrade(userId, status, offset, pageSize);
        List<TradeVO> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tradeList)) {
            List<String> tradeIds = new ArrayList<>();
            List<String> shopIds = new ArrayList<>();
            for (TradeEntity tradeEntity : tradeList) {
                tradeIds.add(tradeEntity.getId());
                shopIds.add(tradeEntity.getShopId());
            }
            List<ShopEntity> shopList = shopDAO.listShopByIds(shopIds, null, null);
            List<TradeItemEntity> tradeItemList = tradeItemDAO.getTradeItemListByIds(tradeIds);
            List<TradeLogEntity> tradeLogList = tradeLogDAO.getByTradeIds(tradeIds);
            buildTradeList(result, tradeList, shopList, tradeItemList, tradeLogList);
        }
        return new PageVO<>(srcPageNo, pageSize, count, result);
    }

    // 构建我的订单数据
    private void buildTradeList(List<TradeVO> result, List<TradeEntity> tradeList, List<ShopEntity> shopList,
                                List<TradeItemEntity> tradeItemList, List<TradeLogEntity> tradeLogList) {
        for (TradeEntity tradeEntity : tradeList) {
            TradeVO tradeVO = new TradeVO();
            BeanUtils.copyProperties(tradeEntity, tradeVO);
            for (ShopEntity shopEntity : shopList) {
                if (shopEntity.getId().equals(tradeEntity.getShopId())) {
                    tradeVO.setShopName(shopEntity.getShopName());
                    break;
                }
            }
            List<TradeItemVO> tradeItemVOList = new ArrayList<>();
            for (TradeItemEntity tradeItemEntity : tradeItemList) {
                if (tradeItemEntity.getTradeId().equals(tradeEntity.getId())) {
                    TradeItemVO tradeItemVO = new TradeItemVO();
                    BeanUtils.copyProperties(tradeItemEntity, tradeItemVO);
                    tradeItemVOList.add(tradeItemVO);
                }
            }
            List<TradeLogEntity> tempLogList = new ArrayList<>();
            for (TradeLogEntity tradeLogEntity : tradeLogList) {
                if (tradeLogEntity.getTradeId().equals(tradeEntity.getId())) {
                    tempLogList.add(tradeLogEntity);
                }
            }
            tradeVO.setTradeGoods(tradeItemVOList);
            tradeVO.setTradeLogEntityList(tempLogList);
            result.add(tradeVO);
        }
    }

    // 支付订单
    @Transactional
    public boolean payTradeByTradeNo(String userId, String tradeNo) {

        TradeEntity tradeEntity = tradeDAO.getTradeByTradeNo(userId, tradeNo);
        A.check(tradeEntity == null, "无效的支付订单");
        A.check(!Constants.TradeStatus.TRADE_NOTPAY.equals(tradeEntity.getStatus()), "无效的订单状态");

        // 计算总共支付 = 商品基本金额+运费
        BigDecimal total = tradeEntity.getTotalPrice().add(tradeEntity.getTotalFreightFree());
        A.check(total.compareTo(Constants.BASE_FREE) < 0, "订单总金额为负数，状态不正常");
        BigDecimal lemTotal = Constants.BASE_FREE;
        if (total.compareTo(Constants.BASE_FREE) > 0) {
            lemTotal = total.divide(new BigDecimal(lemRateService.getLemRate().toString()), Constants.SCALE,
                    RoundingMode.HALF_UP).setScale(Constants.SCALE, RoundingMode.HALF_UP);
            // 检查金额
            A.check(userAssetService.getUserAssetByCurrency(userId, AssetType.CREDIT.getStrType()).getAvailableBalance().compareTo(lemTotal) < 0, "账户余额不足");
        }

        tradeDAO.updateTradeCoinAndStatus(tradeEntity.getId(), lemTotal, Constants.TradeStatus.TRADE_NOTSEND);
        tradeLogDAO.insert(new TradeLogEntity(tradeEntity.getShopId(), tradeEntity.getId(), (short) 1,
                Constants.TradeLogCode.LOG_PAY, Constants.TradeLogCode.LOG_PAY_VALUE));
        if (lemTotal.compareTo(Constants.BASE_FREE) > 0) {
            // 冻结相应数量的莱姆币
            userAssetService.updateUserAsset(userId, AssetType.CREDIT.getStrType(), lemTotal.negate(), lemTotal, TradeType.PAY_ORDER, tradeNo);
        }
        return true;
    }

    // 批量支付订单
    @Transactional
    public void payTradeByInteriorNo(String userId, String interiorNo) {
        // 根据用户ID和内部订单号支付
        List<TradeEntity> list = tradeDAO.getTradeByInteriorNo(userId, interiorNo);
        List<String> tradeIds = new ArrayList<>();
        BigDecimal total = Constants.BASE_FREE;
        BigDecimal rate = new BigDecimal(lemRateService.getLemRate()).setScale(Constants.SCALE, RoundingMode.HALF_UP);
        for (int i = 0; i < list.size(); i++) {
            TradeEntity entity = list.get(i);
            A.check(entity == null, "无效的支付订单");
            A.check(!Constants.TradeStatus.TRADE_NOTPAY.equals(entity.getStatus()), "无效的订单状态");
            A.check(entity.getTotalFreightFree().compareTo(Constants.BASE_FREE) < 0
                    || entity.getTotalPrice().compareTo(Constants.BASE_FREE) < 0, "订单数据异常");
            BigDecimal tempPrice = entity.getTotalFreightFree().add(entity.getTotalPrice()).setScale(Constants.SCALE,
                    RoundingMode.HALF_UP);
            total = total.add(tempPrice).setScale(Constants.SCALE, RoundingMode.HALF_UP);
            tradeIds.add(list.get(i).getId());
            rate = entity.getLemRate();
        }


        BigDecimal lemTotal = Constants.BASE_FREE;

        if (total.compareTo(Constants.BASE_FREE) > 0) {
            lemTotal = total.divide(rate, Constants.SCALE, RoundingMode.HALF_UP).setScale(Constants.SCALE,
                    RoundingMode.HALF_UP);
            // 检查金额
            A.check(userAssetService.getUserAssetByCurrency(userId, AssetType.CREDIT.getStrType()).getAvailableBalance().compareTo(lemTotal) < 0, "账户余额不足");
            // 支付的时候进行冻结处理
            userAssetService.updateUserAsset(userId, AssetType.CREDIT.getStrType(), lemTotal.negate(), lemTotal, TradeType.PAY_ORDER, interiorNo);
        }

        // 更新订单状态
        for (int i = 0; i < list.size(); i++) {
            TradeEntity entity = list.get(i);
            BigDecimal tempPrice = entity.getTotalFreightFree().add(entity.getTotalPrice());
            BigDecimal tempLemPrice = tempPrice.divide(rate, Constants.SCALE, RoundingMode.HALF_UP)
                    .setScale(Constants.SCALE, RoundingMode.HALF_UP);
            tradeDAO.updateTradeCoinAndStatus(entity.getId(), tempLemPrice, Constants.TradeStatus.TRADE_NOTSEND);
            tradeLogDAO.insert(new TradeLogEntity(entity.getShopId(), entity.getId(), (short) 1,
                    Constants.TradeLogCode.LOG_PAY, Constants.TradeLogCode.LOG_PAY_VALUE));
        }
    }

    // 取消订单
    @Transactional
    public void cancelTrade(String shopId, String userId, String id) {
        TradeEntity entity = tradeDAO.getTradeById(userId, id);
        A.check(entity == null, "无效订单");
        if (StringUtils.isNotEmpty(shopId)) {
            A.check(!entity.getShopId().equals(shopId), "订单不存在");
        }
        A.check(!Constants.TradeStatus.TRADE_NOTPAY.equals(entity.getStatus()), "该笔订单不能取消");

        // 取消订单，把商品库存加上，做加法
        List<TradeItemEntity> tradeItemEntityList = tradeItemDAO.getTradeItemListByTradeId(id);
        for (TradeItemEntity tradeItemEntity : tradeItemEntityList) {
            GoodEntity goodEntity = goodDAO.getGoodEntityByGoodId(tradeItemEntity.getGoodId());
            if (goodEntity != null) {
                A.check(goodDAO.updateStock(tradeItemEntity.getGoodId(),
                        goodEntity.getGoodStock() + tradeItemEntity.getBuyNum(), goodEntity.getGoodStock()) != 1,
                        "系统繁忙，请稍后重试");
            }
        }

        if (Constants.TradeStatus.TRADE_NOTPAY.equals(entity.getStatus())) {
            // 未支付处理
            tradeDAO.updateTradeStatusById(id, Constants.TradeStatus.TRADE_CANCEL);
            tradeLogDAO.insert(new TradeLogEntity(entity.getShopId(), entity.getId(), (short) 1,
                    Constants.TradeLogCode.LOG_CANCEL, Constants.TradeLogCode.LOG_CANCEL_VALUE));
        }
    }

    // 申请退款
    @Transactional
    public void backTrade(String userId, String id, String logValue, String img1, String img2, String img3) {
        TradeEntity entity = tradeDAO.getTradeById(userId, id);
        A.check(entity == null, "无效订单");
        A.check(!Constants.TradeStatus.TRADE_NOTSEND.equals(entity.getStatus()), "该笔订单还未支付，不能申请退款");
        tradeDAO.updateTradeStatusById(entity.getId(), Constants.TradeStatus.TRADE_BACK_MONEY);
        TradeLogEntity tradeLogEntity = new TradeLogEntity(entity.getShopId(), entity.getId(), (short) 1,
                Constants.TradeLogCode.LOG_MONEY_BACK, logValue, img1, img2, img3);
        tradeLogEntity.setStatus((short) 1);
        tradeLogEntity.setLogValue2("申请退款自动同意");
        tradeLogDAO.insert(tradeLogEntity);
    }


    // 删除订单
    @Transactional
    public void delTrade(String userId, String id) {
        // 获取订单详细，判断该笔订单是否可以删除
        TradeEntity entity = tradeDAO.getTradeById(userId, id);
        A.check(entity == null || !entity.getBuyUserId().equals(userId), "无效订单");
        A.check(Constants.TradeStatus.TRADE_NOTPAY.equals(entity.getStatus())
                || Constants.TradeStatus.TRADE_NOTSEND.equals(entity.getStatus())
                || Constants.TradeStatus.TRADE_NOTRECV.equals(entity.getStatus())
                || Constants.TradeStatus.TRADE_RECVED.equals(entity.getStatus()), "该笔订单不能删除");
        tradeDAO.delTrade(entity.getId(), (short) 1);
        tradeLogDAO.insert(new TradeLogEntity(entity.getShopId(), entity.getId(), (short) 1,
                Constants.TradeLogCode.LOG_DELETE, Constants.TradeLogCode.LOG_DELETE_VALUE));
    }

    // 删除订单-商家端
    @Transactional
    public void delTradeManage(String shopId, String id) {
        // 获取订单详细，判断该笔订单是否可以删除
        TradeEntity entity = tradeDAO.getTradeById(null, id);
        A.check(entity == null || !entity.getShopId().equals(shopId) || entity.getFlag() == 2, "无效订单");
        A.check(!Constants.TradeStatus.TRADE_PAY_FAIL.equals(entity.getStatus())
                && !Constants.TradeStatus.TRADE_TIMEOUT.equals(entity.getStatus())
                && !Constants.TradeStatus.TRADE_CANCEL.equals(entity.getStatus())
                && !Constants.TradeStatus.TRADE_DONE.equals(entity.getStatus()), "该笔订单不能删除");
        tradeDAO.delShopTrade(entity.getId(), (short) 1);
    }

    // // 删除订单-管理后台
    // @Transactional
    // public void delTradeSys(String shopId, String id) {
    // // 获取订单详细，判断该笔订单是否可以删除
    // TradeEntity entity = tradeDAO.getTradeById(null, id);
    // A.check(entity == null || !entity.getShopId().equals(shopId) ||
    // entity.getFlag() == 3, "无效订单");
    // A.check(!Constants.TradeStatus.TRADE_PAY_FAIL.equals(entity.getStatus())
    // && !Constants.TradeStatus.TRADE_TIMEOUT.equals(entity.getStatus())
    // && !Constants.TradeStatus.TRADE_CANCEL.equals(entity.getStatus())
    // && !Constants.TradeStatus.TRADE_DONE.equals(entity.getStatus()), "该笔订单不能删除");
    // tradeDAO.delTrade(entity.getId(), (short) 3);
    // }

    // 催单
    public void remindTrade(String userId, String id) {
        // 获取订单详细，判断该笔订单当前状态是否未发货
        TradeEntity entity = tradeDAO.getTradeById(userId, id);
        A.check(entity == null, "无效订单");
        A.check(!Constants.TradeStatus.TRADE_NOTSEND.equals(entity.getStatus()), "该订单当前状态不能催单");

        ShopEntity shop = shopDAO.getShopEntityByShopId(entity.getShopId());
        A.check(shop == null, "该店铺不存在");
        UserEntity user = userDAO.getById(shop.getUserId());
        A.check(user == null, "无效的注册用户");

        TradeLogEntity tradeLogEntity = tradeLogDAO.getById(entity.getShopId(), entity.getId(),
                Constants.TradeLogCode.LOG_GOODS_REMIND);
        A.check(tradeLogEntity != null, "催单信息已经发给卖家，请稍等");
        tradeLogDAO.insert(new TradeLogEntity(entity.getShopId(), entity.getId(), (short) 1,
                Constants.TradeLogCode.LOG_GOODS_REMIND, Constants.TradeLogCode.LOG_GOODS_REMIND_VALUE));
        smsService.sendInfoMessage(userId, user.getMobile(), String.format("用户%s提醒您，尽快安排寄往%s的商品（订单号：%s）订单发货",
                user.getUsername(), entity.getDetailAddress(), entity.getTradeNo()));
    }

    // 确认收货
    public void recvTrade(String userId, String id, String mark) {
        // 获取订单详细，判断该笔订单是否可以确认收货
        TradeEntity entity = tradeDAO.getTradeById(userId, id);
        A.check(entity == null, "无效订单");
        userId = entity.getBuyUserId();
        List<TradeItemEntity> tradeItemEntityList = tradeItemDAO.getTradeItemList(entity.getId());
        A.check(!Constants.TradeStatus.TRADE_NOTRECV.equals(entity.getStatus()), "订单还未发货，不能确认收货");
        //更新订单商品状态
        for (TradeItemEntity tradeItemEntity : tradeItemEntityList) {
            tradeItemEntity.setGoodStatus(2);
            tradeItemEntity.setLastTime(new Date());
            tradeItemDAO.updateShopTradeItem(tradeItemEntity);
        }
        ((TradeService) AopContext.currentProxy()).coreRecvTrade(entity, userId, id, mark);
    }

    // 拒绝退款&拒绝退货之后确认收货
    public void refuseRecvTrade(String userId, String id, String mark) {
        // 获取订单详细，判断该笔订单是否可以确认收货
        TradeEntity entity = tradeDAO.getTradeById(userId, id);
        A.check(entity == null, "无效订单");
        userId = entity.getBuyUserId();
        A.check(!Constants.TradeStatus.TRADE_BACK_MONEY.equals(entity.getStatus())
                && !Constants.TradeStatus.TRADE_BACK.equals(entity.getStatus()), "订单不是售后状态");
        ((TradeService) AopContext.currentProxy()).coreRecvTrade(entity, userId, id, mark);
    }

    // 核心确认收货逻辑
    @Transactional
    public void coreRecvTrade(TradeEntity entity, String userId, String id, String mark) {
        tradeDAO.updateTradeStatusById(entity.getId(), Constants.TradeStatus.TRADE_RECVED);
        tradeLogDAO.insert(new TradeLogEntity(entity.getShopId(), entity.getId(), (short) 1,
                Constants.TradeLogCode.LOG_GOODS_RECEVED, mark));
        // UserEntity userEntity = userDAO.getById(userId);
        // A.check(userEntity == null, "用户状态异常");
        // 确认收货更新销量
       /* List<TradeItemEntity> tradeItemEntityList = tradeItemDAO.getTradeItemListByTradeId(id);
        for (TradeItemEntity tradeItemEntity : tradeItemEntityList) {
            goodDAO.getGoodById(tradeItemEntity.getGoodId());
            A.check(goodDAO.updateSales(tradeItemEntity.getGoodId(), tradeItemEntity.getBuyNum()) != 1, "更新销量失败");
        }*/
        //BigDecimal totalCny = entity.getTotalCny();
        // 减去冻结的实际支付数量的莱姆币

        //userAssetService.updateUserAsset(userId, AssetType.CREDIT.getStrType(), new BigDecimal("0"), entity.getCoined().negate(),TradeType.PAY_ORDER, id);


/*        if (entity.getCoined().compareTo(Constants.BASE_FREE) > 0) {
            ShopEntity shopEntity = shopDAO.getShopEntityByShopId(entity.getShopId());
            // 查询费率
            List<SysSettings> rateList = sysSettingsService.selectByCode("rate");
            BigDecimal merchantRate = Constants.BASE_FREE;
            BigDecimal referrerRate = Constants.BASE_FREE;
            BigDecimal platformRate = Constants.BASE_FREE;
            for (SysSettings r : rateList) {
                // 商户费率
                if ("merchant".equals(r.getLabel())) {
                    merchantRate = new BigDecimal(r.getMark());
                }
                // 推荐人费率
                if ("referrer".equals(r.getLabel())) {
                    referrerRate = new BigDecimal(r.getMark());
                }
                // 平台费率
                if ("platform".equals(r.getLabel())) {
                    platformRate = new BigDecimal(r.getMark());
                }
            }

            // 确认收货的时候，需要把钱真正转给商家92%
            ((TradeService) AopContext.currentProxy()).sendBatch(entity.getTradeNo(), userId, shopEntity.getUserId(),
                    entity.getShopId(), totalCny, entity.getCoined().multiply(merchantRate), mark);

            Date current = new Date();

        }*/
    }

    //提交并支付订单
    @Transactional
    public boolean tradeBatchAndPayTrade(String userId, SubmitTradeBatchVO submitTradeBatchVO) {
        lock.lock();
        try {
            Long current = System.currentTimeMillis();
            List<LevleConfig> levleConfigList = sysSettingsMapper.getLevleConfigList();
            List<SubmitTradeVO> sourceTradeList = submitTradeBatchVO.getTradeList();
            Set<String> shopIds = new HashSet<>();
            // Set<String> goodIds = new HashSet<>();
            List<String> goodIds = new ArrayList<>();
            Set<String> addressIds = new HashSet<>();
            //List<SubmitGoodVO> sourceGoodList = submitTradeVO.getGoodList();
            for (SubmitTradeVO submitTradeVO : sourceTradeList) {
                List<SubmitGoodVO> sourceGoodList = submitTradeVO.getGoodList();
                for (SubmitGoodVO submitGoodVO : sourceGoodList) {
                    goodIds.add(submitGoodVO.getGoodId());
                }
                shopIds.add(submitTradeVO.getShopId());
                addressIds.add(submitTradeVO.getAddressId());
            }
            A.check(CollectionUtils.isEmpty(shopIds) || CollectionUtils.isEmpty(goodIds)
                    || CollectionUtils.isEmpty(addressIds), "请求数据不完整");
            List<ShopEntity> shopList = shopDAO.listShopByIds(new ArrayList<>(shopIds), userId,
                    cn.kt.mall.common.constant.Constants.STATE_SUCCESS);
            // List<GoodEntity> goodList = goodDAO.listGoodByIds(new ArrayList<>(goodIds), userId,cn.kt.mall.common.constant.Constants.STATE_SUCCESS);
            List<AddressVO> addresslist = addressDAO.listAddressByIds(new ArrayList<>(addressIds), userId);

            A.check(shopList == null || shopList.size() != shopIds.size(), "店铺非法或不存在");
            //A.check(goodList == null || goodList.size() != goodIds.size(), "商品非法或不存在");
            A.check(addresslist == null || addresslist.size() != addressIds.size(), "地址非法或不存在");
            Map<String, ShopEntity> shopMap = buildToShopMap(shopList);
            //Map<String, GoodEntity> goodMap = buildToGoodMap(goodList);
            Map<String, AddressVO> addressMap = buildToAddressVOMap(addresslist);

            List<TradeEntity> baseTradeList = new ArrayList<>();
            List<TradeItemEntity> baseTradeItemList = new ArrayList<>();
            List<TradeLogEntity> logList = new ArrayList<>();
            List<UserCouponLogEntity> userCouponLogEntityList = new ArrayList<>();
            //立即增加的优惠券列表
            List<UserCouponEntity> userCouponEntityAddList = new ArrayList<>();
            String interiorNo = SerialCodeGenerator.getNext("09");
            // 从凯撒网获取popc币兑换比例
            BigDecimal popcRate = sysSettingsService.getCurrencyPrice("popc").setScale(Constants.SCALE,
                    RoundingMode.HALF_UP);
            //返还推荐人比例
            BigDecimal returnRatio = sysSettingsService.getReturnRatio();
            //商铺信用金低于1000提示库存不足
            BigDecimal shopPointBase = sysSettingsService.getShopPointBase();
            // 订单总费用
            BigDecimal allTotal = Constants.BASE_FREE;
            // 订单总运费
            BigDecimal allFreightFreeTotal = Constants.BASE_FREE;

            // 会员消费基础数据
            BigDecimal consume = Constants.BASE_FREE;

            for (SubmitTradeVO submitTradeVO : sourceTradeList) {
                String id = IDUtil.getUUID();
                ShopEntity shopEntity = shopMap.get(submitTradeVO.getShopId());
                BigDecimal feightRate = shopEntity.getFeightRate();
                AddressVO addressEntity = addressMap.get(submitTradeVO.getAddressId());
                //获取当前商铺信信息
                //ShopEntity shopEntityMessage = shopDAO.getShopEntityByShopId(submitTradeVO.getShopId());
                cn.kt.mall.common.wallet.entity.ShopEntity shopEntityMessage = shopScalesDAO.getShopEntityByShopId(submitTradeVO.getShopId());
                A.check(shopEntity == null, "店铺非法或不存在");
                A.check(addressEntity == null, "地址非法或不存在");
                // 商品基础费用
                BigDecimal total = Constants.BASE_FREE;
                // 商品基础费用不包括加价
                BigDecimal totalExceptRaisePrice = Constants.BASE_FREE;
                // 商品加价总额
                BigDecimal totalRaisePrice = Constants.BASE_FREE;
                // 商品基础余额费用
                BigDecimal pointTotal = Constants.BASE_FREE;
                // 商品基础popc费用
                BigDecimal popcTotal = Constants.BASE_FREE;
                // 商品基础其它费用(优惠券，popc)
                BigDecimal otherTotal = Constants.BASE_FREE;
                // 解冻基础现金
                BigDecimal unfreezePointTotal = Constants.BASE_FREE;
                // 消费获得优惠券基础数据
                List<StatementEntity> statementEntityList = new ArrayList<>();
                // 店铺销售基础数据
                BigDecimal shopConsume = Constants.BASE_FREE;
                //支付类型
                String payTypeFlag = "1";
                // 商品运费
                BigDecimal totalFreightFree = Constants.BASE_FREE;

                for (SubmitGoodVO submitGoodVO : submitTradeVO.getGoodList()) {
                    List<UserCouponLogEntity> userCouponEntityList = new ArrayList<>();
                    TradeItemEntity tradeItemEntity = new TradeItemEntity();
                    UserCouponLogEntity userCouponLogEntity = null;
                    //GoodEntity goodEntity = goodMap.get(submitGoodVO.getGoodId());
                    GoodEntity goodEntity = goodDAO.getGoodByGoodIdAndShopId(submitGoodVO.getGoodId(), submitTradeVO.getShopId());
                    //获取商品支付方式
                    GoodPayReqVO goodPayReqVO = goodDAO.getGoodPayByGoodPayId(submitGoodVO.getGoodPayId());

                    A.check(goodEntity == null, "商品非法或不存在");
                    if (submitGoodVO.getGoodId().equals(goodEntity.getId())) {
                        //订单关联商品
                        tradeItemEntity.setId(IDUtil.getUUID());
                        tradeItemEntity.setTradeId(id);
                        //tradeItemEntity.setShopId(goodEntity.getShopId());
                        tradeItemEntity.setShopId(submitTradeVO.getShopId());
                        tradeItemEntity.setGoodId(goodEntity.getId());
                        tradeItemEntity.setGoodName(goodEntity.getGoodName());
                        tradeItemEntity.setGoodImg(goodEntity.getGoodImg());
                        tradeItemEntity.setBuyPrice(goodEntity.getGoodPrice());
                        tradeItemEntity.setFreightFree(goodEntity.getFreightFree());
                        tradeItemEntity.setBuyUserId(userId);
                        tradeItemEntity.setBuyNum(submitGoodVO.getBuyNum());
                        tradeItemEntity.setPayType(goodPayReqVO.getPayType());
                        tradeItemEntity.setRaisePrice(goodEntity.getRaisePrice());
                        if (totalFreightFree.compareTo(goodEntity.getFreightFree()) < 0) {
                            totalFreightFree = goodEntity.getFreightFree();
                        }
                       /* total = total.add(goodEntity.getGoodPrice().multiply(new BigDecimal(submitGoodVO.getBuyNum())))
                                .setScale(Constants.SCALE, RoundingMode.HALF_UP);*/
                        //总额不包括加价
                        totalExceptRaisePrice = totalExceptRaisePrice.add(goodEntity.getGoodPrice().multiply(new BigDecimal(submitGoodVO.getBuyNum())))
                                .setScale(Constants.SCALE, RoundingMode.HALF_UP);
                        //总额加上加价
                        total = total.add((goodEntity.getGoodPrice().add(goodEntity.getRaisePrice())).multiply(new BigDecimal(submitGoodVO.getBuyNum())))
                                .setScale(Constants.SCALE, RoundingMode.HALF_UP);
                        totalRaisePrice = totalRaisePrice.add(goodEntity.getRaisePrice().multiply(new BigDecimal(submitGoodVO.getBuyNum())));
                        GoodCouponCenterEntity goodCouponCenterEntity = null;
                        //商品关联优惠券
                        if (!CollectionUtils.isEmpty(submitGoodVO.getCouponsList())) {
                            for (SubmitCouponVO submitCouponVO : submitGoodVO.getCouponsList()) {    //计算获取优换卷数量
                                UserCouponEntity userCouponEntity = new UserCouponEntity();
                                userCouponLogEntity = new UserCouponLogEntity();
                                goodCouponCenterEntity = goodCouponCenterDao.getGoodCouponCenterEntityByGoodIdAndCouponId(goodEntity.getId(), submitCouponVO.getCouponId());
                                if (goodCouponCenterEntity != null) {
                                    BigDecimal ratio = goodCouponCenterEntity.getRatio();
                                    //BigDecimal couponNum = goodEntity.getGoodPrice().multiply(new BigDecimal(submitGoodVO.getBuyNum())).multiply(ratio).divide(new BigDecimal(100)).setScale(Constants.SCALE, RoundingMode.HALF_UP);
                                    BigDecimal couponNum = new BigDecimal(0);
                                    if (goodEntity.getFirstGoodType().equals("well-sold")) {//如果为热销商品则只按余额进行优惠券返还
                                        BigDecimal point = (goodEntity.getGoodPrice().multiply(new BigDecimal(submitGoodVO.getBuyNum()))).multiply(goodPayReqVO.getBalanceRatio().divide(new BigDecimal(100)));
                                        couponNum = point.multiply(ratio).divide(new BigDecimal(100)).setScale(Constants.SCALE, RoundingMode.HALF_UP);
                                    } else {
                                        couponNum = goodEntity.getGoodPrice().multiply(new BigDecimal(submitGoodVO.getBuyNum())).multiply(ratio).divide(new BigDecimal(100)).setScale(Constants.SCALE, RoundingMode.HALF_UP);
                                    }
                                    //https://github.com/sharding-sphere/sharding-sphere/issues/767 暂时不支持for update走主库
                                    //HintManager 会自动关闭
                                    try (HintManager hintManager = HintManager.getInstance()) {
                                        //强制走主库
                                        hintManager.setMasterRouteOnly();
                                        //当前用户该优惠券
                                        UserCouponEntity assetNew = couponsDAO.getUserCouponEntity(userId, submitCouponVO.getCouponId());
                                        //获得优惠券记录
                                        userCouponLogEntity.setId(IDUtil.getUUID());
                                        userCouponLogEntity.setTradeid(id);
                                        userCouponLogEntity.setUserid(userId);
                                        userCouponLogEntity.setCouponname(goodCouponCenterEntity.getCouponName());
                                        if (assetNew != null) {//当前用户该优惠券存在
                                            if (goodCouponCenterEntity.getCouponName().equals("彩票积分") || goodCouponCenterEntity.getCouponName().equals("优惠券")) {//如果为彩票积分则获取冻结的数量
                                                userCouponLogEntity.setBeforenum(assetNew.getReservedCouponNum());
                                                userCouponLogEntity.setRechargenum(couponNum);
                                                userCouponLogEntity.setAfternum(couponNum.add(assetNew.getReservedCouponNum()));
                                            } else {
                                                userCouponLogEntity.setBeforenum(assetNew.getCouponNum());
                                                userCouponLogEntity.setRechargenum(couponNum);
                                                userCouponLogEntity.setAfternum(couponNum.add(assetNew.getCouponNum()));
                                            }
                                        } else {//当前用户该优惠券不存在
                                            userCouponLogEntity.setBeforenum(new BigDecimal(0));
                                            userCouponLogEntity.setRechargenum(couponNum);
                                            userCouponLogEntity.setAfternum(couponNum);
                                        }
                                        userCouponLogEntity.setCouponid(submitCouponVO.getCouponId());
                                        userCouponLogEntity.setGoodid(goodEntity.getId());
                                        userCouponLogEntity.setSendfinish(0);
                                        //userCouponLogEntity.setSpeciesType(goodCouponCenterEntity.getSpeciesType());
                                        if (goodCouponCenterEntity.getIsSend() == 1) {//可发放
                                            userCouponLogEntity.setNeedReleseDays(goodCouponCenterEntity.getSendDays());
                                            //if(goodCouponCenterEntity.getCouponName().equals("彩票积分")){//彩票积分则增加不可用数量
                                            userCouponEntity.setReservedCouponNum(couponNum);//彩票或者优惠券
                                            userCouponEntity.setCouponNum(new BigDecimal(0));
                                            //userCouponEntity.setStatus("0");
                                            // userCouponEntityAddList.add(userCouponEntity);
                                            //}
                                        } else {//不可发放该优惠券总数量立即变更为加
                                            userCouponLogEntity.setNeedReleseDays(0);
                                            userCouponEntity.setCouponNum(couponNum);
                                            userCouponEntity.setReservedCouponNum(new BigDecimal(0));
                                            //userCouponEntity.setStatus("0");
                                            //userCouponEntityAddList.add(userCouponEntity);
                                        }
                                        if (assetNew != null) {//如果存在直更新该用户优惠券数量（可用/不可用）
                                            userCouponEntity.setId(assetNew.getId());
                                            couponsDAO.updateUserCoupon(userCouponEntity);
                                        } else {//如果没有则插入
                                            userCouponEntity.setId(IDUtil.getUUID());
                                            userCouponEntity.setUserId(userId);
                                            userCouponEntity.setCouponId(submitCouponVO.getCouponId());
                                            couponsDAO.addUserCouponEntity(userCouponEntity);

                                        }
                                        couponsDAO.insertUserCouponLog(userCouponLogEntity);
                                    }
                                    //userCouponEntityList.add(userCouponLogEntity);//加入到couponLog链表中
                                    if (goodCouponCenterEntity.getCouponName().equals("优惠券")) {//如果为优惠券则加入资产记录表
                                        StatementEntity statementEntity = new StatementEntity();
                                        statementEntity.setReservedChange(couponNum);
                                        statementEntity.setReferenceId(id);
                                        statementEntity.setGoodId(goodEntity.getId());
                                        //消费赠送优惠券
                                        userAssetService.updateUserAssetGive(userId, AssetType.POPC.getStrType(), new BigDecimal(0), statementEntity.getReservedChange(), TradeType.GIVE, statementEntity.getReferenceId(), statementEntity.getGoodId());
                                        //statementEntityList.add(statementEntity);
                                    }
                                }

                            }
                        }
                        //支付方式
                        if (goodPayReqVO != null) {
                            if (goodPayReqVO.getPayType().equals("2")) {//余额+优惠券
                                payTypeFlag = "2";
                            } else if (goodPayReqVO.getPayType().equals("3")) {//余额+其它
                                payTypeFlag = "3";
                            }
                            otherTotal = otherTotal.add((goodEntity.getGoodPrice().multiply(new BigDecimal(submitGoodVO.getBuyNum()))).multiply(goodPayReqVO.getOtherRatio().divide(new BigDecimal(100))));
                            //pointTotal = pointTotal.add((goodEntity.getGoodPrice().multiply(new BigDecimal(submitGoodVO.getBuyNum()))).multiply(goodPayReqVO.getBalanceRatio().divide(new BigDecimal(100))));
                            //余额计算加上加价
                            pointTotal = pointTotal.add((((goodEntity.getGoodPrice()).multiply(new BigDecimal(submitGoodVO.getBuyNum()))).multiply(goodPayReqVO.getBalanceRatio().divide(new BigDecimal(100)))).add(goodEntity.getRaisePrice().multiply(new BigDecimal(submitGoodVO.getBuyNum()))));
                        }
                   /* //获取商品支付方式
                    List<GoodPayEntity> goodPayEntityList = goodDAO.getGoodPayEntityByGoodId(goodEntity.getId());
                    if (!CollectionUtils.isEmpty(goodPayEntityList)) {
                        for (GoodPayEntity goodPayEntity : goodPayEntityList) {
                            if (goodPayEntity.getPayType().equals("popc")) {
                                popcTotal = popcTotal.add(goodPayEntity.getPrice().multiply(new BigDecimal(submitGoodVO.getBuyNum())));
                            }
                            if (goodPayEntity.getPayType().equals("point")) {
                                pointTotal = pointTotal.add(goodPayEntity.getPrice().multiply(new BigDecimal(submitGoodVO.getBuyNum())));
                            }
                        }
                    }*/
                        //现金+popc才给解冻
                   /* if (goodPayEntityList != null) {
                        if (goodPayEntityList.size() > 1) {
                            payTypeFlag = "2";
                            unfreezePointTotal = unfreezePointTotal.add(goodEntity.getGoodPrice().multiply(new BigDecimal(submitGoodVO.getBuyNum())).multiply(goodEntity.getUnfreezeRatio()).divide(new BigDecimal(100)));
                        }
                    }*/


                    }
                    baseTradeItemList.add(tradeItemEntity);
                    //if (userCouponEntityList.size() > 0) {
                    // userCouponLogEntityList.addAll(userCouponEntityList);
                    // }

                }
                TradeEntity tradeEntity = new TradeEntity();
                BeanUtils.copyProperties(addressEntity, tradeEntity);
                tradeEntity.setInteriorNo(interiorNo);
                tradeEntity.setId(id);
                tradeEntity.setTradeNo(SerialCodeGenerator.getNext());
                tradeEntity.setShopId(submitTradeVO.getShopId());
                tradeEntity.setBuyUserId(userId);
                tradeEntity.setStatus(Constants.TradeStatus.TRADE_NOTSEND);
                tradeEntity.setTotalPrice(total);
                tradeEntity.setPoint(pointTotal);
                tradeEntity.setLemRate(popcRate);
                tradeEntity.setPayType(payTypeFlag);
                tradeEntity.setMark(submitTradeVO.getMark());
                tradeEntity.setCurrentTime(current);
                // 达到满减的额度则运费为0
                if (feightRate != null && !(feightRate.compareTo(totalFreightFree) < 0)) {
                    totalFreightFree = Constants.BASE_FREE;
                }
                tradeEntity.setTotalFreightFree(totalFreightFree);
                tradeEntity.setTotalCny(totalFreightFree.add(total));
                if (payTypeFlag.equals("2")) {
                    tradeEntity.setCoined(otherTotal);//实际需付优惠券
                    tradeEntity.setCoinWait(otherTotal);//正常需付优惠券
                } else if (payTypeFlag.equals("3")) {
                    tradeEntity.setCoined(otherTotal.divide(popcRate, Constants.SCALE, RoundingMode.HALF_UP));//实际需付popc
                    tradeEntity.setCoinWait(otherTotal);//正常需付popc
                } else {//余额
                    tradeEntity.setCoined(otherTotal);//实际需付popc
                    tradeEntity.setCoinWait(otherTotal);//正常需付popc
                }
                // tradeEntity.setCoined(popcTotal.divide(popcRate, Constants.SCALE, RoundingMode.HALF_UP));//实际需付popc
                // tradeEntity.setCoinWait(popcTotal);//正常需付popc
                baseTradeList.add(tradeEntity);
                logList.add(new TradeLogEntity(submitTradeVO.getShopId(), id, (short) 1, Constants.TradeLogCode.LOG_PAY,
                        Constants.TradeLogCode.LOG_PAY_VALUE));


 /*           if (!CollectionUtils.isEmpty(baseTradeList)) {
                int rows = tradeDAO.insertBatch(baseTradeList);
                tradeLogDAO.insertBatch(logList);
                A.check(rows != baseTradeList.size(), "提交订单失败" + "------" + "订单插入");
            }
            if (!CollectionUtils.isEmpty(baseTradeItemList)) {
                int rows = 0;
                for (TradeItemEntity tradeItemEntity : baseTradeItemList) {
                    int row = tradeItemDAO.insertTradeItem(tradeItemEntity);
                    rows = rows + row;
                }

                A.check(rows != baseTradeItemList.size(), "提交订单失败" + "------" + "订单Item插入");
            }
            if (!CollectionUtils.isEmpty(userCouponLogEntityList)) {
                int rows = 0;
                for (UserCouponLogEntity userCouponLogEntity : userCouponLogEntityList) {
                    int row = couponsDAO.insertUserCouponLog(userCouponLogEntity);
                    rows = rows + row;
                }
                A.check(rows != userCouponLogEntityList.size(), "提交订单失败" + "------" + "订单插入couPonLog");
            }
            // 清空购物车信息
            String[] cartIds = submitTradeBatchVO.getCartIds();
            if (cartIds != null && cartIds.length > 0) {
                int rows = cartDAO.delCartByBatch(userId, cartIds);
                A.check(rows != cartIds.length, "购物车删除失败");
            }*/
                // A.check(tradeMQSend.sendDelayPayBatchMessage(interiorNo) != 1, "下单失败");


                //支付订单
                A.check(tradeEntity == null, "无效的支付订单");
                A.check(!Constants.TradeStatus.TRADE_NOTSEND.equals(tradeEntity.getStatus()), "无效的订单状态");

                // 计算总共支付 = 商品基本金额+运费
                BigDecimal totalCny = tradeEntity.getTotalPrice().add(tradeEntity.getTotalFreightFree());
                A.check(totalCny.compareTo(Constants.BASE_FREE) < 0, "订单总金额为负数，状态不正常");
                //BigDecimal lemTotal = Constants.BASE_FREE;
                if (totalCny.compareTo(Constants.BASE_FREE) > 0) {
                    //检查会员金额
                    A.check(userAssetService.getUserAssetByCurrency(userId, AssetType.CREDIT.getStrType()).getAvailableBalance().compareTo(tradeEntity.getPoint().add(tradeEntity.getTotalFreightFree())) < 0, "账户余额不足" + ":" + tradeEntity.getPoint());
                    if (tradeEntity.getCoined().compareTo(Constants.BASE_FREE) > 0) {
                        A.check(userAssetService.getUserAssetByCurrency(userId, AssetType.POPC.getStrType()).getAvailableBalance().compareTo(tradeEntity.getCoined()) < 0, "账户优惠券不足" + ":" + tradeEntity.getCoined());
                    }
                    //判断店铺信用金小于配置时的最低信用金时提示该店铺不可销售商品
                    /*if (shopEntityMessage != null) {
                        //A.check(userAssetService.getUserAssetByCurrency(shopEntityMessage.getUserId(), AssetType.CREDIT.getStrType()).getAvailableBalance().compareTo(new BigDecimal(1)) < 0, shopEntityMessage.getShopName() + "的店铺商品库存不足");
                        //判断店铺销售额度
                        A.check(shopEntityMessage.getShopSalesAmount().compareTo(new BigDecimal(0)) <= 0, shopEntityMessage.getShopName() + "的店铺商品库存不足");
                    }*/
                }
                //tradeDAO.updateTradeCoinAndStatus(tradeEntity.getId(), lemTotal, Constants.TradeStatus.TRADE_NOTSEND);
                //tradeEntity.setStatus(Constants.TradeStatus.TRADE_NOTSEND);
                //tradeDAO.insertTrade(tradeEntity);
        /*tradeLogDAO.insert(new TradeLogEntity(tradeEntity.getShopId(), tradeEntity.getId(), (short) 1,
                Constants.TradeLogCode.LOG_PAY, Constants.TradeLogCode.LOG_PAY_VALUE));*/
                if (tradeEntity.getCoined().compareTo(Constants.BASE_FREE) > 0) {
                    // 会员减少相应数量的优惠券
                    userAssetService.updateUserAsset(userId, AssetType.POPC.getStrType(), tradeEntity.getCoined().negate(), new BigDecimal(0), TradeType.PAY_ORDER, tradeEntity.getId());

                }
                if (tradeEntity.getPoint().compareTo(Constants.BASE_FREE) > 0) {
                    // 会员减少相应数量的余额
                    userAssetService.updateUserAsset(userId, AssetType.CREDIT.getStrType(), (tradeEntity.getPoint().add(tradeEntity.getTotalFreightFree())).negate(), new BigDecimal(0), TradeType.PAY_ORDER, tradeEntity.getId());

                }
                if (totalCny.compareTo(Constants.BASE_FREE) > 0) {
                    //A.check(userAssetService.getUserAssetByCurrency(shopEntityMessage.getUserId(), AssetType.CREDIT.getStrType()).getAvailableBalance().compareTo(totalCny) < 0, shopEntityMessage.getShopName() + "的店铺商品库存不足");
                    //判断店铺销售额度
                    //A.check(shopEntityMessage.getShopSalesAmount().compareTo(totalCny) < 0, shopEntityMessage.getShopName() + "的店铺商品库存不足");
                    // 店铺减少相应的数量的余额
                    //userAssetService.updateUserAsset(shopEntityMessage.getUserId(), AssetType.CREDIT.getStrType(), totalCny.negate(), new BigDecimal(0), TradeType.PAY_ORDER, tradeEntity.getId());
                    //店铺减少相应的销售额度
                    shopSalesAmountService.updateShopSales(shopEntityMessage.getUserId(), totalCny.negate(), TradeType.PAY_ORDER, tradeEntity.getId());
                }
                //返还推荐人奖励(优惠券)
                UserEntity userEntity = userDAO.getById(userId);
                if (userEntity != null) {
                    if (userEntity.getReferrer() != null && !userEntity.getReferrer().equals("")) {
                        UserCouponEntity userCouponEntity = new UserCouponEntity();
                        CouponEntity couponEntity = couponsDAO.getCouponByName("彩票积分");
                        //当前用户彩票积分
                        //https://github.com/sharding-sphere/sharding-sphere/issues/767 暂时不支持for update走主库
                        //HintManager 会自动关闭
                        try (HintManager hintManager = HintManager.getInstance()) {
                            //强制走主库
                            hintManager.setMasterRouteOnly();
                            UserCouponEntity couponNew = couponsDAO.getUserCouponEntity(userEntity.getReferrer(), couponEntity.getId());
                       /* // 增加相应数量优惠券的冻结数量
                        userAssetService.updateUserAsset(userEntity.getReferrer(), AssetType.POPC.getStrType(), new BigDecimal(0), tradeEntity.getTotalCny().multiply(returnRatio), TradeType.REFERRER, tradeEntity.getId());
                        //增加返还推荐人优惠券明细
                        CouponsVO couponsVO = goodCouponCenterDao.getGoodCouponCenterEntityByCouponName();
                        UserReturnCouponLogEntity userReturnCouponLogEntity = new UserReturnCouponLogEntity();
                        userReturnCouponLogEntity.setId(IDUtil.getUUID());
                        userReturnCouponLogEntity.setCouponId(couponsVO.getId());
                        userReturnCouponLogEntity.setTradeId(tradeEntity.getId());
                        userReturnCouponLogEntity.setAmount(tradeEntity.getTotalCny().multiply(returnRatio));
                        userReturnCouponLogEntity.setUserId(userEntity.getReferrer());
                        userReturnCouponLogEntity.setRelationUserId(userId);
                        userReturnCouponLogEntity.setNeedReleaseDays(couponsVO.getSendDays());
                        userReturnCouponLogEntity.setReturnRatio(returnRatio);
                        userReturnCouponLogDAO.insertUserReturnCouponLogById(userReturnCouponLogEntity);*/
                            //返还推荐人彩票积分立即到账
                            UserCouponLogEntity userCouponLogEntity = new UserCouponLogEntity();
                            userCouponLogEntity.setId(IDUtil.getUUID());
                            userCouponLogEntity.setNeedReleseDays(0);
                            userCouponLogEntity.setCouponid(couponEntity.getId());
                            userCouponLogEntity.setCouponname("彩票积分");
                            userCouponLogEntity.setUserid(userEntity.getReferrer());
                            userCouponLogEntity.setGoodid("0");
                            userCouponLogEntity.setTradeid("0");
                            if (couponNew == null) {
                                userCouponLogEntity.setBeforenum(BigDecimal.ZERO);
                                //userCouponLogEntity.setAfternum(tradeEntity.getTotalCny().multiply(returnRatio));
                                userCouponLogEntity.setAfternum(totalExceptRaisePrice.multiply(returnRatio));
                            } else {
                                userCouponLogEntity.setBeforenum(couponNew.getCouponNum());
                                //userCouponLogEntity.setAfternum(assetNew.getCouponNum().add(tradeEntity.getTotalCny().multiply(returnRatio)));
                                userCouponLogEntity.setAfternum(couponNew.getCouponNum().add(totalExceptRaisePrice.multiply(returnRatio)));
                            }
                            //userCouponLogEntity.setRechargenum(tradeEntity.getTotalCny().multiply(returnRatio));
                            userCouponLogEntity.setRechargenum(totalExceptRaisePrice.multiply(returnRatio));
                            userCouponLogEntity.setSendfinish(0);
                            //userCouponLogEntity.setTotalCny(tradeEntity.getTotalCny());
                            userCouponLogEntity.setTotalCny(totalExceptRaisePrice);
                            userCouponLogEntity.setTotalRaisePrice(totalRaisePrice);
                            userCouponLogEntity.setRatio(returnRatio);
                            userCouponLogEntity.setRelationUserId(userId);
                            couponsDAO.insertUserCouponLog(userCouponLogEntity);
                            if (couponNew != null) {//如果存在直更新该用户优惠券数量（可用/不可用）
                                //userCouponEntity.setCouponNum(tradeEntity.getTotalCny().multiply(returnRatio));
                                userCouponEntity.setCouponNum(totalExceptRaisePrice.multiply(returnRatio));
                                userCouponEntity.setReservedCouponNum(new BigDecimal(0));
                                userCouponEntity.setId(couponNew.getId());
                                couponsDAO.updateUserCoupon(userCouponEntity);
                            } else {//如果没有则插入
                                userCouponEntity.setId(IDUtil.getUUID());
                                userCouponEntity.setUserId(userEntity.getReferrer());
                                //userCouponEntity.setCouponNum(tradeEntity.getTotalCny().multiply(returnRatio));
                                userCouponEntity.setCouponNum(totalExceptRaisePrice.multiply(returnRatio));
                                userCouponEntity.setReservedCouponNum(new BigDecimal(0));
                                userCouponEntity.setCouponId(couponEntity.getId());
                                couponsDAO.addUserCouponEntity(userCouponEntity);

                            }
                        }
                    }
                }
                //消费赠送优惠券
               /* if (!CollectionUtils.isEmpty(statementEntityList)) {
                    for (StatementEntity statementEntity : statementEntityList) {
                        userAssetService.updateUserAsset(userId, AssetType.POPC.getStrType(), new BigDecimal(0), statementEntity.getReservedChange(), TradeType.GIVE, statementEntity.getReferenceId());
                    }
                }*/
           /* //根据购买商品的解冻比率解冻popc //
            if (unfreezePointTotal.compareTo(Constants.BASE_FREE) > 0) {
                // 解冻相应的popc
                if (userAssetService.getUserAssetByCurrency(userId, AssetType.POPC.getStrType()).getReservedBalance().compareTo(unfreezePointTotal) > 0) {
                    userAssetService.updateUserAsset(userId, AssetType.POPC.getStrType(), unfreezePointTotal, unfreezePointTotal.negate(), TradeType.RELEASE, userId);
                }
                //userAssetService.updateUserAsset(userId, AssetType.CREDIT.getStrType(), unfreezePointTotal, unfreezePointTotal.negate(), TradeType.RELEASE, userId);
            }*/
                //店铺销售累计
                shopConsume = tradeEntity.getTotalCny();
                BigDecimal currentShopConsume = new BigDecimal(0);
                // 计算店铺等级
                for (LevleConfig levleConfig : levleConfigList) {
                    if (levleConfig.getConfigKey().equals("shop_level")) {
                        ShopEntity shop = shopDAO.getShopEntityByShopId(submitTradeVO.getShopId());
                        currentShopConsume = shop.getShopConsume().add(shopConsume);
                        int shopRank = currentShopConsume.divide(levleConfig.getAmount(), 0, BigDecimal.ROUND_DOWN).setScale(0, RoundingMode.DOWN).multiply(new BigDecimal(levleConfig.getConfigLevel())).intValue();
                        shopConsume = currentShopConsume.divideAndRemainder(levleConfig.getAmount())[1].setScale(4, RoundingMode.HALF_UP);//取余数存到余额中
                        int shopRankCurrent = shop.getShopRank();
                    /*    if (shopRank > shop.getShopRank()) {

                            shop.setShopRank(shopRank);
                            shop.setShopConsume(shopConsume);
                            shop.setShopRankBefore(shopRankCurrent);
                            int rows = shopDAO.updateShopRankAndConsume(shop);
                            if (rows < 1) {
                                logger.info("店铺升级失败");
                                A.check("提交订单失败请重新提交");
                            }
                        } else {
                            shop.setShopConsume(shopConsume);
                            shopDAO.updateShopConsume(shop);

                        }*/
                        shop.setShopRank(shopRank);
                        shop.setShopConsume(shopConsume);
                        shop.setShopRankBefore(shopRankCurrent);
                        int rows = shopDAO.updateShopRankAndConsume(shop);
                        if (rows < 1) {
                            logger.info("店铺升级失败");
                            A.check("提交订单失败请重新提交");
                        }

                        break;
                    }
                }
                //会员消费累计
                consume = consume.add(tradeEntity.getTotalCny());
            }
            //计算会员等级
            UserConsumeVO userConsumeVO = userDAO.getUserConsume(userId);
            for (LevleConfig levleConfig : levleConfigList) {

                if (levleConfig.getConfigKey().equals("user_level")) {
                    int userLevel = 1;
                    if (userConsumeVO == null) {
                        userConsumeVO = new UserConsumeVO();
                        userLevel = userLevel + consume.divide(levleConfig.getAmount(), 0, BigDecimal.ROUND_DOWN).setScale(0, RoundingMode.DOWN).multiply(new BigDecimal(levleConfig.getConfigLevel())).intValue();
                        consume = consume.divideAndRemainder(levleConfig.getAmount())[1].setScale(4, RoundingMode.HALF_UP);//取余数存到余额中
                        userConsumeVO.setId(IDUtil.getUUID());
                        userConsumeVO.setUserId(userId);
                        userConsumeVO.setUserLevel(userLevel);
                        userConsumeVO.setConsume(consume);
                        userDAO.insertUserConsume(userConsumeVO);
                    } else {
                        int userCurretLevel = userConsumeVO.getUserLevel();
                        BigDecimal currentConsume = consume.add(userConsumeVO.getConsume());

                        userLevel = currentConsume.divide(levleConfig.getAmount(), 0, BigDecimal.ROUND_DOWN).setScale(0, RoundingMode.DOWN).multiply(new BigDecimal(levleConfig.getConfigLevel())).intValue();
                        consume = currentConsume.divideAndRemainder(levleConfig.getAmount())[1].setScale(4, RoundingMode.HALF_UP);//取余数存到余额中
                        userConsumeVO.setConsume(consume);
                        userConsumeVO.setUserLevel(userLevel);
                        userConsumeVO.setUserLevelBefore(userCurretLevel);
                        int rows = userDAO.updateUserConsume(userConsumeVO);
                        if (rows < 1) {
                            logger.info("会员升级失败");
                            A.check("提交订单失败请重新提交");
                        }
                    }
                    break;
                }

            }
            //((TradeService) AopContext.currentProxy()).checkAndUpdateGoods(goodList, baseTradeItemList); //不需检查库存
            if (!CollectionUtils.isEmpty(baseTradeList)) {
            /*int rows = tradeDAO.insertBatch(baseTradeList);
            tradeLogDAO.insertBatch(logList);*/
                int rows = 0;
                for (TradeEntity tradeEntity : baseTradeList) {
                    int row = tradeDAO.insertTradeEntity(tradeEntity);
                    rows = rows + row;
                }
                //订单插入

                A.check(rows != baseTradeList.size(), "提交订单失败");
            }
            if (!CollectionUtils.isEmpty(baseTradeItemList)) {
                int rows = 0;
                for (TradeItemEntity tradeItemEntity : baseTradeItemList) {
                    int row = tradeItemDAO.insertTradeItem(tradeItemEntity);
                    //String goodName =  sysSettingsService.getGoodName();
                  /*  if(tradeItemEntity.getGoodName().equals("正宇汽车")){
                        goodService.checkShopSalesAmountByShopIdAndGoodId(tradeItemEntity.getShopId(),tradeItemEntity.getGoodId(),tradeItemEntity.getBuyNum());
                    }*/
                    A.check(1 != goodDAO.updateSalesCenter(tradeItemEntity.getShopId(), tradeItemEntity.getGoodId(), tradeItemEntity.getBuyNum()), "系统繁忙，请稍后重新提交");
                    rows = rows + row;
                }
                //订单Item插入
                A.check(rows != baseTradeItemList.size(), "提交订单失败");
            }
            if (!CollectionUtils.isEmpty(logList)) {
                int rows = 0;
                for (TradeLogEntity tradeLogEntity : logList) {
                    int row = tradeLogDAO.insertTradeLogEntity(tradeLogEntity);
                    rows = rows + row;
                }
                //订单Log插入
                A.check(rows != logList.size(), "提交订单失败");
            }
/*            if (!CollectionUtils.isEmpty(userCouponLogEntityList)) {
                int rows = 0;
                for (UserCouponLogEntity userCouponLogEntity : userCouponLogEntityList) {
                    int row = couponsDAO.insertUserCouponLog(userCouponLogEntity);
                    rows = rows + row;
                }
                //订单插入couPonLog
                A.check(rows != userCouponLogEntityList.size(), "提交订单失败");
            }
            if (!CollectionUtils.isEmpty(userCouponEntityAddList)) {
                int rows = 0;
                for (UserCouponEntity userCouponEntity : userCouponEntityAddList) {
                    UserCouponEntity entity = couponsDAO.getUserCouponEntity(userCouponEntity.getUserId(), userCouponEntity.getCouponId());
                    if (entity != null) {//如果存在直更新
                        userCouponEntity.setId(entity.getId());
                        int row = couponsDAO.updateUserCoupon(userCouponEntity);
                        rows = rows + row;
                    } else {//如果没有则插入
                        int row = couponsDAO.addUserCouponEntity(userCouponEntity);
                        rows = rows + row;
                    }

                }
                //订单插入userCouponEntity
                A.check(rows != userCouponLogEntityList.size(), "提交订单失败");
            }*/
            // 清空购物车信息
            String[] cartIds = submitTradeBatchVO.getCartIds();
            if (cartIds != null && cartIds.length > 0) {
                int rows = cartDAO.delCartByBatch(userId, cartIds);
                A.check(rows != cartIds.length, "购物车删除失败");
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    // 批量提交订单
    @Transactional
    public TradeRespVO tradeBatch(String userId, SubmitTradeBatchVO submitTradeBatchVO) {
        List<SubmitTradeVO> sourceTradeList = submitTradeBatchVO.getTradeList();
        Set<String> shopIds = new HashSet<>();
        Set<String> goodIds = new HashSet<>();
        Set<String> addressIds = new HashSet<>();
        Set<String> couponNames = new HashSet<>();
        for (SubmitTradeVO submitTradeVO : sourceTradeList) {
            addressIds.add(submitTradeVO.getAddressId());
            shopIds.add(submitTradeVO.getShopId());
            List<SubmitGoodVO> sourceGoodList = submitTradeVO.getGoodList();
            for (SubmitGoodVO submitGoodVO : sourceGoodList) {
                goodIds.add(submitGoodVO.getGoodId());
            }
        }
        A.check(CollectionUtils.isEmpty(shopIds) || CollectionUtils.isEmpty(goodIds)
                || CollectionUtils.isEmpty(addressIds), "请求数据不完整");
        List<ShopEntity> shopList = shopDAO.listShopByIds(new ArrayList<>(shopIds), userId,
                cn.kt.mall.common.constant.Constants.STATE_SUCCESS);
        List<GoodEntity> goodList = goodDAO.listGoodByIds(new ArrayList<>(goodIds), userId,
                cn.kt.mall.common.constant.Constants.STATE_SUCCESS);
        List<AddressVO> addresslist = addressDAO.listAddressByIds(new ArrayList<>(addressIds), userId);

        A.check(shopList == null || shopList.size() != shopIds.size(), "店铺非法或不存在");
        A.check(goodList == null || goodList.size() != goodIds.size(), "商品非法或不存在");
        A.check(addresslist == null || addresslist.size() != addressIds.size(), "地址非法或不存在");

        Map<String, ShopEntity> shopMap = buildToShopMap(shopList);
        Map<String, GoodEntity> goodMap = buildToGoodMap(goodList);
        Map<String, AddressVO> addressMap = buildToAddressVOMap(addresslist);

        List<TradeEntity> baseTradeList = new ArrayList<>();
        List<TradeItemEntity> baseTradeItemList = new ArrayList<>();
        List<TradeLogEntity> logList = new ArrayList<>();
        String interiorNo = SerialCodeGenerator.getNext("09");
        // 获取莱姆币兑换比例
        BigDecimal lemRate = new BigDecimal(lemRateService.getLemRate()).setScale(Constants.SCALE,
                RoundingMode.HALF_UP);
        // 订单总费用
        BigDecimal allTotal = Constants.BASE_FREE;
        // 订单总运费
        BigDecimal allFreightFreeTotal = Constants.BASE_FREE;
        for (SubmitTradeVO submitTradeVO : sourceTradeList) {
            String id = IDUtil.getUUID();
            ShopEntity shopEntity = shopMap.get(submitTradeVO.getShopId());
            BigDecimal feightRate = shopEntity.getFeightRate();
            AddressVO addressEntity = addressMap.get(submitTradeVO.getAddressId());
            A.check(shopEntity == null, "店铺非法或不存在");
            A.check(addressEntity == null, "地址非法或不存在");
            // 商品基础费用
            BigDecimal total = Constants.BASE_FREE;
            // 商品运费
            BigDecimal totalFreightFree = Constants.BASE_FREE;
            for (SubmitGoodVO submitGoodVO : submitTradeVO.getGoodList()) {
                TradeItemEntity tradeItemEntity = new TradeItemEntity();
                GoodEntity goodEntity = goodMap.get(submitGoodVO.getGoodId());
                A.check(goodEntity == null, "商品非法或不存在");
                if (submitGoodVO.getGoodId().equals(goodEntity.getId())) {
                    tradeItemEntity.setId(IDUtil.getUUID());
                    tradeItemEntity.setTradeId(id);
                    tradeItemEntity.setShopId(goodEntity.getShopId());
                    tradeItemEntity.setGoodId(goodEntity.getId());
                    tradeItemEntity.setGoodName(goodEntity.getGoodName());
                    tradeItemEntity.setGoodImg(goodEntity.getGoodImg());
                    tradeItemEntity.setBuyPrice(goodEntity.getGoodPrice());
                    tradeItemEntity.setFreightFree(goodEntity.getFreightFree());
                    tradeItemEntity.setBuyUserId(userId);
                    tradeItemEntity.setBuyNum(submitGoodVO.getBuyNum());
                    if (totalFreightFree.compareTo(goodEntity.getFreightFree()) < 0) {
                        totalFreightFree = goodEntity.getFreightFree();
                    }
                    total = total.add(goodEntity.getGoodPrice().multiply(new BigDecimal(submitGoodVO.getBuyNum())))
                            .setScale(Constants.SCALE, RoundingMode.HALF_UP);

                }
                baseTradeItemList.add(tradeItemEntity);
            }

            TradeEntity tradeEntity = new TradeEntity();
            BeanUtils.copyProperties(addressEntity, tradeEntity);
            tradeEntity.setInteriorNo(interiorNo);
            tradeEntity.setId(id);
            tradeEntity.setTradeNo(SerialCodeGenerator.getNext());
            tradeEntity.setShopId(submitTradeVO.getShopId());
            tradeEntity.setBuyUserId(userId);
            tradeEntity.setStatus(Constants.TradeStatus.TRADE_NOTPAY);
            tradeEntity.setTotalPrice(total);
            tradeEntity.setLemRate(lemRate);
            // 达到满减的额度则运费为0
            if (feightRate != null && !(feightRate.compareTo(totalFreightFree) < 0)) {
                totalFreightFree = Constants.BASE_FREE;
            }
            tradeEntity.setTotalFreightFree(totalFreightFree);
            tradeEntity.setTotalCny(totalFreightFree.add(total));
            BigDecimal coinWait = tradeEntity.getTotalCny().divide(lemRate, Constants.SCALE, RoundingMode.HALF_UP);
            if (coinWait.compareTo(Constants.BASE_FREE) > 0) {
                tradeEntity.setCoinWait(coinWait);
            } else {
                tradeEntity.setCoinWait(Constants.BASE_FREE);
            }
            tradeEntity.setMark(submitTradeVO.getMark());
            allFreightFreeTotal = allFreightFreeTotal.add(totalFreightFree);
            allTotal = tradeEntity.getTotalCny().add(allTotal);
            baseTradeList.add(tradeEntity);
            logList.add(new TradeLogEntity(submitTradeVO.getShopId(), id, (short) 1, Constants.TradeLogCode.LOG_CREATE,
                    Constants.TradeLogCode.LOG_CREATE_VALUE));
        }

        ((TradeService) AopContext.currentProxy()).checkAndUpdateGoods(goodList, baseTradeItemList);

        if (!CollectionUtils.isEmpty(baseTradeList)) {
            int rows = tradeDAO.insertBatch(baseTradeList);
            tradeLogDAO.insertBatch(logList);
            A.check(rows != baseTradeList.size(), "提交订单失败");
        }
        if (!CollectionUtils.isEmpty(baseTradeItemList)) {
            int rows = tradeItemDAO.insertBatch(baseTradeItemList);
            A.check(rows != baseTradeItemList.size(), "提交订单失败");
        }
        // 清空购物车信息
        String[] cartIds = submitTradeBatchVO.getCartIds();
        if (cartIds != null && cartIds.length > 0) {
            int rows = cartDAO.delCartByBatch(userId, cartIds);
            A.check(rows != cartIds.length, "购物车删除失败");
        }

        TradeRespVO tradeRespVO = new TradeRespVO();
        tradeRespVO.setLemRate(lemRate);
        tradeRespVO.setInteriorNo(interiorNo);
        tradeRespVO.setLemTotal(allTotal.divide(lemRate, Constants.SCALE, RoundingMode.HALF_UP)
                .setScale(Constants.SCALE, RoundingMode.HALF_UP));
        tradeRespVO.setBaseTotal(allTotal.setScale(Constants.SCALE, RoundingMode.HALF_UP));
        return tradeRespVO;
    }

    @Transactional
    public void checkAndUpdateGoods(List<GoodEntity> goodEntityList, List<TradeItemEntity> tradeItemEntityList) {
        Map<String, Integer> tradeMap = checkGoodsStock(tradeItemEntityList, goodEntityList);
        // 更新库存 下单，做减法
        for (Map.Entry<String, Integer> entry : tradeMap.entrySet()) {
            for (GoodEntity goodEntity : goodEntityList) {
                if (goodEntity.getId().equals(entry.getKey())) {
                    A.check(1 != goodDAO.updateStock(entry.getKey(), goodEntity.getGoodStock() - entry.getValue(),
                            goodEntity.getGoodStock()), "系统繁忙，请稍后重新提交");
                    A.check(1 != goodDAO.updateSales(entry.getKey(), entry.getValue()), "系统繁忙，请稍后重新提交");
                }
            }
        }
    }

    private Map<String, Integer> checkGoodsStock(List<TradeItemEntity> tradeItemEntityList,
                                                 List<GoodEntity> goodEntityList) {
        Map<String, Integer> tradeMap = new HashMap<>();
        for (TradeItemEntity tradeItemEntity : tradeItemEntityList) {
            Integer num = tradeMap.get(tradeItemEntity.getGoodId());
            if (num == null) {
                num = 0;
            }
            tradeMap.put(tradeItemEntity.getGoodId(), num + tradeItemEntity.getBuyNum());
        }
        for (GoodEntity goodEntity : goodEntityList) {
            Integer buyNum = tradeMap.get(goodEntity.getId());
            A.check(buyNum == null, "购买商品异常");
            A.check(buyNum > goodEntity.getGoodStock(), "所购买的商品<" + goodEntity.getGoodName() + ">库存不足");
        }
        return tradeMap;
    }

    private Map<String, ShopEntity> buildToShopMap(List<ShopEntity> list) {
        Map<String, ShopEntity> shopMap = new HashMap<>();
        for (ShopEntity shopEntity : list) {
            shopMap.put(shopEntity.getId(), shopEntity);
        }
        return shopMap;
    }

    private Map<String, GoodEntity> buildToGoodMap(List<GoodEntity> list) {
        Map<String, GoodEntity> goodMap = new HashMap<>();
        for (GoodEntity goodEntity : list) {
            goodMap.put(goodEntity.getId(), goodEntity);
        }
        return goodMap;
    }

    private Map<String, AddressVO> buildToAddressVOMap(List<AddressVO> list) {
        Map<String, AddressVO> addressMap = new HashMap<>();
        for (AddressVO addressVO : list) {
            addressMap.put(addressVO.getId(), addressVO);
        }
        return addressMap;
    }

    // 消息队列更新订单超时
    public void timeOutTrade(String interiorNo) {
        List<TradeEntity> list = tradeDAO.getTradeByInteriorNo(null, interiorNo);
        if (!CollectionUtils.isEmpty(list)) {
            for (TradeEntity entity : list) {
                if (Constants.TradeStatus.TRADE_NOTPAY.equals(entity.getStatus())) {
                    tradeDAO.setTradeStatus(entity.getId(), Constants.TradeStatus.TRADE_NOTPAY,
                            Constants.TradeStatus.TRADE_TIMEOUT);
                    tradeLogDAO.insert(new TradeLogEntity(entity.getShopId(), entity.getId(), (short) 1,
                            Constants.TradeLogCode.LOG_TIMEOUT, Constants.TradeLogCode.LOG_TIMEOUT_VALUE));
                    // 释放库存
                    List<TradeItemEntity> itemList = tradeItemDAO.getTradeItemListByTradeId(entity.getId());
                    if (!CollectionUtils.isEmpty(itemList)) {
                        for (TradeItemEntity tradeItemEntity : itemList) {
                            goodDAO.addStock(tradeItemEntity.getGoodId(), tradeItemEntity.getBuyNum());
                        }
                    }
                }
            }
        }
    }

    // 确认收货的时候，分四批进行分发
    @Transactional
    public void sendBatch(String tradeNo, String fromUserId, String toUserId, String toShopId, BigDecimal cny,
                          BigDecimal lemTotal, String mark) {
        Date current = new Date();
        BigDecimal batchTotal = lemTotal.divide(new BigDecimal("4"), Constants.SCALE, RoundingMode.FLOOR);

    }

    // 商家端分页获取订单信息
    public CommonPageVO<TradeManageRespVO> listTrade(TradeManageReqVO tradeManageReqVO, int pageNo, int pageSize) {
        List<TradeManageRespVO> resultList = new ArrayList<>();
        List<String> userIdList = new ArrayList<>();
        List<UserEntity> userEntityList = new ArrayList<>();
        //查询订单
        //List<TradeEntity> list = tradeDAO.listTrade(tradeManageReqVO, new RowBounds(pageNo, pageSize));
        Map<String, Object> UserMap = new HashMap<String, Object>();
        //判断当前shopID的用户是否有下级关系
        ShopEntity shopEntity = shopDAO.getShopEntityByShopId(tradeManageReqVO.getShopId());
        userIdList.add(shopEntity.getUserId());
        if (shopEntity != null) {
            //查询user表
            userEntityList = userDAO.getUserByPid(shopEntity.getUserId());
            if (userEntityList != null && userEntityList.size() > 0) {
                for (UserEntity pidUser : userEntityList) {
                    userIdList.add(pidUser.getId());
                }
            }

        }
        // 如果没有店铺，直接返回
        if (userIdList == null || userIdList.size() == 0) {
            PageInfo<TradeEntity> pageInfo = new PageInfo<>();
            return CommonUtil.copyFromPageInfo(pageInfo, resultList);
        }
        List<String> shopIdList = shopDAO.getShopListByUserId(userIdList);
        shopIdList.add(shopEntity.getId());
        tradeManageReqVO.setShopIdList(shopIdList);
        tradeManageReqVO.setShopId(null);
        List<TradeEntity> list = tradeDAO.listTradeByShopId(tradeManageReqVO, new RowBounds(pageNo, pageSize));
        PageInfo<TradeEntity> pageInfo = new PageInfo<>(list);
        if (!CollectionUtils.isEmpty(list)) {
            List<String> tradeIds = new ArrayList<>();
            List<String> shopIds = new ArrayList<>();
            for (TradeEntity tradeEntity : list) {
                tradeIds.add(tradeEntity.getId());
                shopIds.add(tradeEntity.getShopId());
            }
            List<TradeItemEntity> tradeItemList = tradeItemDAO.getTradeItemListByIds(tradeIds);
            Map<String, List<TradeItemEntity>> map = listForMap(tradeItemList);
            List<ShopEntity> shopList = shopDAO.listShopByIds(shopIds, null, null);
            Map<String, ShopEntity> shopMap = listFromShopMap(shopList);
            for (TradeEntity tradeEntity : list) {
                //如果map里存在说明他们是下级关系
                TradeManageRespVO vo = new TradeManageRespVO();
                BeanUtils.copyProperties(tradeEntity, vo);
                vo.setTradeItems(map.get(vo.getId()));
                ShopEntity tempShopEntity = shopMap.get(tradeEntity.getShopId());
                if (tempShopEntity != null) {
                    vo.setShopName(tempShopEntity.getShopName());
                }
                //  UserEntity userEntity = (UserEntity) UserMap.get(tradeEntity.getBuyUserId());
//                if (userEntity != null) {
//                    String userLevel = userDAO.queryUserLevelByUserID(userEntity.getId());
//                    vo.setLevel(StringUtils.isEmpty(userLevel) ? "0" : userLevel + "");
//                    vo.setRelationship("下级");
//                } else {
//                    UserEntity user = userDAO.getById(tradeEntity.getBuyUserId());
//                    if (user != null && user.getLevel() != null) {
//                        String userLevel = userDAO.queryUserLevelByUserID(user.getId());
//                        vo.setLevel(StringUtils.isEmpty(userLevel) ? "0" : userLevel);
//                    }
//                    vo.setRelationship("无");
//                }
                if (userEntityList != null && userEntityList.size() > 0) {
                    UserEntity u = userDAO.getById(tradeEntity.getBuyUserId());
                    if (!u.getPid().equals(shopEntity.getUserId())) {
                        String userLevel = userDAO.queryUserLevelByUserID(tradeEntity.getBuyUserId());
                        vo.setLevel(userLevel);
                        vo.setRelationship("无");
                    } else {
                        String userLevel = userDAO.queryUserLevelByUserID(tradeEntity.getBuyUserId());
                        vo.setLevel(userLevel);
                        vo.setRelationship("下级");
                    }

                } else {
                    String userLevel = userDAO.queryUserLevelByUserID(tradeEntity.getBuyUserId());
                    vo.setRelationship("无");
                    vo.setLevel(userLevel);
                }

                resultList.add(vo);
            }
        }
        return CommonUtil.copyFromPageInfo(pageInfo, resultList);
    }


    /**
     * 获取商家订单，不分页
     *
     * @param
     * @return
     */

    public List<TradeManageRespVO> getTradeManageRespVOList(TradeManageReqVO tradeManageReqVO) {
        List<TradeManageRespVO> resultList = new ArrayList<>();
        List<UserEntity> userEntityList = new ArrayList<>();
        List<String> userIdList = new ArrayList<>();
        //判断当前shopID的用户是否有下级关系
        ShopEntity shopEntity = shopDAO.getShopEntityByShopId(tradeManageReqVO.getShopId());
        userIdList.add(shopEntity.getUserId());
        if (shopEntity != null) {
            //查询user表
            userEntityList = userDAO.getUserByPid(shopEntity.getUserId());
            if (userEntityList != null) {
                for (UserEntity user : userEntityList) {
                    userIdList.add(user.getId());
                }
            }

        }

        List<String> shopIdList = shopDAO.getShopListByUserId(userIdList);
        tradeManageReqVO.setShopIdList(shopIdList);
        tradeManageReqVO.setShopId(null);
        List<TradeEntity> list = tradeDAO.listTradeByShopId(tradeManageReqVO);
        if (!CollectionUtils.isEmpty(list)) {
//            List<String> tradeIds = new ArrayList<>();
////            List<String> shopIds = new ArrayList<>();
//            for (TradeEntity tradeEntity : list) {
//                tradeIds.add(tradeEntity.getId());
//
//            }
//            List<TradeItemEntity> tradeItemList = tradeItemDAO.getTradeItemListByIds(tradeIds);
//            Map<String, List<TradeItemEntity>> map = listForMap(tradeItemList);
            List<ShopEntity> shopList = shopDAO.listShopByIds(shopIdList, null, null);
            Map<String, ShopEntity> shopMap = listFromShopMap(shopList);
            for (TradeEntity tradeEntity : list) {
                //如果map里存在说明他们是下级关系
                TradeManageRespVO vo = new TradeManageRespVO();
                BeanUtils.copyProperties(tradeEntity, vo);
//                vo.setTradeItems(map.get(vo.getId()));
                ShopEntity tempShopEntity = shopMap.get(tradeEntity.getShopId());
                if (tempShopEntity != null) {
                    vo.setShopName(tempShopEntity.getShopName());
                }
//                UserEntity userEntity = (UserEntity) UserMap.get(tradeEntity.getBuyUserId());
//                if (userEntity != null) {
//                    vo.setLevel(userEntity.getLevel());
//                    vo.setRelationship("下级");
//                } else {
//                    vo.setRelationship("无");
//                }
                if (userEntityList != null && userEntityList.size() > 0) {
                    UserEntity u = userDAO.getById(tradeEntity.getBuyUserId());
                    if (!u.getPid().equals(shopEntity.getUserId())) {
                        String userLevel = userDAO.queryUserLevelByUserID(tradeEntity.getBuyUserId());
                        vo.setLevel(userLevel);
                        vo.setRelationship("无");
                    } else {
                        String userLevel = userDAO.queryUserLevelByUserID(tradeEntity.getBuyUserId());
                        vo.setLevel(userLevel);
                        vo.setRelationship("下级");
                    }

                } else {
                    String userLevel = userDAO.queryUserLevelByUserID(tradeEntity.getBuyUserId());
                    vo.setRelationship("无");
                    vo.setLevel(userLevel);
                }


                resultList.add(vo);
            }
        }
        return resultList;
    }


    private Map<String, ShopEntity> listFromShopMap(List<ShopEntity> list) {
        Map<String, ShopEntity> map = new HashMap<>();
        for (ShopEntity shopEntity : list) {
            map.put(shopEntity.getId(), shopEntity);
        }
        return map;
    }

    // 订单item list转map
    public Map<String, List<TradeItemEntity>> listForMap(List<TradeItemEntity> list) {
        Map<String, List<TradeItemEntity>> map = new HashMap<>();
        for (TradeItemEntity tradeItemEntity : list) {
            String key = tradeItemEntity.getTradeId();
            List<TradeItemEntity> tradeItem = map.get(key);
            if (tradeItem == null) {
                tradeItem = new ArrayList<>();
            }
            tradeItem.add(tradeItemEntity);
            map.put(key, tradeItem);
        }
        return map;
    }

    // 订单发货处理
    @Transactional
    public void deliveGoods(String shopId, String tradeId, String label, String logisticsNo, String userId) {
        TradeEntity entity = tradeDAO.getTradeById(null, tradeId);
        A.check(entity == null || !shopId.equals(entity.getShopId()), "订单不存在");
        A.check(!entity.getStatus().equals(Constants.TradeStatus.TRADE_NOTSEND), "该订单状态不能做发货操作");
        int rows = tradeDAO.setTradeStatus(entity.getId(), Constants.TradeStatus.TRADE_NOTSEND,
                Constants.TradeStatus.TRADE_NOTRECV);
        A.check(rows != 1, "更新状态失败");
        tradeLogDAO.insert(new TradeLogEntity(entity.getShopId(), entity.getId(), (short) 1,
                Constants.TradeLogCode.LOG_GOODS_START, Constants.TradeLogCode.LOG_GOODS_START_VALUE, null, null, null,
                label, logisticsNo));
        //  tradeMQSend.sendDelayDoneGoodsMessage(tradeId);
        //批量更新订单明细的里的商品变为发货状态
        List<TradeItemEntity> tradeItem = tradeItemDAO.getTradeItemListByTradeId(entity.getId());
        for (TradeItemEntity itemEntity : tradeItem) {
            int count = tradeItemDAO.updateTradeItemByShopIdAndGoodId(entity.getId(), itemEntity.getGoodId(), 1);
            A.check(count <= 0, "订单发货失败");
            ShopTransportVO shopTransportVO = new ShopTransportVO();
            shopTransportVO.setId(UUID.randomUUID().toString());
            shopTransportVO.setShopId(entity.getShopId());
            shopTransportVO.setGoodId(itemEntity.getGoodId());
            shopTransportVO.setTradeNo(entity.getTradeNo());
            shopTransportVO.setTotalFreightFree(BigDecimal.ZERO);
            shopTransportVO.setUserId(userId);
            shopTransportVO.setTransportNo(logisticsNo);
            shopTransportVO.setLogisticsName(label);
            int tranCount = shopGoodTransportDAO.addShopTranSport(shopTransportVO);
            A.check(tranCount <= 0, "订单发货失败");
        }
        logisticsService.addLogistice(logisticsNo, label);
    }

    // 按需更新
    public void updateSelective(TradeEntity tradeEntity) {
        int rows = tradeDAO.updateSelective(tradeEntity);
        A.check(rows != 1, "更新失败");
    }

//    /**
//     * 批量发货处理
//     *
//     * @param shopId
//     * @param deliveGoodsVO
//     */
//    @Transactional
//    public void deliveGoodsBatch(String shopId, DeliveGoodsVO deliveGoodsVO) {
//        for (DeliveGoodsItemVO deliveGoodsItemVO : deliveGoodsVO.getData()) {
//            ((TradeService) AopContext.currentProxy()).deliveGoods(shopId, deliveGoodsItemVO.getTradeId(),
//                    deliveGoodsItemVO.getLabel(), deliveGoodsItemVO.getLogisticsNo());
//        }
//    }

    // 获取订单发货单号
    public TradeLogEntity getLogisticsById(String shopId, String tradeId) {
        return tradeLogDAO.getById(shopId, tradeId, Constants.TradeLogCode.LOG_GOODS_START);
    }

    // 获取店铺订单相关统计信息
    public ManageCountRespVO tradeCountInfo(String shopId, String userId) {
        // 商品信息
//        int apply = goodDAO.countByShopIdAndStatus(shopId,
//                cn.kt.mall.shop.good.constant.Constants.GoodStatus.GOOD_APPLY);// 申请中数量
        int down = goodDAO.countByShopIdAndStatus(shopId, cn.kt.mall.shop.good.constant.Constants.GoodStatus.GOOD_DOWN);// 下架数量
//        int fail = goodDAO.countByShopIdAndStatus(shopId, cn.kt.mall.shop.good.constant.Constants.GoodStatus.GOOD_FAIL);// 审核不通过数量
        int pass = goodDAO.countByShopIdAndStatus(shopId, cn.kt.mall.shop.good.constant.Constants.GoodStatus.GOOD_PASS);// 审核通过数量
        // 订单数据
        int notPay = tradeDAO.countByShopIdAndStatus(shopId, Constants.TradeStatus.TRADE_NOTPAY);// 待付款
        int notSend = tradeDAO.countByShopIdAndStatus(shopId, Constants.TradeStatus.TRADE_NOTSEND);// 待发货
        int notComment = tradeDAO.countByShopIdAndStatus(shopId, Constants.TradeStatus.TRADE_RECVED);// 待评价
        int drawBack = tradeLogDAO.countAbnormal(shopId, (short) 0, Constants.TradeLogCode.LOG_MONEY_BACK);// 待退款
        int refound = tradeLogDAO.countAbnormal(shopId, (short) 0, Constants.TradeLogCode.LOG_GOODS_BACK);// 待退货
        Date endTime = new Date();
        Date startTime = DateUtil.minusTime(endTime, "6d");
        List<TradeCountVO> todayCountList = tradeDAO.tradeByShopIdCount(shopId, null, DateUtil.getDateString(startTime),
                DateUtil.getDateString(endTime));// 八天订单统计数据

        List<CountStatementVO> countStatementList = statementService.countStatementByDay(shopId);// 七日收益统计

        todayCountList = buildTradeCountData(todayCountList, startTime, endTime);
        //TODO
        BigDecimal coin = new BigDecimal("0");//walletLemService.getWallet(userId).getCoin();
        BigDecimal incomeLem = statementService.countByStatementLem(shopId);

        return new ManageCountRespVO(null, down, null, pass, notPay, notSend, notComment, drawBack, refound,
                todayCountList, coin, incomeLem == null ? CommonConstants.BIGDECIMAL_DEFAULT : incomeLem,
                countStatementList);
    }

    private static List<TradeCountVO> buildTradeCountData(List<TradeCountVO> todayCountList, Date startTime,
                                                          Date endTime) {
        Map<String, TradeCountVO> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(todayCountList)) {
            for (TradeCountVO tradeCountVO : todayCountList) {
                map.put(tradeCountVO.getDay(), tradeCountVO);
            }
        } else {
            todayCountList = new ArrayList<>();
        }
        for (Date currentDate = startTime; !currentDate.after(endTime); currentDate = DateUtil.plusTime(currentDate,
                "1d")) {
            String key = DateUtil.getDateString(currentDate);
            TradeCountVO vo = map.get(key);
            if (vo == null) {
                vo = new TradeCountVO(0, CommonConstants.BIGDECIMAL_DEFAULT, CommonConstants.BIGDECIMAL_DEFAULT, key);
                todayCountList.add(vo);
            }
        }
        return todayCountList.stream().sorted(Comparator.comparing(TradeCountVO::getDay)).collect(Collectors.toList());
    }

//    public static void main(String[] args) {
//        Date date = new Date();
//        System.out.println(buildTradeCountData(null, DateUtil.minusTime(date, "6d"), date));
//    }

    /**
     * @param pid     父ID
     * @param hashMap 数据集
     * @return
     */
    public Map<String, Object> getUserPid(String pid, Map<String, Object> hashMap) {
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

    public BigDecimal getTotalIncomeForDay(String dayString) {
        return shopTradeDAO.selectTotalMoney(dayString);
    }

    /**
     * 订单部分发货
     *
     * @param tradeId         订单ID
     * @param goodId          商品ID
     * @param logistics       物流公司
     * @param logisticsNumber 物流单号
     * @return
     */
    @Transactional
    public int tradePartialShipments(String tradeId,
                                     List<String> goodId,
                                     String logistics,
                                     String logisticsNumber,
                                     String userId) {
        A.check(logistics == null || logisticsNumber == null, "参数异常");
        //判断这个订单编号得商品是否全部发货
        TradeEntity tradeEntity = tradeDAO.getTradeById(null, tradeId);
        A.check(tradeEntity == null, "订单不存在");
        A.check(tradeEntity.getStatus().equals("2"), "订单已全部发货");
        List<TradeItemEntity> list = tradeItemDAO.getTradeItemListByTradeId(tradeId);
        A.check(list == null || list.size() <= 0, "订单详情不存在");

        int goodStatusCount = 0;
        for (TradeItemEntity tradeItemEntity : list) {
            if (tradeItemEntity.getGoodStatus() == 1) {
                goodStatusCount++;
            }

        }
        A.check(list.size() == goodStatusCount, "订单商品已经全部发货");
        for (String good : goodId) {
            //更改商品状态
            int count = tradeItemDAO.updateTradeItemByShopIdAndGoodId(tradeEntity.getId(), good, 1);
            A.check(count <= 0, "部分发货失败");
            //添加物流信息
            logisticsService.addLogistice(logisticsNumber, logistics);
            //添加物流于商品对应关系
            ShopTransportVO shopTransportVO = new ShopTransportVO();
            shopTransportVO.setId(UUID.randomUUID().toString());
            shopTransportVO.setShopId(tradeEntity.getShopId());
            shopTransportVO.setGoodId(good);
            shopTransportVO.setTradeNo(tradeEntity.getTradeNo());
            shopTransportVO.setTotalFreightFree(BigDecimal.ZERO);
            shopTransportVO.setUserId(userId);
            shopTransportVO.setTransportNo(logisticsNumber);
            shopTransportVO.setLogisticsName(logistics);
            int tranCount = shopGoodTransportDAO.addShopTranSport(shopTransportVO);
            A.check(tranCount <= 0, "添加物流信息失败");
        }
        //判断是否全部发货
        List<TradeItemEntity> tradeList = tradeItemDAO.getTradeItemListByTradeId(tradeId);
        int goodCount = 0;
        for (TradeItemEntity itemEntity : tradeList) {
            if (itemEntity.getGoodStatus() == 1) {
                goodCount++;
            }
        }
        if (goodCount == tradeList.size()) {
            // 说明全部发货
            tradeEntity.setStatus("2");

        } else {
            //部分发货
            tradeEntity.setStatus("4");
        }
        int tradeCount = tradeDAO.updateTradeStatusById(tradeEntity.getId(), tradeEntity.getStatus());
        return tradeCount;

    }

    //根据订单编号和商品查询物流信息
    public ShopTransportVO getShopTransportByShopIdAndGoodId(String shopId, String goodId, String tradeNo) {

        ShopTransportVO transportVO = shopGoodTransportDAO.getShopTransportByShopIdAndGoodId(shopId, goodId, tradeNo);
        if (transportVO == null) {
            //那么就返回订单的物流单号
            TradeEntity tradeEntity = tradeDAO.getTradeByTradeNo(null, tradeNo);
            A.check(tradeEntity == null, "订单不存在");
            List<TradeLogEntity> tradeLogEntityList = tradeLogDAO.getByTradeId(tradeEntity.getId());
            for (TradeLogEntity tradeLogEntity : tradeLogEntityList) {
                if (tradeLogEntity.getLogCode() == 2) {
                    transportVO = new ShopTransportVO();
                    transportVO.setLogisticsName(tradeLogEntity.getLogValue1());
                    transportVO.setTransportNo(tradeLogEntity.getLogValue2());
                }
            }
        }
        return transportVO;
    }

    //前台根据订单编号和商品查询物流信息
    public cn.kt.mall.shop.shop.vo.ShopTransportVO getShopTransportByShopIdAndGoodIdFront(String userId, String shopId, String goodId, String tradeId) {
        TradeEntity tradeEntity = tradeDAO.getTradeById(userId, tradeId);
        return tradeItemDAO.getShopTranSportByShopIdAndTradeIdAndGoodId(shopId, goodId, tradeEntity.getTradeNo());
    }

}
