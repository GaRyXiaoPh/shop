package cn.kt.mall.offline.web.controller;

import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.ShopAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.offline.dao.OfflineShopDAO;
import cn.kt.mall.offline.entity.DataEntity;
import cn.kt.mall.offline.entity.MerInfoEntity;
import cn.kt.mall.offline.service.OfflineShopService;
import cn.kt.mall.offline.service.OrderService;
import cn.kt.mall.offline.service.SystemDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Administrator on 2018/5/24.
 */

@Api(description = "Web商圈", tags = "circle_web")
@RequestMapping("/circle/web")
@RestController
public class DataController {

    @Autowired
    private OfflineShopService offlineShopService;

    @Autowired
    private SystemDataService systemDataService;

    @Autowired
    private OrderService orderService;


    @ApiOperation(value = "查询商家信息")
    @GetMapping("getMerInfo")
    public MerInfoEntity getMerInfo() {
        return offlineShopService.getMerInfo(SubjectUtil.getCurrent().getId());
    }

    @ApiOperation(value = "首页")
    @GetMapping("homePage")
    @ShopAuth
    public Map homePage() {
        String userId = SubjectUtil.getCurrent().getId();
        return systemDataService.homePage(userId);
    }


    @ApiOperation(value = "数据统计")
    @GetMapping("dataStatistics")
    @ShopAuth
    public Map dataStatistics() {
        String userId = SubjectUtil.getCurrent().getId();
        return systemDataService.dataStatistics(userId);
    }

    //月度收益统计
    @ApiOperation(value = "月度收益统计")
    @GetMapping("monthIncome")
    @ShopAuth
    public PageVO<DataEntity> monthIncome(@RequestParam Integer pageNo,@RequestParam Integer pageSize) {
        return orderService.monthIncome(pageNo,pageSize,SubjectUtil.getCurrent().getId());
    }

    //资金流水明细
    @ApiOperation(value = "资金流水明细")
    @GetMapping("getCapitalDetail")
    public PageVO<DataEntity> getCapitalDetail(@RequestParam Integer pageNo,@RequestParam Integer pageSize) {
        return orderService.getCapitalDetail(pageNo,pageSize,SubjectUtil.getCurrent().getId());
    }

}
