package cn.kt.mall.im.moments.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.kt.mall.im.moments.entity.MomentsImgEntity;

@Mapper
public interface MomentsImgEntityMapper {
    int deleteByPrimaryKey(Long id);
    
    int deleteByMomentsId(Long momentsId);

    int insert(MomentsImgEntity record);

    int insertSelective(MomentsImgEntity record);

    MomentsImgEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MomentsImgEntity record);

    int updateByPrimaryKey(MomentsImgEntity record);

	int insertBatch(@Param("list") List<MomentsImgEntity> list);

	List<MomentsImgEntity> selectImgList(@Param("list") List<Long> list);
}