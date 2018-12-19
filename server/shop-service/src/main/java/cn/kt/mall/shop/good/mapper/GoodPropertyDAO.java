package cn.kt.mall.shop.good.mapper;

import cn.kt.mall.shop.good.entity.GoodImgEntity;
import cn.kt.mall.shop.good.entity.GoodPropertyEntity;
import cn.kt.mall.shop.good.entity.GoodTypeEntity;
import cn.kt.mall.shop.good.vo.AdResVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodPropertyDAO {

    // 获取商品类型
    GoodTypeEntity getGoodTypeById(@Param("id") String id);

    int addGoodType(GoodTypeEntity goodTypeEntity);

    int updateGoodType(GoodTypeEntity goodTypeEntity);

    int delGoodType(@Param("id") String id);

    List<GoodTypeEntity> getGoodType(@Param("parentId") String parentId);

    List<GoodTypeEntity> listAllType();

    List<AdResVO> getAdResVOList();

    List<GoodPropertyEntity> getGoodProperty(@Param("goodId") String goodId);

    List<GoodImgEntity> getGoodImg(@Param("goodId") String goodId);

    List<GoodTypeEntity> listByIds(@Param("ids") List<String> ids);

    // 商品属性添加修改删除
    int addGoodImg(@Param("list") List<GoodImgEntity> list);

    int addGoodProperty(@Param("list") List<GoodPropertyEntity> list);

    int delGoodImg(@Param("goodId") String goodId);

    int delGoodProperty(@Param("goodId") String goodId);

    int addGoodImgs(GoodImgEntity goodImgEntity);

}
