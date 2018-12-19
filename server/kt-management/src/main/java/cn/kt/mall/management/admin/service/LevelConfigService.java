package cn.kt.mall.management.admin.service;

import cn.kt.mall.management.admin.vo.LevelConfigVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 等级管理接口类
 */
public interface LevelConfigService {
    /**
     * 查询等级管理列表
     */
    List<LevelConfigVO> getLevelConfigList();

    /**
     * 修改等级配置信息
     * @param id
     * @param amount
     * @param configLevel
     */
    void updateLevelConfig(String id, BigDecimal amount, String configLevel);
}
