package cn.kt.mall.management.admin.dao;

import cn.kt.mall.management.admin.vo.LevelConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface LevelConfigDAO {
    /**
     * 查询等级管理列表
     * @return
     */
    List<LevelConfigVO> getLevelConfigList();

    /**
     * 更新等级配置信息
     * @return
     */
   int updateLevelConfig(@Param("id") String id, @Param("amount") BigDecimal amount, @Param("configLevel") String configLevel);

}
