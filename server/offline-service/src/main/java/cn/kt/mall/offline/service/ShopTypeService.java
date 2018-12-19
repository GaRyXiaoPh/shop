package cn.kt.mall.offline.service;



import cn.kt.mall.offline.entity.ShopTypeEntity;

import java.util.List;


/**
 * 商圈接口
 * Created by chenhong on 2018/4/19.
 */
public interface ShopTypeService {

    /**
     * 添加商圈商户类型
     * @param shopTypeEntity
     */
    void insertShopType(ShopTypeEntity shopTypeEntity);

    /**
     * 查询商圈商户类型列表
     *
     * @return
     */
    List<ShopTypeEntity> getTypeList();

    /**
     * 编辑商圈商户类型
     *
     * @param shopTypeEntity
     */
    void updateShopType(ShopTypeEntity shopTypeEntity);

    /**
     * 删除商圈商户类型
     *
     * @param type
     */
    void deleteShopType(String type);

}
