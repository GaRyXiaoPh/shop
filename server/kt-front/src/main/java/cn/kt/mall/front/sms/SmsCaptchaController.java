package cn.kt.mall.front.sms;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.sms.service.SmsService;
import cn.kt.mall.common.user.model.CurrentUser;
import cn.kt.mall.front.jwt.Subject;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(description = "短信验证码", tags = "captcha-sms")
@RequestMapping("/sms/captcha")
@RestController
public class SmsCaptchaController {

    @Autowired
    private SmsService smsService;

/*    @ApiOperation(value = "发送短信验证码", notes="返回:无", produces = "application/json")
    @GetMapping("")
    @ResponseBody
    public Success send(
            @RequestParam("type")
            @ApiParam(value = "验证码类型：TRANSACTION_PASSWORD 设置交易密码，TRANSACTION_PASSWORD_EDIT 修改交易密码", required = true)
                    String type) {
        CurrentUser currentUser = Subject.getCurrent();
        this.smsService.sendSmsCaptcha(currentUser.getId(), currentUser.getNationalCode(), currentUser.getUsername(), type);
        return Response.SUCCESS;
    }
*/

    @ApiOperation(value = "获取短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "mobile", paramType = "query", required = true),
            @ApiImplicitParam(value = "国家地区编码(数字)", name = "nationalCode", paramType = "query", required = false),
            @ApiImplicitParam(value = "图形验证码(字符)", name = "captchaImgWord", paramType = "query", required = true),
            @ApiImplicitParam(value = "验证码类型：TRANSACTION_PASSWORD 设置交易密码，TRANSACTION_PASSWORD_EDIT 修改交易密码", name="type", paramType = "query", required = true)
    })
    @GetMapping("")
    public Success forgetCaptcha(@RequestParam("mobile") @ApiIgnore String mobile,
                                 @RequestParam(value = "nationalCode", required = false) @ApiIgnore String nationalCode,
                                 @RequestParam("captchaImgWord") @ApiIgnore String captchaImgWord,
                                 @RequestParam("type") String type) {
        A.check(mobile == null || mobile.trim().length() == 0, "手机号输入不正确");
        String captchaImgSno = mobile;
        CurrentUser currentUser = Subject.getCurrent();
        this.smsService.sendSmsCaptcha(currentUser.getId(), nationalCode, mobile, type, captchaImgSno, captchaImgWord);
        return Response.SUCCESS;
    }
}
