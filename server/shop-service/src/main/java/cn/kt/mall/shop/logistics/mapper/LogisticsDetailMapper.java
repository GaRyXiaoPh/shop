
package cn.kt.mall.shop.logistics.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.kt.mall.shop.logistics.entity.LogisticsDetail;

@Mapper
public interface LogisticsDetailMapper {

	Integer addLogisticsDetail(LogisticsDetail logisticsDetail);

	List<LogisticsDetail> selectLogisticsDetail(@Param("logisticsNo") String logisticsNo);

	void delLogisticsDetail(@Param("logisticsNo") String logisticsNo);

	LogisticsDetail selectOne(LogisticsDetail logisticsDetail);

}
