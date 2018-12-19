package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.util.DateUtil;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.common.wallet.vo.UnfreezeVO;
import cn.kt.mall.management.admin.entity.CouponEntity;
import cn.kt.mall.management.admin.entity.CouponTimeConfigEntity;
import cn.kt.mall.management.admin.service.CouponService;
import cn.kt.mall.management.admin.service.CouponTimeConfigService;
import cn.kt.mall.management.admin.service.UnfreezeService;
import cn.kt.mall.management.admin.vo.UnfreezeInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api(description = "赠送管理模块", tags = "manager-unfreeze")
@RequestMapping("/manage/")
@RestController
public class UnfreezeControllor {

    @Autowired
    private UnfreezeService unfreezeService;
    @Autowired
    private UserAssetService userAssetService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponTimeConfigService couponTimeConfigService;


    @ApiOperation(value = "解冻列表")
    @GetMapping("unfreezeList")
    public PageVO<UnfreezeVO> unfreezeList(@ApiParam(value = "开始时间") @RequestParam(name = "start", required = false) String start,
                                           @ApiParam(value = "结束时间") @RequestParam(name = "end", required = false) String end,
                                           @ApiParam(value = "页数") @RequestParam Integer pageNo,
                                           @ApiParam(value = "页码") @RequestParam Integer pageSize) {
        Date startTime = DateUtil.getDate(start);
        Date endTime = DateUtil.getDate(end);
        return unfreezeService.unfreezeList(startTime, endTime, pageNo, pageSize);
    }

    @ApiOperation(value = "查询当日营业额")
    @PostMapping("queryUnfreezeInfo")
    public UnfreezeInfoVO queryUnfreezeInfo() {
        return unfreezeService.queryUnfreezeInfo();
    }

    @ApiOperation(value = "释放冻结金额 -- 测试接口 ")
    @PostMapping("releaseCoin")
    public Success releaseCoin(@ApiParam(value = "") @RequestParam String userId) {
        userAssetService.releaseCoin("2018-08-14","popc",new BigDecimal("1")
                ,new BigDecimal("0.2"),userId,new BigDecimal("100"),new BigDecimal("100") , 1);

        return Response.SUCCESS;
    }

    @ApiOperation(value = "查询优惠券返还配置")
    @PostMapping("getCouponEntityBySendDay")
    public List<CouponEntity> getCouponEntityBySendDay() {
        return couponService.getCouponEntityBySendDay();
    }

    @ApiOperation(value = "编辑优惠券返还配置")
    @PostMapping("updateCoupon4sendDays")
    public Success updateCoupon4sendDays(@ApiParam(value = "优惠券ID") @RequestParam(name = "id", required = true) String id,
                                               @ApiParam(value = "返还天数") @RequestParam(name = "sendDays", required = true) Integer sendDays) {
        couponService.updateCoupon4sendDays(id, sendDays);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "新增假期")
    @PostMapping("addCouponTime")
    public Success  addCouponTime(@ApiParam(value = "假期，多个日期以,分割") @RequestParam(name = "dateList", required = true) List<String> dateList){
        couponTimeConfigService.addCouponTimeConfig(dateList);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "删除假期")
    @PostMapping("delCouponTime")
    public Success  delCouponTime(@ApiParam(value = "假期，多个id以,分割") @RequestParam(name = "idList", required = true) List<String> idList){
        couponTimeConfigService.delCouponTimeConfig(idList);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "查询假期")
    @PostMapping("couponTimeList")
    public List<CouponTimeConfigEntity>  couponTimeList(){
        List<CouponTimeConfigEntity> list = couponTimeConfigService.getCouponTimeConfigList();
        return list;
    }

}
