package cn.kt.mall.web.shop.controller.cash;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.ShopAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.shop.cash.service.CashService;
import cn.kt.mall.shop.cash.vo.CashReqVO;
import cn.kt.mall.shop.cash.vo.CashRespVO;
import cn.kt.mall.shop.cash.vo.DeliveCashesVO;
import cn.kt.mall.shop.logistics.service.LogisticsService;
import cn.kt.mall.shop.logistics.vo.LogisticsReqVO;
import cn.kt.mall.shop.logistics.vo.LogisticsRespVO;
import cn.kt.mall.shop.trade.constant.Constants;
import cn.kt.mall.shop.trade.entity.TradeEntity;
import cn.kt.mall.shop.trade.entity.TradeLogEntity;
import cn.kt.mall.shop.trade.service.TradeAbnormalService;
import cn.kt.mall.shop.trade.service.TradeService;
import cn.kt.mall.shop.trade.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商家端提现记录模块", tags = "online-shop-cash")
@RequestMapping("/shop/cash")
@RestController
public class CashController {

    @Autowired
    private CashService cashService;

    @ApiOperation(value = "获取提现记录")
    @PostMapping("getCashList")
    @ResponseBody
    @ShopAuth
    public CommonPageVO<CashRespVO> getCashList(@RequestParam(name = "userId", required = true) String userId,
                                                @RequestParam(name = "status", required = false) String status,
                                                @RequestParam(name = "startTime", required = false) String startTime,
                                                @RequestParam(name = "endTime", required = false) String endTime,
                                                @RequestParam(name = "trueName", required = false) String trueName,
                                                @RequestParam("pageNo") int pageNo,
                                                @RequestParam("pageSize") int pageSize) {
        CashReqVO cashReqVO = new CashReqVO(userId,
                status, startTime, endTime, trueName, null);
        return cashService.getCashList(cashReqVO, pageNo, pageSize);
    }

    @ApiOperation(value = "修改提现状态")
    @PostMapping("deliveCashes")
    @ResponseBody
    @ShopAuth
    public Success deliveCashes(@RequestParam(name = "cashId") String cashId,
                                @ApiParam(value = "状态", required = true) @RequestParam(name = "status") String status) {
        cashService.deliveCashes(cashId, status);
        return Response.SUCCESS;
    }

    // 批量发货
    @ApiOperation(value = "批量修改提现状态")
    @PostMapping("deliveCashesBatch")
    @ResponseBody
    @ShopAuth
    public Success deliveCashesBatch(@RequestBody DeliveCashesVO deliveCashesVO) {
        cashService.deliveCashesBatch(deliveCashesVO);
        return Response.SUCCESS;
    }
}
