package cn.kt.mall.front.user.controller;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.user.model.UserInfo;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.enums.AssetType;
import cn.kt.mall.common.wallet.service.StatementService;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.front.jwt.Subject;
import cn.kt.mall.front.password.service.TransactionPasswordService;
import cn.kt.mall.front.user.service.MyAssetService;
import cn.kt.mall.front.user.service.UserService;
import cn.kt.mall.front.user.vo.MyAssetInfoVO;
import cn.kt.mall.front.user.vo.MyAssetVO;
import cn.kt.mall.shop.coupon.entity.CouponEntity;
import cn.kt.mall.shop.coupon.entity.UserCouponEntity;
import cn.kt.mall.shop.coupon.service.CouponService;
import cn.kt.mall.shop.coupon.service.UserCouponEntityService;
import cn.kt.mall.shop.coupon.vo.CouponTransferVO;
import cn.kt.mall.shop.coupon.vo.ExtractVO;
import cn.kt.mall.shop.good.vo.GoodRespVO;
import cn.kt.mall.shop.trade.entity.TradeEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(description = "商城前台-我的资产", tags = "user-myasset")
@RequestMapping("/myAsset")
@RestController
public class MyAssetController {

    @Autowired
    private MyAssetService myAssetService;
    @Autowired
    private UserAssetService userAssetService;
    @Autowired
    TransactionPasswordService transactionPasswordService;
    @Autowired
    private SysSettingsService sysSettingsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserCouponEntityService userCouponEntityService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private StatementService statementService;

    @ApiOperation(value = "查询我的资产")
    @GetMapping("queryMyAsset")
    public MyAssetVO queryMyAsset(@ApiParam(value = "用户id") @RequestParam(name = "userId", required = false) String userId) {
        return myAssetService.queryMyAsset(userId);
    }

    @ApiOperation(value = "申请提币")
    @GetMapping("/applyPopcTurnOut")
    public Success applyPopcTurnOut(@ApiParam(value = "用户id") @RequestParam(name = "userId", required = false) String userId,
                                    @ApiParam(value = "提币数量") @RequestParam(name = "count", required = false) double count,
                                    @ApiParam(value = "交易密码") @RequestParam(name = "password") String password) {
        transactionPasswordService.check(userId, password);
        myAssetService.applyPopcTurnOut(userId, count);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "查询我的资产明细记录")
    @GetMapping("queryMyAssetList")
    public PageVO<MyAssetInfoVO> queryMyAssetList(@ApiParam(value = "用户id") @RequestParam(name = "userId", required = false) String userId,
                                                        @ApiParam(value = "类别(0消费,1提币,2充值,3解冻)") @RequestParam(name = "type", required = false) int type,
                                                        @ApiParam(value = "页数") @RequestParam Integer pageNo,
                                                        @ApiParam(value = "页码") @RequestParam Integer pageSize) {
        return myAssetService.queryMyAssetList(userId, type, pageNo, pageSize);
    }
    @ApiOperation(value = "查询我的余额", notes = "tradeType 交易类型: 1:消费 2:收款 3:充值 4:给会员充值 5:扣款 6:扣除用户资金 7:解冻 8:提现")
    @GetMapping("getMyAsset")
    public UserAssetEntity getMyAsset() {
        return myAssetService.getMyAsset(SubjectUtil.getCurrent().getId());
    }

    @ApiOperation(value = "查询我的优惠券余额及转让配置")
    @GetMapping("getMyAssetAndTransferConfig")
    public UserAssetEntity getMyAssetAndTransferConfig() {
        List<SysSettings> sysSettingsList =  sysSettingsService.getTranferConfig();
        UserAssetEntity userAssetEntity = myAssetService.getMyAssetPopc(SubjectUtil.getCurrent().getId());
        if(userAssetEntity != null){
            for(SysSettings sysSettings: sysSettingsList){
                if(sysSettings.getCode().equals("inside_ratio")){
                    userAssetEntity.setInsideRatio(new BigDecimal(sysSettings.getLabel()));
                }else if(sysSettings.getCode().equals("outside_ratio")){
                    userAssetEntity.setOutsideRatio(new BigDecimal(sysSettings.getLabel()));
                }else if(sysSettings.getCode().equals("transfer_start")){
                    userAssetEntity.setTransferStart(new BigDecimal(sysSettings.getLabel()));
                }else if(sysSettings.getCode().equals("transfer_increase")){
                    userAssetEntity.setTransferIncrease(new BigDecimal(sysSettings.getLabel()));
                }
            }
        }
        return userAssetEntity;
    }

    @ApiOperation(value = "转让优惠券/使用游戏积分/使用彩票积分")
    @PostMapping("transferMyAsset")
    public Success transferMyAsset( @ApiParam(value = "转让/使用数量") @RequestParam(value = "transferNumber") BigDecimal transferNumber,
                                    @ApiParam(value = "对方账户") @RequestParam(value = "mobile") String mobile,
                                    @ApiParam(value = "优惠券Id") @RequestParam(value = "couponId") String couponId,
                                    @ApiParam(value = "彩票积分使用类型") @RequestParam(value = "useType") String useType,
                                    @ApiParam(value = "交易密码") @RequestParam(value = "password") String password) {
        transactionPasswordService.check(SubjectUtil.getCurrent().getId(), password);
        myAssetService.transferMyAsset(transferNumber,mobile,couponId,password,useType);
        return Response.SUCCESS;
    }
    @ApiOperation(value = "优惠券赠送（消费，推荐）明细详情")
    @GetMapping("getUserCouponDetailListMessageAsset")
    public TradeEntity getUserCouponDetailListMessageAsset(@RequestParam(value = "tradeId") String tradeId,
                                                           @RequestParam(value = "tradeType",required = false) String tradeType,
                                                           @RequestParam(value = "couponId") String couponId,
                                                           @RequestParam(value = "goodId") String goodId) {
        return this.couponService.getUserCouponDetailListMessageAsset(Subject.getCurrent().getId(),tradeId,tradeType,couponId,goodId);
    }
    @ApiOperation(value = "优惠券赠送（消费，推荐）明细")
    @GetMapping("getUserStatementConsumeDetailList")
    public CommonPageVO<StatementEntity> getUserStatementConsumeDetailList( @RequestParam(value = "couponId") String couponId,
                                                                            @RequestParam("pageNo") int pageNo,
                                                                            @RequestParam("pageSize") int pageSize) {
        return this.statementService.getuserCouponConsumeDetailList(Subject.getCurrent().getId(),couponId,pageNo,pageSize);
    }
    @ApiOperation(value = "优惠券消费转让明细")
    @GetMapping("userStatementTransferDetailList")
    public  CommonPageVO<StatementEntity> getuserStatementTransferDetailList( @RequestParam("pageNo") int pageNo,
                                                                              @RequestParam("pageSize") int pageSize) {
        return this.statementService.getuserStatementTransferDetailList(Subject.getCurrent().getId(),pageNo,pageSize);
    }
    @ApiOperation(value = "初始化资产释放明细详情")
    @GetMapping("getUserAssetBaseReleaseStatementDetailList")
    public List<StatementEntity> getUserAssetBaseReleaseStatementDetailList() {
        return this.statementService.getUserAssetBaseReleaseStatementDetailList(Subject.getCurrent().getId());
    }
}
