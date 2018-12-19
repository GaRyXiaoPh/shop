package cn.kt.mall.offline.dao;

import cn.kt.mall.offline.entity.MerInfoEntity;
import cn.kt.mall.offline.entity.ShopEntity;
import cn.kt.mall.offline.vo.HomeShopVO;
import cn.kt.mall.offline.vo.SearchShopVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */
@Mapper
public interface OfflineShopDAO {

    /**
     * 查询商圈首页商户列表
     *
     * @param city
     * @return
     */
    List<ShopEntity> selectHomeShopList(@Param("city") long city);

    /**
     * 搜索商户
     *
     * @param searchShopVO
     * @return
     */
    List<ShopEntity> searchShopList(SearchShopVO searchShopVO);

    /**
     * 获取商家信息
     *
     * @param id
     * @return
     */
    MerInfoEntity getMerInfo(@Param("id") String id);
}
