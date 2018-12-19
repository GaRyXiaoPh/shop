package cn.kt.mall.offline.web.user.controller;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Result;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.sms.service.SmsService;
import cn.kt.mall.common.user.model.UserInfo;
import cn.kt.mall.common.user.vo.*;
import cn.kt.mall.offline.web.jwt.Subject;
import cn.kt.mall.offline.web.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
    public UserController(UserService userService, SmsService smsService) {
        this.userService = userService;
        this.smsService = smsService;
    }

    @ApiOperation(value = "根据用户Id获取用户信息", notes = "返回:用户信息")
    @GetMapping("/user/{id}")
    public UserInfo getUser(@PathVariable("id") String id) {
        String userId = SubjectUtil.getCurrent().getId();
        return this.userService.queryUserById(id,userId);
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
            @ApiImplicitParam(value = "图形验证码(字符)", name = "captchaImgWord", paramType = "query", required = true)
    })
    @GetMapping("/register/captcha")
    public Success getRegisterSms(@RequestParam("mobile") @ApiIgnore String mobile,
                                  @RequestParam(value = "nationalCode", required = false) @ApiIgnore String nationalCode,
                                  @RequestParam("captchaImgWord") @ApiIgnore String captchaImgWord) {
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
            @ApiImplicitParam(value = "图形验证码(字符)", name = "captchaImgWord", paramType = "query", required = true)
    })
    @GetMapping("/forget/captcha")
    public Success forgetCaptcha(@RequestParam("mobile") @ApiIgnore String mobile,
                                 @RequestParam(value = "nationalCode", required = false) @ApiIgnore String nationalCode,
                                 @RequestParam("captchaImgWord") @ApiIgnore String captchaImgWord) {
        A.check(mobile == null || mobile.trim().length() == 0, "手机号输入不正确");
        String captchaImgSno = mobile;
        this.smsService.sendSmsCaptcha(null, nationalCode, mobile, "PASSWORD_FORGET", captchaImgSno, captchaImgWord);
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
