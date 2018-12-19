package cn.kt.mall.management.admin.controller;


import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.exception.BusinessException;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.ShopAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.management.admin.service.impl.UserService;
import cn.kt.mall.management.admin.vo.ShopReqVO;
import cn.kt.mall.offline.entity.GoodEntity;
import cn.kt.mall.shop.good.service.GoodService;
import cn.kt.mall.shop.good.vo.GoodReqVO;
import cn.kt.mall.shop.good.vo.GoodRespVO;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.enums.ShopLevel;
import cn.kt.mall.shop.shop.service.ShopService;
import cn.kt.mall.shop.shop.vo.ShopSalesAndTimeVO;
import cn.kt.mall.shop.shop.vo.ShopSalesVO;
import cn.kt.mall.shop.shop.vo.ShopVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@Api(description = "店铺管理", tags = "online-shop")
@RequestMapping("/shop/manage")
@RestController
public class ShopController {
    @Autowired
    private ShopService shopService;

    @Autowired
    private GoodService goodService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "店铺列表")
    @GetMapping("shops")
    public CommonPageVO<ShopVO> listUserData(
            @ApiParam("店铺编号") @RequestParam(required = false) String shopNo,
            @ApiParam("店长名称") @RequestParam(required = false) String name,
            @ApiParam("店铺类型  2: 零售店 3: 批发店") @RequestParam(required = false) String level,
            @ApiParam("开始时间") @RequestParam(required = false) String startTime,
            @ApiParam("结束时间") @RequestParam(required = false) String endTime,
            @ApiParam("店铺名称") @RequestParam(required = false) String shopName,
            @ApiParam("店长手机号") @RequestParam(required = false) String mobile,
            @ApiParam("店铺状态") @RequestParam(required = false) String status,
            @RequestParam("pageNo") int pageNo,
            @RequestParam("pageSize") int pageSize) {

        CommonPageVO<ShopVO> shopList = shopService.getShopByShopNoAndLevel(shopNo, name, level, startTime, endTime, shopName, mobile, status, pageNo, pageSize);

        return shopList;

    }

    @ApiOperation(value = "店铺详情")
    @GetMapping("shop-details")
    public ShopVO getShopById(@ApiParam("店铺id") @RequestParam(required = false) String shopId) {
        return shopService.getShopByShopId(shopId, null);
    }

    @ApiOperation(value = "调整门店级别")
    @PostMapping("level")
    public Success updateShopLevel(
            @ApiParam("店铺id") @RequestParam String shopId,
            @ApiParam("店铺类型 2: 零售店 3: 批发店") @RequestParam Integer level) {

        A.check(level < 2 || level > 3, "级别不存在");
        shopService.updateShopLevel(shopId, level);

        return Response.SUCCESS;
    }

    @ApiOperation(value = "设置订单开关")
    @PostMapping("orderOperation")
    public Success updateShopLogistics(
            @ApiParam("店铺id") @RequestParam String shopId,
            @ApiParam("是否可以发货 false: 不可以 true: 可以") @RequestParam Boolean enable) {

        shopService.updateOrderOperation(shopId, enable);

        return Response.SUCCESS;
    }

    @ApiOperation(value = "设置提币开关")
    @PostMapping("withdrawalOperation")
    public Success updateShopLogi(
            @ApiParam("店铺id") @RequestParam String shopId,
            @ApiParam("是否可以提币 false: 不可以 true: 可以") @RequestParam Boolean enable) {

        shopService.updateWithdrawalOperation(shopId, enable);

        return Response.SUCCESS;
    }

    @ApiOperation(value = "添加门店")
    @PostMapping("createShop")
    public ShopVO createShop(
            @RequestBody ShopReqVO shop) {


        A.check(StringUtils.isEmpty(shop.getShopNo()), "门店id不能为空");
        A.check(StringUtils.isEmpty(shop.getShopName()), "门店名称不能为空");
        //验证店长手机号是否存在
        UserEntity user = userService.getByMobile(shop.getMobile());
        A.check(user == null, "用户不存在");
        A.check(user.getStatus().equals("1"), "用户被禁用,不能创建店铺");
        // 验证shopNo是否存在
        String id = shopService.getShopIdByShopNo(shop.getShopNo());

        A.check(!StringUtils.isEmpty(id), "商铺ID存在");
        ShopVO shopVO = new ShopVO();
        shopVO.setShopRank(1);
        shopVO.setShopConsume(new BigDecimal(0));
        BeanUtils.copyProperties(shop, shopVO);
        try {
            String shopId = shopService.createShop(shopVO,SubjectUtil.getCurrent().getId());
            shopVO.setId(shopId);
        } catch (SQLException t) {
            t.printStackTrace();
            A.check(t.getMessage() != null, "商品有所变更，请稍后创建店铺");
        } catch (BusinessException b) {
            A.check(b.getMessage() != null, b.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return shopVO;
    }

    @ApiOperation(value = "开启店铺（批量）")
    @PostMapping("openShop")
    public Success openShop(@ApiParam("店铺id集合：使用,分隔") @RequestParam String shopIds) {
        shopService.editShopStatus(shopIds, "1");
        return Response.SUCCESS;
    }

    @ApiOperation(value = "关闭店铺（批量）")
    @PostMapping("closeShop")
    public Success closeShop(@ApiParam("店铺id集合：使用,分隔") @RequestParam String shopIds) {
        shopService.editShopStatus(shopIds, "3");
        return Response.SUCCESS;
    }

    //店铺业绩统计根据时间分组
    @ApiOperation(value = "店铺业绩统计根据时间分组")
    @PostMapping("getShopSalesVOByShopId")
    public List<ShopSalesAndTimeVO> getShopSalesVOByShopId(@ApiParam("店铺id") @RequestParam String shopId,
                                                           @ApiParam("开始时间") @RequestParam(required = false) String startTime,
                                                           @ApiParam("结束时间") @RequestParam(required = false) String endTime) {
        return shopService.getShopSalesAndTime(shopId, startTime, endTime);
    }

    //查询店铺总收入
    @ApiOperation(value = "查询店铺总收入")
    @PostMapping("getShopSalesAndPointAndCoupon")
    public ShopSalesAndTimeVO getShopSalesAndPointAndCoupon(@ApiParam("店铺id") @RequestParam String shopId
    ) {
        return shopService.getShopSalesAndPointAndCoupon(shopId);
    }

    //修改店铺接口----2018-07-25
    @ApiOperation(value = "修改店铺")
    @PostMapping("updateShopName")
    public Success updateShopName(@RequestBody ShopEntity shop) {
        A.check(StringUtils.isEmpty(shop.getId()), "未接收到参数:主键ID");
        A.check(StringUtils.isEmpty(shop.getShopName()), "商铺名称不能为空");
        A.check(StringUtils.isEmpty(shop.getShopNo()), "商铺ID不能为空");
        shopService.updateShopName(shop);
        return Response.SUCCESS;
    }

}
