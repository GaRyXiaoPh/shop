package cn.kt.mall.shop.trade.mapper;

import cn.kt.mall.shop.trade.entity.TradeLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TradeLogDAO {

    int insert(TradeLogEntity shopTradeLogEntity);

    int insertBatch(@Param("list") List<TradeLogEntity> list);

    int insertTradeLogEntity(TradeLogEntity tradeLogEntity);

    int deleteById(@Param("id") String id);

    int listByPageCount(@Param("shopId") String shopId,@Param("tradeId") String tradeId);

    List<TradeLogEntity> listByPage(@Param("shopId") String shopId, @Param("tradeId") String tradeId,
                                    @Param("offset")int offset,
                                    @Param("pageSize")int pageSize);

    TradeLogEntity getById(@Param("shopId") String shopId, @Param("tradeId") String tradeId, @Param("logCode") Short logCode);
    
    int updateSelectiveById(TradeLogEntity shopTradeLogEntity);

    List<TradeLogEntity> getByTradeId(@Param("tradeId") String tradeId);

    List<TradeLogEntity> getByTradeIds(@Param("tradeIds") List<String> tradeIds);
    
	int countAbnormal(@Param("shopId") String shopId,  @Param("status") Short tradeBack,  @Param("logCode") Short logCode);
}
