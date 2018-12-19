package cn.kt.mall.shop.trade.mapper;

import cn.kt.mall.shop.shop.vo.ShopTransportVO;
import cn.kt.mall.shop.trade.entity.TradeItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TradeItemDAO {

    /* 根据主键集合批量查询 */
    List<TradeItemEntity> getTradeItemListByIds(@Param("tradeIds") List<String> tradeItemIds);

    // 批量新增
    int insertBatch(@Param("list") List<TradeItemEntity> list);

    // 新增
    int insertTradeItem(TradeItemEntity tradeItemEntity);
    List<TradeItemEntity> getTradeItemListByTradeId(@Param("tradeId") String tradeId);
    // 根据trade主表主键id查询订单商品信息
    List<TradeItemEntity> getTradeItemListByTradeIdAndShopId(@Param("tradeId") String tradeId,@Param("shopId") String shopId);

    // 根据trade主表主键id查询订单商品信息
     int updateTradeItemByShopIdAndGoodId(@Param("tradeId") String tradeId, @Param("goodId") String goodId, @Param("goodStatus") int goodStatus);

    //查询指定订单的关联商品
    List<TradeItemEntity> getTradeItemList(@Param("tradeId") String tradeId);

    int updateShopTradeItem(TradeItemEntity tradeItemEntity);

    ShopTransportVO getShopTranSportByShopIdAndTradeIdAndGoodId(@Param("shopId") String shopId, @Param("goodId") String goodId, @Param("tradeNo") String tradeNo);

}
