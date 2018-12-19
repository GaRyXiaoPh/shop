package cn.kt.mall.shop.trade.service;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.CommonUtil;

import cn.kt.mall.mq.send.TradeMQSend;
import cn.kt.mall.shop.logistics.mapper.LogisticsCompanyDAO;
import cn.kt.mall.shop.logistics.service.LogisticsService;
import cn.kt.mall.shop.logistics.vo.LogisticsVO;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.mapper.ShopDAO;
import cn.kt.mall.shop.trade.constant.Constants;
import cn.kt.mall.shop.trade.entity.TradeEntity;
import cn.kt.mall.shop.trade.entity.TradeLogEntity;
import cn.kt.mall.shop.trade.mapper.ShopTradeDAO;
import cn.kt.mall.shop.trade.mapper.TradeDAO;
import cn.kt.mall.shop.trade.mapper.TradeLogDAO;
import cn.kt.mall.shop.trade.vo.DeliveGoodsItemVO;
import cn.kt.mall.shop.trade.vo.DeliveGoodsVO;
import cn.kt.mall.shop.trade.vo.ShopTradeReqVO;
import cn.kt.mall.shop.trade.vo.ShopTradeRespVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物流管理
 * Created by gwj on 2018/06/09
 */
@Service
public class ShopTradeService  {

    @Autowired
    ShopTradeDAO shopTradeDAO;
    @Autowired
    TradeDAO tradeDAO;
    @Autowired
    TradeLogDAO tradeLogDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ShopDAO shopDAO;

    @Autowired
    private LogisticsService logisticsService;
    @Autowired
    private LogisticsCompanyDAO logisticsCompanyDAO;

    /**
     * 根据条件查询订单列表
     * @param shopTradeReqVO
     * @param pageNo
     * @param pageSize
     * @return
     */
    public CommonPageVO<ShopTradeRespVO> queryShopTradeList(ShopTradeReqVO shopTradeReqVO, int pageNo, int pageSize) {

        if (!StringUtils.isEmpty(shopTradeReqVO.getShopNo())) {
            String shopId = shopDAO.getShopIdByShopNo(shopTradeReqVO.getShopNo());
            if (shopId == null) {
                //门店不存在，设置成一个不存在的门店id
                shopId = "-1";
            }
            shopTradeReqVO.setShopId(shopId);
        }

        List<ShopTradeRespVO> list = shopTradeDAO.queryShopTradeList(shopTradeReqVO, new RowBounds(pageNo, pageSize));

        if (!list.isEmpty()) {
            List<ShopEntity> shopEntityList = shopDAO.getUserIdListByShopIdList(list.stream().map(l -> l.getShopId()).collect(Collectors.toList()));
            Map<String, String> shopId2UserIdMap = new HashMap<>();
            for (ShopEntity s :shopEntityList ){
                shopId2UserIdMap.put(s.getId(), s.getUserId());
            }

            List<UserEntity> userList = userDAO.getByIdList(shopEntityList.stream().map(l -> l.getUserId()).collect(Collectors.toList()));
            Map<String, String> userId2TrueNameMap = new HashMap<>();
            for (UserEntity s :userList ){
                userId2TrueNameMap.put(s.getId(), s.getTrueName());
            }

            for( ShopTradeRespVO v : list) {
                v.setName(userId2TrueNameMap.get(shopId2UserIdMap.get(v.getShopId())));
            }

        }


        PageInfo<ShopTradeRespVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    /**
     * 根据条件查询订单列表（ 无分页信息）,用于导出
     * @param shopTradeReqVO
     * @return
     */
    public List<ShopTradeRespVO> queryShopTradeList(ShopTradeReqVO shopTradeReqVO) {

        List<ShopTradeRespVO> list = shopTradeDAO.queryShopTradeList(shopTradeReqVO);

        if (!list.isEmpty()) {
            List<ShopEntity> shopEntityList = shopDAO.getUserIdListByShopIdList(list.stream().map(l -> l.getShopId()).collect(Collectors.toList()));
            Map<String, String> shopId2UserIdMap = new HashMap<>();
            for (ShopEntity s :shopEntityList ){
                shopId2UserIdMap.put(s.getId(), s.getUserId());
            }

            List<UserEntity> userList = userDAO.getByIdList(shopEntityList.stream().map(l -> l.getUserId()).collect(Collectors.toList()));
            Map<String, String> userId2TrueNameMap = new HashMap<>();
            for (UserEntity s :userList ){
                userId2TrueNameMap.put(s.getId(), s.getTrueName());
            }

            for( ShopTradeRespVO v : list) {
                v.setName(userId2TrueNameMap.get(shopId2UserIdMap.get(v.getShopId())));
            }

        }
        return  list;
    }


    /**
     * admin后台批量发货
     * @param deliveGoodsVO
     */
    @Transactional
    public void deliveGoodsBatch(DeliveGoodsVO deliveGoodsVO) {
        for (DeliveGoodsItemVO deliveGoodsItemVO : deliveGoodsVO.getData()) {
            A.check(StringUtils.isEmpty(deliveGoodsItemVO.label) || StringUtils.isEmpty(deliveGoodsItemVO.getLogisticsNo()),"物流信息为空") ;

            deliveGoods( deliveGoodsItemVO.getTradeId(), deliveGoodsItemVO.getLabel(), deliveGoodsItemVO.getLogisticsNo());
        }
    }

    // 管理后台订单发货处理]
    private void deliveGoods(String tradeId, String label, String logisticsNo) {
        TradeEntity entity = tradeDAO.getTradeById(null, tradeId);

        ShopEntity shop = shopDAO.getShopEntityByShopId(entity.getShopId());
        A.check(shop == null , "订单对应的店铺不存在");
        A.check(shop.getWhetherLogistics().equalsIgnoreCase("1"), "该订单只能由店铺发货，管理员无法给此订单发货");

        A.check(!entity.getStatus().equals(Constants.TradeStatus.TRADE_NOTSEND), "该订单状态不能做发货操作");
        int rows = tradeDAO.setTradeStatus(entity.getId(), Constants.TradeStatus.TRADE_NOTSEND, Constants.TradeStatus.TRADE_NOTRECV);
        A.check(rows != 1, "更新状态失败");
        tradeLogDAO.insert(new TradeLogEntity(entity.getShopId(), entity.getId(), (short) 1,
                Constants.TradeLogCode.LOG_GOODS_START, Constants.TradeLogCode.LOG_GOODS_START_VALUE, null, null, null,
                label, logisticsNo));
        logisticsService.addLogistice(logisticsNo, label);
    }

    public List<LogisticsVO> getLogisticsList() {
        return logisticsCompanyDAO.getCompanyList();
    }

}
