package cn.kt.mall.shop.collect.mapper;

import cn.kt.mall.shop.collect.entity.GoodCollectEntity;
import cn.kt.mall.shop.collect.entity.ShopCollectEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CollectDAO {

    //收藏店铺
    int addShopCollect(@Param("id")String id, @Param("userId")String userId, @Param("shopId")String shopId);

    int delShopCollect(@Param("userId")String userId, @Param("shopIds")String[] shopIds);

    ShopCollectEntity getShopCollectEntity(@Param("userId")String userId, @Param("shopId")String shopId);

    int getShopCollectEntityListCount(@Param("userId")String userId);

    List<ShopCollectEntity> getShopCollectEntityList(@Param("userId")String userId, @Param("offset")int offset, @Param("pageSize")int pageSize);

    //收藏商品
    int addGoodCollect(@Param("id")String id, @Param("userId")String userId, @Param("goodId")String goodId);

    int delGoodCollectBatch(@Param("userId")String userId, @Param("goodIds")String[] goodIds);

    GoodCollectEntity getGoodCollectEntity(@Param("userId")String userId, @Param("goodId")String goodId);

    int getGoodCollectEntityListCount(@Param("userId")String userId);

    List<GoodCollectEntity> getGoodCollectEntityList(@Param("userId")String userId, @Param("offset")int offset, @Param("pageSize")int pageSize);

}
