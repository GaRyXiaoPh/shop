package cn.kt.mall.management.logistics.dao;

import cn.kt.mall.management.logistics.vo.ShopTransportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopTransportDAO {

    int addShopTranSport(ShopTransportVO shopTransportVO);

    List<ShopTransportVO> getShopTranSportByShopIdAndTradeId(@Param("shopId") String shopId, @Param("tradeNo") String tradeNo);

    int getShopTranSportByShopIdAndTradeIdAndGoodId(@Param("shopId") String shopId, @Param("tradeNo") String tradeNo,@Param("goodId") String goodId);
}
