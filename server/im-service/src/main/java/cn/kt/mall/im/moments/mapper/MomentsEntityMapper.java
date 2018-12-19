package cn.kt.mall.im.moments.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.kt.mall.im.moments.entity.MomentsEntity;
import cn.kt.mall.im.moments.vo.MomentsRespVO;

@Mapper
public interface MomentsEntityMapper {
	int deleteByPrimaryKey(Long id);

	int insert(MomentsEntity record);

	int insertSelective(MomentsEntity record);

	MomentsEntity selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(MomentsEntity record);

	int updateByPrimaryKey(MomentsEntity record);

	List<MomentsRespVO> listMoments(@Param("friendIds") List<String> friendIds, RowBounds rowBounds);
}