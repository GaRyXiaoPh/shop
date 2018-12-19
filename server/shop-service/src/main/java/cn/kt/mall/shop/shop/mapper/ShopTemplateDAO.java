package cn.kt.mall.shop.shop.mapper;

import cn.kt.mall.shop.shop.entity.ShopTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface ShopTemplateDAO {

	int insert(ShopTemplateEntity shopTemplateEntity);

	int updateByIdAndShopId(ShopTemplateEntity shopTemplateEntity);

	List<ShopTemplateEntity> listByPage(@Param("shopId") String shopId, RowBounds rowBounds);

	int deleteById(@Param("id") String id, @Param("shopId") String shopId);

	ShopTemplateEntity getByIdAndShopId(@Param("id") String id, @Param("shopId") String shopId);

	List<ShopTemplateEntity> getByShopId(@Param("shopId") String shopId);

}
