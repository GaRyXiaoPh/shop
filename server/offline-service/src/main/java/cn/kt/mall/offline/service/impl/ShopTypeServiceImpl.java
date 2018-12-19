package cn.kt.mall.offline.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.offline.dao.ShopTypeDAO;
import cn.kt.mall.offline.entity.ShopTypeEntity;
import cn.kt.mall.offline.service.ShopTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 商圈实现类
 * Created by chenhong on 2018/4/23.
 */
@Service("shopTypeService")
public class ShopTypeServiceImpl implements ShopTypeService {

    @Autowired
    private ShopTypeDAO shopTypeDAO;

    /**
     * 添加商圈商户类型
     *
     * @param shopTypeEntity
     */
    @Override
    public void insertShopType(ShopTypeEntity shopTypeEntity) {
        A.checkParam(shopTypeDAO.checkTypeName(shopTypeEntity.getName())>0, "该类型名称已存在");
        A.checkParam(shopTypeDAO.checkSort(shopTypeEntity.getSort())>0, "排序序号已存在");
        shopTypeEntity.setId(IDUtil.getUUID());
        shopTypeDAO.insertShopType(shopTypeEntity);
    }

    /**
     * 获取商圈商户类型列表
     *
     * @return
     */
    @Override
    public List<ShopTypeEntity> getTypeList() {
        return shopTypeDAO.getTypeList();
    }

    /**
     * 编辑商圈商户类型
     *
     * @param shopTypeEntity
     */
    @Override
    public void updateShopType(ShopTypeEntity shopTypeEntity) {
        shopTypeDAO.updateShopType(shopTypeEntity);
    }

    /**
     * 删除商圈商户类型
     *
     * @param id
     */
    @Override
    public void deleteShopType(String id) {
        shopTypeDAO.deleteShopType(id);
    }
}
