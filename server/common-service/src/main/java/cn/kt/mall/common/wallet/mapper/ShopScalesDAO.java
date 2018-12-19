package cn.kt.mall.common.wallet.mapper;


import cn.kt.mall.common.shop.vo.ShopSalesAmountVO;
import cn.kt.mall.common.wallet.entity.ShopEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface ShopScalesDAO {

    //根据用户Id查询店铺
    ShopEntity getMyShop(@Param("userId") String userId);

    int updateShopSalesAmount(ShopEntity shopEntity);

    int addShopScalesLog(ShopSalesAmountVO shopSalesAmountVO);

    // 获取店铺信息
    ShopEntity getShopEntityByShopId(@Param("shopId") String shopId);
}
