package cn.kt.mall.im.moments.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.kt.mall.im.moments.entity.MomentsCommentEntity;
import cn.kt.mall.im.moments.vo.CommentRespVO;

@Mapper
public interface MomentsCommentEntityMapper {
    int deleteByPrimaryKey(Long id);
    
    int deleteByMomentsId(Long momentsId);

    int insert(MomentsCommentEntity record);

    int insertSelective(MomentsCommentEntity record);

    MomentsCommentEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MomentsCommentEntity record);

    int updateByPrimaryKey(MomentsCommentEntity record);

	List<CommentRespVO> selectCommentList(@Param("list") List<Long> momentsIds, @Param("userId") String userId);
}