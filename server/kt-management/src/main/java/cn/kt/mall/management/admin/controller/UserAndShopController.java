package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.ShopAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.user.entity.CertificationEntity;
import cn.kt.mall.common.user.vo.ReferrerRespVO;
import cn.kt.mall.common.user.vo.UserCouponVO;
import cn.kt.mall.common.user.vo.UserManageBaseVO;
import cn.kt.mall.common.user.vo.UserManageRespVO;
import cn.kt.mall.common.wallet.common.UserRechargeConstant;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.service.StatementService;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.management.admin.service.CertificationService;
import cn.kt.mall.management.admin.service.FundService;
import cn.kt.mall.management.admin.service.impl.UserService;
import cn.kt.mall.management.admin.vo.FundVO;
import cn.kt.mall.management.admin.vo.fund.DeliveFundsVO;
import cn.kt.mall.management.admin.vo.fund.FundReqVO;
import cn.kt.mall.shop.shop.service.ShopService;
import cn.kt.mall.shop.shop.service.TradeCommentService;
import cn.kt.mall.shop.shop.vo.ShopCommentVO;
import cn.kt.mall.shop.shop.vo.ShopVO;
import cn.kt.mall.shop.trade.service.TradeService;
import cn.kt.mall.shop.trade.vo.TradeManageReqVO;
import cn.kt.mall.shop.trade.vo.TradeManageRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(description = "会员管理", tags = "user_shop_manage")
@RequestMapping("/user/manage/")
@RestController
public class UserAndShopController {

    @Autowired
    private UserService userService;
    @Autowired
    private StatementService statementService; // 账户流水服务
    @Autowired
    private ShopService shopService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private TradeCommentService tradeCommentService;
    @Autowired
    private CertificationService certificationService;
    @Autowired
    private UserAssetService userAssetService;
    @Autowired
    private FundService fundService;

//	@ApiOperation(value = "会员列表管理(搜素+高级搜索)")
//	@PostMapping("listUserData")
//	public PageVO<UserManageRespVO> listUserData(@ApiParam("用户手机号") @RequestParam(name = "userMobile", required = false)  String userMobile,
//												 @ApiParam("推荐人手机号") @RequestParam(name = "referrerMobile", required = false)  String referrerMobile,
//												 @ApiParam("用户等级") @RequestParam(name = "level", required = false)  String level,
//												 @ApiParam("店铺类型--2:形象店  3:旗舰店") @RequestParam(name = "shopType", required = false)  String shopType,
//												 @ApiParam("所属商铺电话") @RequestParam(name = "shopMobile", required = false)  String shopMobile,
//												 @ApiParam("用户状态,0已启用，1已禁用") @RequestParam(name = "status", required = false)  String status,
//												 @ApiParam("实名审核状态，0未审核，1已通过，2已拒绝，3未实名") @RequestParam(name = "certificationStatus", required = false)  String certificationStatus,
//												 @RequestParam("pageNo") int pageNo,
//												 @RequestParam("pageSize") int pageSize) {
//
//		return userService.listUserData(userMobile, referrerMobile, level, shopType, shopMobile, status, certificationStatus, pageNo, pageSize);
//	}

    @ApiOperation(value = "查询会员列表")
    @PostMapping("listUserData")
    public PageVO<UserManageRespVO> listUserData(@ApiParam("用户手机号") @RequestParam(name = "userMobile", required = false) String userMobile,
                                                 @ApiParam("用户状态,0已启用，1已禁用") @RequestParam(name = "status", required = false) String status,
                                                 @ApiParam("实名审核状态，0未审核，1已通过，2已拒绝，3未实名") @RequestParam(name = "certificationStatus", required = false) String certificationStatus,
                                                 @RequestParam("pageNo") int pageNo,
                                                 @RequestParam("pageSize") int pageSize) {

        return userService.queryUserList(userMobile, status, certificationStatus, pageNo, pageSize);
    }

    @ApiOperation(value = "会员恢复(批量)")
    @PostMapping("passUser")
    public Success passUser(@ApiParam("会员id集合：使用,分隔") @RequestParam(name = "userIds", required = false) String userIds) {
        userService.passUser(userIds);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "会员禁用(批量)")
    @PostMapping("failUser")
    public Success failUser(@ApiParam("会员id集合：使用,分隔") @RequestParam(name = "userIds", required = false) String userIds) {
        userService.failUser(userIds);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "查询会员等级(用于筛选)")
    @GetMapping("queryUserLevel")
    public List<String> queryUserLevel() {
        return userService.queryUserLevel();
    }

    @ApiOperation(value = "获取会员基本信息")
    @PostMapping("getUserBaseInfo")
    public UserManageBaseVO getUserBaseInfo(@RequestParam("userId") String userId) {
        return userService.getUserBaseInfo(userId);
    }

    @ApiOperation(value = "获取会员店铺信息")
    @PostMapping("getUserShopInfo")
    public List<ShopVO> getUserShopInfo(@RequestParam("userId") String userId) {
        return shopService.manageShopList(userId);
    }

    @ApiOperation(value = "获取会员优惠券列表")
    @PostMapping("getUserCouponList")
    public List<UserCouponVO> getUserCouponList(@RequestParam("userId") String userId) {
        return userService.getUserCouponList(userId);
    }

