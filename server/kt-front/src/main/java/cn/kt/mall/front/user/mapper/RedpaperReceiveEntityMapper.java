package cn.kt.mall.front.user.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.kt.mall.front.user.entity.RedpaperReceiveEntity;

@Mapper
public interface RedpaperReceiveEntityMapper {
	int deleteByPrimaryKey(Long id);

	int insert(RedpaperReceiveEntity record);

	int insertSelective(RedpaperReceiveEntity record);

	RedpaperReceiveEntity selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(RedpaperReceiveEntity record);

	int updateSelectiveByIdAndSortNo(RedpaperReceiveEntity record);
	
	int insertBatch(@Param("lists") List<RedpaperReceiveEntity> lists);
	
	List<RedpaperReceiveEntity> getByRedId(@Param("redId") String redId, @Param("userId") String userId);

	List<RedpaperReceiveEntity> getByReceivedRedId(@Param("redId") String redId, RowBounds rowBounds);

	BigDecimal countUnReceived(@Param("redId") String redId);
}