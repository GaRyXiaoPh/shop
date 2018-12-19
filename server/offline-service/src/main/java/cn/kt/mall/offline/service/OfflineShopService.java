package cn.kt.mall.offline.service;

import cn.kt.mall.offline.entity.MerInfoEntity;
import cn.kt.mall.offline.entity.ShopEntity;
import cn.kt.mall.offline.vo.HomeShopVO;
import cn.kt.mall.offline.vo.SearchShopVO;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */
public interface OfflineShopService {

    /**
     * 查询商圈首页商户列表
     *
     * @param homeShopVO
     * @return
     */
    List<ShopEntity> selectHomeShopList(HomeShopVO homeShopVO);

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
     * @param userId
     * @return
     */
    MerInfoEntity getMerInfo(String userId);
}
