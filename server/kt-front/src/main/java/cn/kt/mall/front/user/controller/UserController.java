package cn.kt.mall.front.user.controller;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Result;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.sms.service.SmsService;
import cn.kt.mall.common.user.entity.CertificationEntity;
import cn.kt.mall.common.user.model.UserInfo;
import cn.kt.mall.common.user.vo.*;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.service.StatementService;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.front.jwt.Subject;
import cn.kt.mall.front.user.service.UserService;
import cn.kt.mall.shop.coupon.entity.CouponEntity;
import cn.kt.mall.shop.coupon.entity.UserCouponEntity;
import cn.kt.mall.shop.coupon.entity.UserCouponLogEntity;
import cn.kt.mall.shop.coupon.entity.UserReleaseCouponLogEntity;
import cn.kt.mall.shop.coupon.service.CouponService;
import cn.kt.mall.shop.coupon.service.UserCouponEntityService;
import cn.kt.mall.shop.coupon.service.UserReleaseCouponsLogService;
import cn.kt.mall.shop.coupon.vo.CouponVO;
import cn.kt.mall.shop.coupon.vo.ExtractVO;
import cn.kt.mall.shop.trade.entity.TradeEntity;
import cn.kt.mall.shop.trade.vo.TradeVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户模块 Controller
 * Created by jerry on 2017/12/21.
 */
@Api(description = "用户-信息管理", tags = "user-info")
@RestController
public class UserController {

    private UserService userService;
    private SmsService smsService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private StatementService statementService;
    @Autowired
    private UserReleaseCouponsLogService userReleaseCouponsLogService;
    @Autowired
    private SysSettingsService sysSettingsService;
    @Autowired
    private UserCouponEntityService userCouponEntityService;

    @Autowired
    public UserController(UserService userService, SmsService smsService) {
        this.userService = userService;
        this.smsService = smsService;
    }

    @ApiOperation(value = "根据用户Id获取用户信息", notes = "返回:用户信息")
    @GetMapping("/user")
    public UserInfo getUser() {
        String userId = SubjectUtil.getCurrent().getId();
        return this.userService.queryUserById(userId);
    }


    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public Success addUser(@RequestBody RegisterVO registerVO) {
        this.userService.register(registerVO);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "判断用户名是否可用", notes = "返回：0 不可用，1 可用")
    @IgnoreJwtAuth
    @GetMapping("/user/username/valid")
    public Result<String> usernameIsValid(@RequestParam("username") String username) {
        return Response.result(this.userService.usernameIsValid(username));
    }

