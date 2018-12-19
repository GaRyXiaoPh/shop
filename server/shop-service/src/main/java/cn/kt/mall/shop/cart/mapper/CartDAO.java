package cn.kt.mall.shop.cart.mapper;

import cn.kt.mall.shop.cart.entity.CartEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartDAO {

	// 添加购物车
	int addCart(CartEntity cartEntity);

	int delCart(@Param("id") String id);

	// 批量删除购物车
	int delCartByBatch(@Param("userId") String userId, @Param("ids") String[] ids);

	int updateCart(CartEntity cartEntity);

	// 我的购物车根据购买者ID
	List<CartEntity> getCartByBuyUserId(@Param("buyUserId") String userId);

	// 根据店铺统计
	List<CartEntity> getCartByShopId(@Param("shopId") String shopId);

	// 根据商品ID统计
	List<CartEntity> getCartByGoodId(@Param("goodId") String goodId);

	List<CartEntity> getByShopIdAndUserId(@Param("shopId") String shopId, @Param("goodId") String goodId, @Param("userId") String userId);

	// 根据用户id获取购物车内商品数量
	int getCartGoodsCountByUserId(@Param("userId") String userId);
}
