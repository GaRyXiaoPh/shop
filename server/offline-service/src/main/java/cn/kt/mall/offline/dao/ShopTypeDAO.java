package cn.kt.mall.offline.dao;

import cn.kt.mall.offline.entity.ShopTypeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 商圈商户类型接口
 *
 * Created by wqt on 2018/1/29.
 */
@Mapper
public interface ShopTypeDAO {

    /**
     * 添加商圈商户类型
     *
     * @param shopTypeEntity
     */
    void insertShopType(ShopTypeEntity shopTypeEntity);

    /**
     * 判断该类型名称是否已存在
     *
     * @param name
     * @return
     */
    int checkTypeName(@Param("name") String name);

    /**
     * 判断排序序号是否已存在
     *
     * @param sort
     * @return
     */
    int checkSort(@Param("sort") int sort);

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
     * @param id
     */
    void deleteShopType(@Param("id") String id);

}
