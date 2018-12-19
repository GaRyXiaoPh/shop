package cn.kt.mall.front.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import cn.kt.mall.front.user.entity.RedpaperEntity;

@Mapper
public interface RedpaperEntityMapper {
    int deleteByPrimaryKey(String id);

    int insert(RedpaperEntity record);	

    int insertSelective(RedpaperEntity record);

    RedpaperEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RedpaperEntity record);

    int updateByPrimaryKey(RedpaperEntity record);
}