package cn.kt.mall.offline.dao;

import cn.kt.mall.offline.entity.*;
import cn.kt.mall.offline.vo.OrderRequestVO;
import cn.kt.mall.offline.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/5/12.
 */
@Mapper
public interface OrderDAO {

	/**
	 * 添加订单
	 *
	 * @param orderResEntity
	 * @return
	 */
	int addOrder(OrderResEntity orderResEntity);

	/**
	 * 添加订单详细信息
	 *
	 * @param list
	 * @return
	 */
	int addOrderDetail(List<OrderDetailResEntity> list);

	/**
	 * 查询订单信息
	 *
	 * @param orderId
	 * @return
	 */
	OrderInfoEntity getOrderInfo(@Param("orderId") String orderId);

	/**
	 * 查询订单管理列表
	 *
	 * @param orderRequestVO
	 * @return
	 */
	List<OrderEntity> queryOrderManageList(OrderRequestVO orderRequestVO);

	int queryOrderManageCount(OrderRequestVO orderRequestVO);

	/**
	 * 更新订单状态
	 *
	 * @param actualLyme
	 * @param orderId
	 */
	void updateOrderStatus(@Param("orderId") String orderId, @Param("actualLyme") double actualLyme);

	/**
	 * 根据订单号查询订单商品信息
	 *
	 * @param orderId
	 * @return
	 */
	List<OrderDetailResEntity> getOrderGoodInfo(@Param("orderId") String orderId);

	/**
	 * 获取用户商圈订单列表
	 *
	 * @param userId
	 * @return
	 */
	List<CircleOrderEntity> getCircleOrderInfo(@Param("userId") String userId, @Param("offset") int offset,
			@Param("pageSize") int pageSize);

	/**
	 * 获取用户商圈订单列表长度
	 *
	 * @param userId
	 * @return
	 */
	int getCircleOrderCount(@Param("userId") String userId);

	/**
	 * 获取某天的交易数据
	 *
	 * @return
	 */
	List<DataEntity> getTransData(@Param("startTime") String startTime, @Param("endTime") String endTime,
			@Param("dayTime") String dayTime, @Param("shopId") String shopId);

	/**
	 * 获取线下商户月度收益统计
	 *
	 * @param shopId
	 * @return
	 */
	List<DataEntity> monthIncome(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize,
			@Param("shopId") String shopId);

	int monthIncomeCount(@Param("shopId") String shopId);

	/**
	 * 获取线下商户的资金流水明细
	 *
	 * @param offset
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	List<DataEntity> getCapitalDetail(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize,
			@Param("userId") String userId);

	int getCapitalDetailCount(@Param("userId") String userId);

	/**
	 * 获取线下商户的累计收益
	 *
	 * @param shopId
	 * @return
	 */
	double getShopIncome(@Param("shopId") String shopId);

	BigDecimal manageCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
