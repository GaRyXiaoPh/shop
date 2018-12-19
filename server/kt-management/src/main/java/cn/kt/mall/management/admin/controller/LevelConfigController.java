package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.management.admin.service.LevelConfigService;
import cn.kt.mall.management.admin.service.MoneyService;
import cn.kt.mall.management.admin.vo.LevelConfigVO;
import cn.kt.mall.management.admin.vo.MoneyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 等级管理 Controller
 */
@Api(description = "等级管理", tags = "levelConfig-management")
@RequestMapping("/levelConfig")
@RestController
public class LevelConfigController {

    @Autowired
    private LevelConfigService levelConfigService;

    @ApiOperation(value = "获取等级配置信息列表")
    @PostMapping("getLevelConfigList")
    public List<LevelConfigVO> getLevelConfigList() {
        return levelConfigService.getLevelConfigList();
    }

    @ApiOperation(value = "修改等级配置信息")
    @PostMapping("updateLevelConfig")
    public Success updateLevelConfig(String id, BigDecimal amount, String configLevel) {
        levelConfigService.updateLevelConfig(id, amount, configLevel);
        return Response.SUCCESS;
    }


}
