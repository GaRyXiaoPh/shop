package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.shop.coupon.service.CouponsService;
import cn.kt.mall.shop.coupon.vo.CouponsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 兑换券管理 Controller
 * Created by lixx on 2018/06/21.
 */
/*@Api(description = "优惠券管理", tags = "coupons-management")
@RequestMapping("/coupons")
@RestController*/
public class CouponsController {
    @Autowired
    private CouponsService couponsService;


    @ApiOperation(value = "获取优惠券列表")
    @GetMapping("searchCoupons")
    public CommonPageVO<CouponsVO> searchCoupons(
            @RequestParam(name = "id", required = false) String id,
            @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return couponsService.getCouponsList(id,pageNo, pageSize);
    }

    @ApiOperation(value = "添加/修改优惠券")
    @PostMapping("addOrUpdateCoupons")
    public Success addOrUpdateCoupons(@RequestParam(name = "id", required = false) String id,
                                      @RequestParam(name = "couponName", required = true) String couponName,
                                      @RequestParam(name = "ratio", required = true) BigDecimal ratio) {
        CouponsVO couponsVO = new CouponsVO();
        couponsVO.setId(id);
        couponsVO.setCouponName(couponName);
        couponsVO.setRatio(ratio);
        couponsService.addOrUpdateCoupons(couponsVO);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "删除优惠券")
    @GetMapping("deleteCoupons")
    public Success deleteCoupons(@RequestParam("ids") String ids) {
        couponsService.deleteCoupons(ids);
        return Response.SUCCESS;
    }

}
