package cn.kt.mall.front.password.controller;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Result;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.sms.service.SmsService;
import cn.kt.mall.front.jwt.Subject;
import cn.kt.mall.front.password.service.TransactionPasswordService;
import cn.kt.mall.front.password.vo.SetPasswordVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 交易密码
 * Created by wqt on 2018/1/29.
 */
@Api(description = "密码-交易密码", tags = "password-transaction")
@RequestMapping("/password/transaction")
@RestController
public class TransactionPasswordController {

    @Autowired
    private TransactionPasswordService transactionPasswordService;
    @Autowired
    private SmsService smsService;

    @ApiOperation(value = "设置交易密码")
    @PostMapping("")
    public Success add(@RequestBody SetPasswordVO password) {
        this.transactionPasswordService.setPassword(Subject.getCurrent(), password);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "修改交易密码")
    @PutMapping("")
    public Success edit(@RequestBody SetPasswordVO passwordVO) {
        this.transactionPasswordService.updatePassword(Subject.getCurrent(), passwordVO);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "是否已经设置交易密码", notes = "返回：0 否，1 是")
    @GetMapping("hasSetting")
    public Result<String> hasSetting() {
        return Response.result(this.transactionPasswordService.hasSettings(Subject.getCurrent().getId()));
    }
    @ApiOperation(value = "获取设置交易密码短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "mobile", paramType = "query", required = true),
            @ApiImplicitParam(value = "国家地区编码(数字)", name = "nationalCode", paramType = "query", required = false),
            @ApiImplicitParam(value = "图形验证码(字符)", name = "captchaImgWord", paramType = "query", required = false)
    })
    @GetMapping("/set")
    public Success setCaptcha(@RequestParam("mobile") @ApiIgnore String mobile,
                                 @RequestParam(value = "nationalCode", required = false) @ApiIgnore String nationalCode,
                                 @RequestParam(value ="captchaImgWord") @ApiIgnore String captchaImgWord) {
        A.check(mobile == null || mobile.trim().length() == 0, "手机号输入不正确");
        String captchaImgSno = mobile;
        this.smsService.sendSmsCaptcha(null, nationalCode, mobile, "TRANSACTION_PASSWORD", captchaImgSno, captchaImgWord);
        return Response.SUCCESS;
    }
    @ApiOperation(value = "获取修改交易密码短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "mobile", paramType = "query", required = true),
            @ApiImplicitParam(value = "国家地区编码(数字)", name = "nationalCode", paramType = "query", required = false),
            @ApiImplicitParam(value = "图形验证码(字符)", name = "captchaImgWord", paramType = "query", required = false)
    })
    @GetMapping("/edit")
    public Success editCaptcha(@RequestParam("mobile") @ApiIgnore String mobile,
                                 @RequestParam(value = "nationalCode", required = false) @ApiIgnore String nationalCode,
                                 @RequestParam(value ="captchaImgWord") @ApiIgnore String captchaImgWord) {
        A.check(mobile == null || mobile.trim().length() == 0, "手机号输入不正确");
        String captchaImgSno = mobile;
        this.smsService.sendSmsCaptcha(null, nationalCode, mobile, "TRANSACTION_PASSWORD_EDIT", captchaImgSno, captchaImgWord);
        return Response.SUCCESS;
    }
}
