package cn.kt.mall.shop.good.mapper;

import cn.kt.mall.shop.good.entity.GoodTypeEntity;
import cn.kt.mall.shop.good.vo.GoodTypeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ShopGoodTypeDAO {
    /**
     * 商品分类列表
     * @param goodTypeName 商品分类名称
     * @return
     */
    List<GoodTypeVO> goodTypeList(@Param("goodTypeName") String goodTypeName);

    /**
     * 添加商品分类
     * @param goodTypeVO
     */
    void addGoodType(GoodTypeVO goodTypeVO);

    /**
     * 修改商品分类
     * @param goodTypeId 商品分类id
     * @param goodTypeName 商品分类名称
     */
    void editGoodType(@Param("goodTypeId") String goodTypeId,
                      @Param("goodTypeName") String goodTypeName);

    /**
     * 前台查询商品分类
     *
     */
    List<GoodTypeEntity> listAllType();
}
