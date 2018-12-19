package cn.kt.mall.shop.trade.mapper;


import cn.kt.mall.shop.trade.vo.ShopTradeReqVO;
import cn.kt.mall.shop.trade.vo.ShopTradeRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;

/**
 * 物流管理
 */
@Mapper
public interface ShopTradeDAO {

    List<ShopTradeRespVO> queryShopTradeList(ShopTradeReqVO shopTradeReqVO, RowBounds rowBounds);

    /**
     * 查询某天的总营业额， 不包含手续费
     * @param dayString 格式 2018-06-15
     * @return
     */
    BigDecimal selectTotalMoney(String dayString);

    BigDecimal getTotalIncomeForShop(String shopId);

    List<ShopTradeRespVO> queryShopTradeList(ShopTradeReqVO shopTradeReqVO);
}
