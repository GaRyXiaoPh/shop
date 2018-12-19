package cn.kt.mall.im.moments.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.kt.mall.im.moments.entity.MomentsLikeEntity;
import cn.kt.mall.im.moments.vo.LikeRespVO;

@Mapper
public interface MomentsLikeEntityMapper {
	int deleteByPrimaryKey(Long id);
	
	int deleteByMomentsId(Long momentsId);

	int insert(MomentsLikeEntity record);

	int insertSelective(MomentsLikeEntity record);

	MomentsLikeEntity selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(MomentsLikeEntity record);

	int updateByPrimaryKey(MomentsLikeEntity record);

	MomentsLikeEntity selectByUserIdAndMomentsId(@Param("userId") String userId, @Param("momentsId") Long momentsId);

	List<LikeRespVO> selectLikeUserList(@Param("list") List<Long> list, @Param("userId") String userId);
}