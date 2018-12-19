package cn.kt.mall.offline.service;

import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.offline.entity.*;
import cn.kt.mall.offline.vo.CommentResVO;
import cn.kt.mall.offline.vo.OrderRequestVO;
import cn.kt.mall.offline.vo.OrderVO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/12.
 */
public interface OrderService {

    /**
     * 下单
     *
     * @param orderVO
     */
    String  addOrder(OrderVO orderVO);

    /**
     * 支付
     *
     * @param orderId
     * @param userId
     */
    void pay(String orderId,String userId);

    /**
     * 查询订单管理列表
     *
     * @param orderRequestVO
     * @return
     */
    PageVO<OrderEntity> queryOrderManageList(OrderRequestVO orderRequestVO);

    /**
     * 根据订单号查询订单详细信息
     *
     * @param orderId
     * @return
     */
    OrderDetailEntity getOrderDetailInfo(String orderId);

    /**
     * 获取商圈订单列表
     *
     * @param userId
     * @return
     */
    PageVO<CircleOrderEntity> getCircleOrderInfo(String userId, int pageNo, int pageSize);

    /**
     * 获取订单信息
     *
     * @param orderId
     * @return
     */
    OrderInfoEntity getOrderInfo(String orderId);

    /**
     * 获取线下商户的月度收益统计
     *
     * @param userId
     * @return
     */
    PageVO<DataEntity> monthIncome(Integer pageNo,Integer pageSize,String  userId);

    /**
     * 获取线下商户的资金流水明细
     *
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return
     */
    PageVO<DataEntity> getCapitalDetail(Integer pageNo,Integer pageSize,String  userId);
}
