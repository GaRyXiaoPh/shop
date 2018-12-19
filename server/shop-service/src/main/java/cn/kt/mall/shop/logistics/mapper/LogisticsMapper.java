
package cn.kt.mall.shop.logistics.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.kt.mall.shop.logistics.entity.Logistics;

@Mapper
public interface LogisticsMapper {

	int addLogistics(Logistics logistics);

	Logistics selectLogistics(@Param("logisticsNo") String logisticsNo);

	int getLogisticsCount(@Param("logisticsNo") String logisticsNo, @Param("com") String com);

	void updateLogisticsStatus(Logistics logistics);

}
