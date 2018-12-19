package cn.kt.mall.offline.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.bitcoin.helper.LEMHelper;
import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.entity.WalletLemEntity;
import cn.kt.mall.common.wallet.enums.AssetType;
import cn.kt.mall.common.wallet.mapper.WalletLemDAO;
import cn.kt.mall.common.wallet.service.LemRateService;
import cn.kt.mall.common.wallet.service.StatementService;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.offline.constant.Constants;
import cn.kt.mall.offline.dao.CommentDAO;
import cn.kt.mall.offline.dao.OfflineUserDAO;
import cn.kt.mall.offline.dao.OrderDAO;
import cn.kt.mall.offline.entity.*;
import cn.kt.mall.offline.service.OrderService;
import cn.kt.mall.offline.vo.*;
import cn.kt.mall.shop.config.SysConfig;
import cn.kt.mall.shop.trade.mapper.TradeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/12.
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private UserAssetService userAssetService;

    @Autowired
    private TradeDAO tradeDAO;

    @Autowired
    private LemRateService lemRateService;

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private SysConfig sysConfig;

    @Autowired
    private StatementService statementService;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SysSettingsService sysSettingsService;

    @Autowired
    private WalletLemDAO walletLemDAO;

    @Autowired
    private LEMHelper lemHelper;

    @Autowired
    private OfflineUserDAO offlineUserDAO;


    /**
     * 下单
     *
     * @param orderVO
     */
    @Transactional
    @Override
    public String addOrder(OrderVO orderVO) {

        //生成订单号
        String orderId = IDUtil.getOrderId();
        //购买者id
        String userId = SubjectUtil.getCurrent().getId();

        OrderResEntity orderResEntity = new OrderResEntity(orderVO,orderId,userId,orderVO.getFlag());
        //添加订单信息
        int count = orderDAO.addOrder(orderResEntity);
        A.check(count<1,"添加订单信息失败");
        if(orderVO.getFlag()==1){
            //立即支付时有商品信息
            List<OrderDetailResEntity> orderDetailList = new ArrayList<>();
            List<OrderDetailVO> list = orderVO.getGoodsDetail();
            for(OrderDetailVO l:list){
                OrderDetailResEntity orderDetailResEntity = new OrderDetailResEntity(l,orderId,userId,orderVO.getShopId());
                orderDetailList.add(orderDetailResEntity);
            }
            count = orderDAO.addOrderDetail(orderDetailList);
            A.check(count<1,"添加订单详细信息失败!");
        }
        return orderId;
    }

    /**
     * 支付
     *
     * @param orderId
     */
    @Override
    @Transactional
    public void pay(String orderId,String userId) {

        //查询订单信息
        OrderInfoEntity orderInfoEntity = orderDAO.getOrderInfo(orderId);
        A.check(orderInfoEntity == null, "无效的支付订单");
        A.check(!Constants.ORDER_STATUS_UNPAID.equals(orderInfoEntity.getStatus()), "无效的订单状态");
        //获取订单总额
        double totalPrice = orderInfoEntity.getTotalPrice();
        A.check(totalPrice<0,"订单金额为负,订单状态不正常");
        BigDecimal lemTotal = new BigDecimal(totalPrice)
                .divide(new BigDecimal(lemRateService.getLemRate().toString()), cn.kt.mall.shop.trade.constant.Constants.SCALE, RoundingMode.FLOOR)
                .setScale(cn.kt.mall.shop.trade.constant.Constants.SCALE, RoundingMode.FLOOR);

        /**判断用户账户余额是否能够支付订单*/
        //根据用户id查询推荐人
        String  referrer = userDAO.getById(userId).getReferrer();

        UserAssetEntity fromLemEntity = userAssetService.getUserAssetByCurrency(userId, AssetType.CREDIT.getStrType());
        A.checkParam(fromLemEntity == null, "用户账户不存在");
        //判断平台账号是否存在
        WalletLemEntity toLemEntity = walletLemDAO.getWallet(sysConfig.getSysAccount());
        A.checkParam(toLemEntity == null, "平台账户不存在");

        //判断商户账户是否存在
        toLemEntity = walletLemDAO.getWallet(orderInfoEntity.getMerUserId());
        A.checkParam(toLemEntity == null, "商户账户不存在");

        //判断推荐人
        if(referrer!=null && !"".equals(referrer)){
            toLemEntity = walletLemDAO.getWallet(referrer);
            A.checkParam(toLemEntity == null, "推荐人账户不存在");
        }


        A.checkParam(fromLemEntity.getAvailableBalance().compareTo(lemTotal) < 0, "账户余额不足，不能转账");




		userAssetService.updateUserAsset(userId, AssetType.CREDIT.getStrType(), lemTotal.negate(), lemTotal, TradeType.PAY_ORDER, orderId);
        /*//更新用户账户余额
        String hash = walletLemService.transFrom(userId, sysConfig.getSysAccount(), lemTotal);

        //添加订单流水
        StatementEntity statementEntity = new StatementEntity(orderId, userId,
                null, sysConfig.getSysAccount(), null, new BigDecimal(totalPrice),
                lemTotal,TradeType.TRANSFER_IN_SYS, (short) 2, "商圈订单", hash, new Date());
        statementService.addStatement(statementEntity);*/

        //更新订单状态为成功
        orderDAO.updateOrderStatus(orderId,lemTotal.doubleValue());
    }

    @Override
    public PageVO<OrderEntity> queryOrderManageList(OrderRequestVO orderRequestVO) {
        int pageNo = orderRequestVO.getPageNo();
        int pageSize = orderRequestVO.getPageSize();
        int srcPageNo = pageNo;

        if (pageNo >= 1)
            pageNo = pageNo - 1;
        int offset = pageNo * pageSize;
        orderRequestVO.setPageNo(offset);
        orderRequestVO.setPageSize(pageSize);

        //获取店铺id
        String shopId = offlineUserDAO.getShopId(SubjectUtil.getCurrent().getId());
        orderRequestVO.setShopId(shopId);
        int count = orderDAO.queryOrderManageCount(orderRequestVO);
        //店铺订单列表
        List<OrderEntity> list = orderDAO.queryOrderManageList(orderRequestVO);
        return new PageVO<OrderEntity>(srcPageNo, pageSize, count, list);
    }

    @Override
    public OrderDetailEntity getOrderDetailInfo(String orderId) {
        //获取订单信息
        OrderInfoEntity orderInfo = orderDAO.getOrderInfo(orderId);
        A.check(orderInfo == null,"该订单不存在");
        //根据订单号查询订单商品信息
        List<OrderDetailResEntity> orderGoodInfo = orderDAO.getOrderGoodInfo(orderId);

        //查询订单评论信息
        CommentEntity commentEntity = commentDAO.getOrderComment(orderId);

        OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
        orderDetailEntity.setOrderId(orderId);
        orderDetailEntity.setPayTime(orderInfo.getPayTime());
        orderDetailEntity.setUsername(orderInfo.getUsername());
        orderDetailEntity.setTotalPrice(orderInfo.getTotalPrice());
        orderDetailEntity.setLyme(orderInfo.getLyme());
        orderDetailEntity.setOrderGoodInfo(orderGoodInfo);
        orderDetailEntity.setOrderComment(commentEntity);
        return orderDetailEntity;
    }

    @Override
    public PageVO<CircleOrderEntity> getCircleOrderInfo(String userId,int pageNo,int pageSize) {
        int srcPageNo = pageNo;
        if (pageNo>0) pageNo=pageNo-1;
        int offset = pageNo*pageSize;
        int count = orderDAO.getCircleOrderCount(userId);
        //获取商圈列表
        List<CircleOrderEntity> list = orderDAO.getCircleOrderInfo(userId,offset, pageSize);
        for(CircleOrderEntity l:list){
            String orderId = l.getOrderId();
            List<OrderDetailResEntity> goodsList = orderDAO.getOrderGoodInfo(orderId);
            OrderInfoEntity orderInfoEntity = orderDAO.getOrderInfo(orderId);
            CircleOrderInfo  circleOrderInfo = new CircleOrderInfo();
            //商品总价
            circleOrderInfo.setTotalPrice(orderInfoEntity.getTotalPrice());
            //订单总价
            circleOrderInfo.setOrderPrice(orderInfoEntity.getTotalPrice());
            //需付莱姆
            circleOrderInfo.setLem(orderInfoEntity.getLyme());
            //实付莱姆
            circleOrderInfo.setActualLem(orderInfoEntity.getLyme());
            //订单号
            circleOrderInfo.setOrderId(orderId);
            //下单时间
            circleOrderInfo.setCreateTime(orderInfoEntity.getCreateTime());
            //支付时间
            circleOrderInfo.setPayTime(orderInfoEntity.getPayTime());
            //商品信息
            circleOrderInfo.setGoodsList(goodsList);
            //订单信息
            l.setOrderInfo(circleOrderInfo);
        }
        return new PageVO<CircleOrderEntity>(srcPageNo, pageSize, count, list);
    }

    @Override
    public OrderInfoEntity getOrderInfo(String orderId) {
        OrderInfoEntity orderInfoEntity = orderDAO.getOrderInfo(orderId);
        A.check(orderInfoEntity==null,"订单不存在");
        return orderInfoEntity;
    }

    @Override
    public PageVO<DataEntity> monthIncome(Integer pageNo,Integer pageSize,String userId) {
        String  shopId = offlineUserDAO.getShopId(userId);
        int srcPageNo = pageNo;
        if (pageNo>0) pageNo=pageNo-1;
        int offset = pageNo*pageSize;
        int count = orderDAO.monthIncomeCount(shopId);
        List<DataEntity> list = orderDAO.monthIncome(offset,pageSize,shopId);
        return new PageVO<>(srcPageNo, pageSize, count, list);
    }

    @Override
    public PageVO<DataEntity> getCapitalDetail(Integer pageNo, Integer pageSize, String userId) {
        int srcPageNo = pageNo;
        if (pageNo>0) pageNo=pageNo-1;
        int offset = pageNo*pageSize;
        int count = orderDAO.getCapitalDetailCount(userId);
        List<DataEntity> list = orderDAO.getCapitalDetail(offset,pageSize,userId);
        return new PageVO<>(srcPageNo, pageSize, count, list);
    }


}