    @ApiOperation(value = "获取账户流水")
    @GetMapping("getStatement")
    public PageVO<StatementEntity> getStatement(@RequestParam("userId") String userId,
                                                @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return statementService.getStatement(userId, pageNo, pageSize);
    }

    @ApiOperation(value = "获取会员推荐列表")
    @PostMapping("refereeUserList")
    public CommonPageVO<ReferrerRespVO> getRefereeUserByUserId(@RequestParam("userId") String userId,
                                                               @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return userService.getRefereeUserByUserId(userId, pageNo, pageSize);
    }

    @ApiOperation(value = "该账号消费明细")
    @GetMapping("userConsumeList")
    public CommonPageVO<TradeManageRespVO> userConsumeList(@RequestParam("userId") String userId,
                                                           @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        TradeManageReqVO tradeManageReqVO = new TradeManageReqVO();
        tradeManageReqVO.setUserId(userId);
        tradeManageReqVO.setStatus("12");
        tradeManageReqVO.setFlag((short) 3);
        return tradeService.listTrade(tradeManageReqVO, pageNo, pageSize);
    }

    @ApiOperation(value = "店铺订单明细")
    @GetMapping("shopTradeList")
    public CommonPageVO<TradeManageRespVO> shopTradeList(@RequestParam("shopId") String shopId,
                                                         @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        TradeManageReqVO tradeManageReqVO = new TradeManageReqVO();
        tradeManageReqVO.setShopId(shopId);
        tradeManageReqVO.setStatus("12");
        tradeManageReqVO.setFlag((short) 3);
        return tradeService.listTrade(tradeManageReqVO, pageNo, pageSize);
    }

    // 获取店铺评论列表
//	@ApiOperation(value = "获取店铺评论列表")
//	@GetMapping("getShopComment")
    public PageVO<ShopCommentVO> getShopComment(@RequestParam("shopId") String shopId,
                                                @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return tradeCommentService.getShopComment(shopId, null, null, null, pageNo, pageSize);
    }

    @ApiOperation(value = "获取实名认证列表")
    @PostMapping("getCertificationList")
    public PageVO<CertificationEntity> getCertificationList(@ApiParam(value = "审核状态，0未审核，1已通过，2已拒绝") @RequestParam(name = "status", required = false) String status,
                                                            @ApiParam(value = "用户姓名") @RequestParam(name = "trueName", required = false) String trueName,
                                                            @ApiParam(value = "开始时间") @RequestParam(name = "startTime", required = false) String startTime,
                                                            @ApiParam(value = "结束时间") @RequestParam(name = "endTime", required = false) String endTime,
                                                            @ApiParam(value = "页数") @RequestParam Integer pageNo,
                                                            @ApiParam(value = "页码") @RequestParam Integer pageSize) {
        return certificationService.getCertificationList(status, trueName, startTime, endTime, pageNo, pageSize);
    }

    @ApiOperation(value = "实名审核通过(批量)")
    @GetMapping("CertificationPass")
    public Success CertificationPass(@ApiParam("审核id集合：使用,分隔") @RequestParam(name = "userIds", required = false) String userIds) {
        certificationService.editCertificationStatus("1", userIds);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "实名审核拒绝(批量)")
    @GetMapping("CertificationRefuse")
    public Success CertificationRefuse(@ApiParam("审核id集合：使用,分隔") @RequestParam(name = "userIds", required = false) String userIds) {
        certificationService.editCertificationStatus("2", userIds);
        return Response.SUCCESS;
    }

    /**
     * 资金管理 充值或者扣除
     */
    @ApiOperation(value = "会员资金管理-充值或扣除")
    @PostMapping("rechargeOthers")
    @ResponseBody
    @ShopAuth
    public Success rechargeOthers(@RequestParam("mobile") String mobile,
                                  @ApiParam("操作类型：1充值 2扣除") @RequestParam("operationType") String operationType,
                                  @RequestParam("money") BigDecimal money,
                                  @RequestParam(name="remarks", required = false) String remarks) {
        //type 商铺后台使用还是总后台使用 1：总后台 2：店铺后台
        userAssetService.rechargeOthers(mobile, "point", money, SubjectUtil.getCurrent().getId(), operationType, UserRechargeConstant.USER_ADMIN_SHOP, remarks);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "获取资金管理列表")
    @PostMapping("searchFund")
    public CommonPageVO<FundVO> searchFund(FundReqVO fundReqVO,
                                           @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return fundService.getFundList(fundReqVO, pageNo, pageSize);
    }

    /**
     * @param
     * @return
     */
    @ApiOperation(value = "资金审核")
    @PostMapping("passFund")
    public Success passFund(@RequestParam("id") String id, @RequestParam("status") String status) {
        fundService.passFund(id, status);
        return Response.SUCCESS;
    }

    // 批量审核
    @ApiOperation(value = "批量修改资金审核状态")
    @PostMapping("deliveFundsBatch")
    @ResponseBody
    @ShopAuth
    public Success deliveFundsBatch(@RequestBody DeliveFundsVO deliveFundsVO) {
        fundService.deliveFundsBatch(deliveFundsVO);
        return Response.SUCCESS;
    }

}
