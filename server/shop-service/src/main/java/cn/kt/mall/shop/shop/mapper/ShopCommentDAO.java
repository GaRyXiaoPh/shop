package cn.kt.mall.shop.shop.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.kt.mall.shop.shop.entity.ShopCommentEntity;
import cn.kt.mall.shop.shop.vo.ShopCommentVO;

@Mapper
public interface ShopCommentDAO {

	int insert(ShopCommentEntity shopCommentEntity);

	int deleteById(@Param("id") String id);

	int listByPageCount(@Param("shopId") String shopId, @Param("tradeId") String tradeId,
			@Param("searchName") String searchName, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	List<ShopCommentVO> listByPage(@Param("shopId") String shopId, @Param("tradeId") String tradeId,
			@Param("searchName") String searchName, @Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("offset") int offset, @Param("pageSize") int pageSize);

	ShopCommentEntity getMyCommentByTradeId(@Param("shopId") String shopId, @Param("tradeId") String tradeId,
			@Param("userId") String userId);
	
	int updateShopPoint(@Param("shopId") String shopId);
}
