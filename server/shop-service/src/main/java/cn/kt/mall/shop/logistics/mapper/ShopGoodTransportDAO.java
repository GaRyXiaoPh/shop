package cn.kt.mall.shop.logistics.mapper;


import cn.kt.mall.shop.logistics.vo.ShopTransportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopGoodTransportDAO {

    int addShopTranSport(ShopTransportVO shopTransportVO);

    List<ShopTransportVO> getShopTranSportByShopIdAndTradeId(@Param("shopId") String shopId, @Param("tradeNo") String tradeNo);

    //根据订单编号和商品查询物流信息
    ShopTransportVO getShopTransportByShopIdAndGoodId(@Param("shopId") String shopId, @Param("goodId") String goodId,@Param("tradeNo") String tradeNo);
}