    @ApiOperation(value = "获取用户注册短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "mobile", paramType = "query", required = true),
            @ApiImplicitParam(value = "国家地区编码(数字)", name = "nationalCode", paramType = "query", required = false),
            @ApiImplicitParam(value = "图形验证码(字符)", name = "captchaImgWord", paramType = "query", required = false)
    })
    @GetMapping("/register/captcha")
    public Success getRegisterSms(@RequestParam("mobile") @ApiIgnore String mobile,
                                  @RequestParam(value = "nationalCode", required = false) @ApiIgnore String nationalCode,
                                  @RequestParam(value = "captchaImgWord") @ApiIgnore String captchaImgWord) {
        A.check(mobile == null || mobile.trim().length() == 0, "手机号输入不正确");
        String captchaImgSno = mobile;
        this.smsService.sendSmsCaptcha(null, nationalCode, mobile, "REGISTER", captchaImgSno, captchaImgWord);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "忘记密码")
    @PostMapping("/forget")
    public Success forget(@RequestBody ForgetVO forgetVO) {
        this.userService.forget(forgetVO);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "获取忘记密码短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "mobile", paramType = "query", required = true),
            @ApiImplicitParam(value = "国家地区编码(数字)", name = "nationalCode", paramType = "query", required = false),
            @ApiImplicitParam(value = "图形验证码(字符)", name = "captchaImgWord", paramType = "query", required = false)
    })
    @GetMapping("/forget/captcha")
    public Success forgetCaptcha(@RequestParam("mobile") @ApiIgnore String mobile,
                                 @RequestParam(value = "nationalCode", required = false) @ApiIgnore String nationalCode,
                                 @RequestParam(value = "captchaImgWord") @ApiIgnore String captchaImgWord) {
        A.check(mobile == null || mobile.trim().length() == 0, "手机号输入不正确");
        String captchaImgSno = mobile;
        this.smsService.sendSmsCaptcha(null, nationalCode, mobile, "PASSWORD_FORGET", captchaImgSno, captchaImgWord);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "重置密码")
    @IgnoreJwtAuth
    @GetMapping("/resetPassword")
    public Success resetPassword(@RequestParam(value = "mobile") String mobile, @RequestParam(value = "passwordVO") String passwordVO) {
        this.userService.resetPassword(mobile, passwordVO);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/password")
    public Success editPassword(@RequestBody PasswordVO passwordVO) {
        this.userService.updatePassword(Subject.getCurrent(), passwordVO);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "编辑个人信息", notes = "返回:用户信息")
    @PutMapping("/user/personal")
    public Success editPersonalInfo(@RequestBody EditVO editVO) {
        editVO.setUserId(Subject.getCurrent().getId());
        this.userService.editPersonalInfo(editVO);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "添加银行卡信息")
    @PutMapping("/user/addBankMessage")
    public Success addBankMessage(@RequestBody BankCardVO bankCardVO) {
        bankCardVO.setUserId(Subject.getCurrent().getId());
        this.userService.addBankMessage(bankCardVO);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "编辑银行卡信息")
    @PutMapping("/user/updateBankMessage")
    public Success editBankMessage(@RequestBody BankCardVO bankCardVO) {
        bankCardVO.setUserId(Subject.getCurrent().getId());
        this.userService.updateBankMessage(bankCardVO);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "添加身份证信息")
    @PutMapping("/user/addCertificationMessage")
    public Success addCertificationMessage(@RequestBody CertificationVO certificationVO) {
        certificationVO.setUserId(Subject.getCurrent().getId());
        this.userService.addCertificationMessage(certificationVO);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "编辑身份证信息")
    @PutMapping("/user/updatCertificationMessage")
    public Success editCertificationMessage(@RequestBody CertificationVO certificationVO) {
        certificationVO.setUserId(Subject.getCurrent().getId());
        this.userService.updatCertificationMessage(certificationVO);
        return Response.SUCCESS;
    }

    /*@ApiOperation(value = "实名认证信息判断")
    @PutMapping("/user/getCertificationMessage")
    public Boolean getCertificationMessage() {
        return  userService.getCertificationMessage(Subject.getCurrent().getId());
    }*/
    @ApiOperation(value = "获取实名认证信息")
    @GetMapping("/user/getCertificationMessage")
    public Result<CertificationEntity> getCertificationMessageByUserId() {
        CertificationEntity certificationEntity = userService.getCertificationMessage(Subject.getCurrent().getId());
        Result<CertificationEntity> result = new Result<>(certificationEntity);
        result.setResult(certificationEntity);
        return result;
    }

    @ApiOperation(value = "添加交易密码")
    @PutMapping("/user/addTransactionPassword")
    public Success addTransactionPassword(@RequestParam(value = "transactionPassword") String transactionPassword) {
        this.userService.addTransactionPassword(Subject.getCurrent().getId(), transactionPassword);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "修改交易密码")
    @PutMapping("/user/editTransactionPassword")
    public Success editTransactionPassword(@RequestBody PasswordVO passwordVO) {
        this.userService.editTransactionPassword(Subject.getCurrent(), passwordVO);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "我的优惠券列表")
    @GetMapping("/user/userCouponList")
    public List<CouponEntity> userCouponList() {
        return this.couponService.getUserCouponList(Subject.getCurrent().getId());
    }
    @ApiOperation(value = "优惠券明细(不包括优惠券)")
    @GetMapping("/user/userCouponDetailList")
    public CommonPageVO<UserCouponLogEntity>  userCouponDetailList(@RequestParam(value = "couponId") String couponId,
                                                                   @RequestParam("pageNo") int pageNo,
                                                                   @RequestParam("pageSize") int pageSize) {
        return this.couponService.getUserCouponDetailList(Subject.getCurrent().getId(),couponId,pageNo,pageSize);
    }
    @ApiOperation(value = "优惠券明细详情(不包括优惠券)")
    @GetMapping("/user/userCouponDetailListMessage")
    public TradeEntity userCouponDetailListMessage(@RequestParam(value = "tradeId") String tradeId, @RequestParam(value = "couponLogId") String couponLogId) {
        return this.couponService.getUserCouponDetailListMessage(Subject.getCurrent().getId(),tradeId,couponLogId);
    }
    @ApiOperation(value = "查询我的优惠券余额(不包括优惠券)及使用配置")
    @GetMapping("getMyCouponAndExtractConfig")
    public UserCouponEntity getMyCouponAndExtractConfig(@RequestParam(value = "couponId") String couponId) {
        List<SysSettings> sysSettingsList =  sysSettingsService.getTranferConfig();
        UserInfo userInfo = userService.queryUserById(SubjectUtil.getCurrent().getId());
        UserCouponEntity userCouponEntity = couponService.getUserCouponEntity(SubjectUtil.getCurrent().getId(),couponId);
        if(userCouponEntity != null) {
            for (SysSettings sysSettings : sysSettingsList) {
                if (sysSettings.getCode().equals("coupon_extract")) {
                    userCouponEntity.setCouponExtract(new BigDecimal(sysSettings.getLabel()));
                }else if(sysSettings.getCode().equals("transfer_start")){
                    userCouponEntity.setTransferStart(new BigDecimal(sysSettings.getLabel()));
                }else if(sysSettings.getCode().equals("transfer_increase")){
                    userCouponEntity.setTransferIncrease(new BigDecimal(sysSettings.getLabel()));
                }
            }
            userCouponEntity.setMobile(userInfo.getMobile());
        }else{
            userCouponEntity = new UserCouponEntity();
            for (SysSettings sysSettings : sysSettingsList) {
                if (sysSettings.getCode().equals("coupon_extract")) {
                    userCouponEntity.setCouponExtract(new BigDecimal(sysSettings.getLabel()));
                }else if(sysSettings.getCode().equals("transfer_start")){
                    userCouponEntity.setTransferStart(new BigDecimal(sysSettings.getLabel()));
                }else if(sysSettings.getCode().equals("transfer_increase")){
                    userCouponEntity.setTransferIncrease(new BigDecimal(sysSettings.getLabel()));
                }
            }
            userCouponEntity.setCouponNum(BigDecimal.ZERO);
            userCouponEntity.setMobile(userInfo.getMobile());
        }

        return userCouponEntity;
    }
    @ApiOperation(value = "查询我的优惠券(不包括优惠券)使用明细")
    @GetMapping("getMyExtractLogList")
    public CommonPageVO<ExtractVO> getMyExtractLogList(@RequestParam(value = "couponId") String couponId,
                                                       @RequestParam("pageNo") int pageNo,
                                                       @RequestParam("pageSize") int pageSize) {
        return  userCouponEntityService.getCouponExtractLogList(SubjectUtil.getCurrent().getId(),couponId,pageNo,pageSize);
    }
    @ApiOperation(value = "初始化彩票积分释放明细详情")
    @GetMapping("getUserCouponBaseReleaseDetailList")
    public List<UserReleaseCouponLogEntity> getUserCouponBaseReleaseDetailList() {
        return this.couponService.getUserCouponDetailListBaseMessageList(Subject.getCurrent().getId());
    }
    //@ApiOperation(value = "获取修改手机号的短信验证码")
    //@GetMapping("/mobile/captcha")
    public Success changeMobileCaptcha(@RequestParam("mobile") String mobile,
                                       @RequestParam(value = "nationalCode", required = false) String nationalCode) {
        this.userService.changeMobileCaptcha(Subject.getCurrent(), nationalCode, mobile);
        return Response.SUCCESS;
    }

    //@ApiOperation(value = "修改手机号")
    //@PutMapping("/mobile")
    public Success changeMobile(@RequestBody ChangeMobileVO changeMobileVO) {
        this.userService.changeMobile(Subject.getCurrent(), changeMobileVO);
        return Response.SUCCESS;
    }
}
