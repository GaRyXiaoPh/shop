package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.management.admin.service.MoneyService;
import cn.kt.mall.management.admin.vo.MoneyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 资金管理 Controller
 * Created by xxx on 2018/06/21.
 */
@Api(description = "资金管理", tags = "fund-management")
@RequestMapping("/money")
@RestController
public class MoneyController {
    @Autowired
    private MoneyService moneyService;


    @ApiOperation(value = "获取提现列表")
    @PostMapping("searchMoney")
    public CommonPageVO<MoneyVO> searchMoney(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "beginTime", required = false) String beginTime,
            @RequestParam(name = "endTime", required = false) String endTime,
            @RequestParam(name = "timeType", required = false) String timeType,
            @RequestParam(name = "hasShop", required = false) String hasShop,
            @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return moneyService.getMoneyList(status,name,beginTime,endTime,timeType,hasShop,pageNo, pageSize);
    }

    @ApiOperation(value = "提现通过")
    @PostMapping("passMoney")
    public Success passMoney(String ids,String status) {
        moneyService.updateMoney(ids, status);
        return Response.SUCCESS;
    }


}
