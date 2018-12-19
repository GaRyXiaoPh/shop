
package cn.kt.mall.shop.logistics.service;

import java.util.List;

import cn.kt.mall.shop.logistics.entity.Logistics;
import cn.kt.mall.shop.logistics.entity.LogisticsDetail;
import cn.kt.mall.shop.logistics.vo.LogisticsReqVO;
import cn.kt.mall.shop.logistics.vo.LogisticsRespVO;
import cn.kt.mall.shop.logistics.vo.LogisticsSubReqVO;

public interface LogisticsService {

	/**
	 * 获取物流信息
	 */
	LogisticsRespVO getLogisticsInfo(LogisticsReqVO logisticsReqVO);

	/**
	 * 订阅物流信息
	 */
	boolean subscribeLogisticsInfo(LogisticsSubReqVO logisticsSubReqVO);

	/**
	 * 更新物流状态
	 */
	boolean updateLogisticsStatus(Logistics logistics);

	/**
	 * 查看物流详情
	 */
	List<LogisticsDetail> selectLogisticsDetail(String logisticsNo);

	/**
	 * 插入物流详情
	 */
	boolean delLogisticsDetail(String logisticsNo);

	/**
	 * 插入物流详情
	 */
	boolean addLogisticsDetail(LogisticsDetail logisticsDetail);

	void addLogistice(String logisticsNo, String label);

}
