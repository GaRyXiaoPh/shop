package cn.kt.mall.offline.dao;

import cn.kt.mall.offline.entity.GoodEntity;
import cn.kt.mall.offline.entity.ShopInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 商圈商品管理接口
 *
 * Created by chenhong on 2018/4/26.
 */
@Mapper
public interface ShopInfoDAO {

    /**
     * 根据店铺id查询店铺信息
     *
     * @param shopId
     * @return
     */
    ShopInfoEntity selectShopInfo(@Param("shopId") String shopId);

    /**
     * 根据店主id查询店铺商品信息
     *
     * @param userId
     * @return
     */
    List<GoodEntity> selectGoodInfo(@Param("userId") String userId);

}
