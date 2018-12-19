package cn.kt.mall.offline.service;

import cn.kt.mall.offline.entity.ShopInfoEntity;

/**
 * Created by Administrator on 2018/5/11.
 */
public interface ShopInfoService {
    /**
     * 根据店铺id查询店铺信息
     *
     * @param shopId
     * @return
     */
    ShopInfoEntity selectShopInfo(String shopId);
}
