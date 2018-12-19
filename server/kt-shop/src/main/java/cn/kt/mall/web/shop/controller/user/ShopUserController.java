package cn.kt.mall.web.shop.controller.user;

import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.ShopAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.util.DateUtil;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.common.wallet.vo.UserFundsVO;
import cn.kt.mall.common.wallet.vo.UserRechargeLogVO;
import cn.kt.mall.shop.User.service.ShopUserService;
import cn.kt.mall.shop.User.vo.UserRechargeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Api(value = "用户管理模块", tags = "online-user-recharge")
@RequestMapping("/user/recharge")
@RestController
public class ShopUserController {
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private UserAssetService userAssetService;
    @Autowired
    private SysSettingsService sysSettingsService;


    /**
     * 查询该店铺用户子级用户
     */
    @ApiOperation(value = "查询该店铺用户子级用户")
    @PostMapping("listUserByShopId")
    @ResponseBody
    @ShopAuth
    public CommonPageVO<UserRechargeVO> listUserByShopId(@RequestParam(name = "username", required = false) String username,
                                                         @RequestParam("pageNo") int pageNo,
                                                         @RequestParam("pageSize") int pageSize) {
        String shopId = SubjectUtil.getCurrentShop().getId();
        return shopUserService.getUserListByPid(shopId, username, pageNo, pageSize);

    }

    /**
     * 查询登陆人的资产明细
     */
    @ApiOperation(value = "查询登陆人的资产明细")
    @PostMapping("getUserAssetEntityList")
    @ResponseBody
    @ShopAuth
    public UserAssetEntity getUserAssetEntityList() {
        return userAssetService.getUserAssetByCurrency(SubjectUtil.getCurrent().getId(), "point");
    }


    /**
     * 当前店铺下充值记录
     */
    @ApiOperation(value = "当前店铺下充值记录")
    @PostMapping("getUserRechargeLogListByUserId")
    @ResponseBody
    @ShopAuth
    public CommonPageVO<UserRechargeLogVO> getUserRechargeLogListByUserId(@RequestBody UserRechargeLogVO userRechargeLogVO,
                                                                          @RequestParam("pageNo") int pageNo,
                                                                          @RequestParam("pageSize") int pageSize) {
        userRechargeLogVO.setUserId(SubjectUtil.getCurrent().getId());
        return userAssetService.getUserRechargeLogListByUserId(userRechargeLogVO, pageNo, pageSize);

    }

    /**
     * 用户资金管理分页
     */
    @ApiOperation(value = "用户资金管理分页")
    @PostMapping("getUserFundsByUserId")
    @ResponseBody
    @ShopAuth
    public CommonPageVO<UserFundsVO> getUserFundsByUserId(@RequestParam(name = "rechargeType", required = false) String rechargeType,
                                                          @RequestParam(name = "operationType", required = false) String operationType,
                                                          @RequestParam(name = "mobile", required = false) String mobile,
                                                          @RequestParam(value = "beginTime", required = false) String beginTime,
                                                          @RequestParam(value = "endTime", required = false) String endTime,
                                                          @RequestParam("pageNo") int pageNo,
                                                          @RequestParam("pageSize") int pageSize) {
        return userAssetService.getUserFundsByUserId(SubjectUtil.getCurrent().getId(), rechargeType, operationType, mobile, beginTime, endTime, pageNo, pageSize);

    }

    /**
     * 资金管理 充值或者扣除
     */
    @ApiOperation(value = "资金管理 充值或者扣除")
    @PostMapping("rechargeOthers")
    @ResponseBody
    @ShopAuth
    public Success rechargeOthers(@RequestParam(name = "userId", required = true) String userId,
                                  @RequestParam(name = "mobile", required = true) String mobile,
                                  @RequestParam(name = "rechargeType", required = true) String rechargeType,
                                  @RequestParam(name = "money", required = true) BigDecimal money,
                                  @RequestParam(name = "operationType", required = true) String operationType,
                                  @RequestParam(name = "type", required = true) String type,
                                  @RequestParam(name = "remarks", required = false) String remarks) {
        //type 商铺后台使用还是总后台使用 1：总后台 2：商品平台
        userAssetService.rechargeOthers(mobile, rechargeType, money, userId, operationType, type, remarks);
        return Response.SUCCESS;
    }


    @ApiOperation(value = "获取凯撒网虚拟币人民币价格")
    @GetMapping("coin-price")
    public BigDecimal getCoinPrice() {
        BigDecimal price = sysSettingsService.getCurrencyPrice("popc");
        return price;
    }

}
