package cn.kt.mall.shop.good.mapper;

import cn.kt.mall.shop.good.entity.GoodShopCenterEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodShopCenterDao {

    int modifyStatus(@Param("shopId") String shopId, @Param("goodId") String goodId, @Param("goodStatus") String goodStatus);


    int batchGoodStatus(@Param("goodList") List<String> goodList, @Param("goodStatus") String goodStatus);

    int insertGoodShopCenter(GoodShopCenterEntity goodShopCenterEntity);

    int batchDeleteGoodByGoodIds(@Param("goodIds") String[] goodIds);

    int insertByBatchGoodShopCenter(@Param("list") List<GoodShopCenterEntity> list);
}
