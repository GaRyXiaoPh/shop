package cn.kt.mall.common.captcha.controller;

import cn.kt.mall.common.captcha.service.CaptchaImgService;
import cn.kt.mall.common.captcha.util.CageUtil;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(description = "图形验证码", tags = "captcha-img")
@RequestMapping("/captcha/img")
@RestController
public class CaptchaImgController {

    private CaptchaImgService captchaImgService;

    @Autowired
    public CaptchaImgController(CaptchaImgService captchaImgService) {
        this.captchaImgService = captchaImgService;
    }

    @ApiOperation(value = "获取账户图形验证码", notes = "返回:图片文件")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "mobile", paramType = "query", required = true)
    })
    @ApiResponse(code = 200, message = "图片文件")
    @GetMapping("word.jpg")
    @IgnoreJwtAuth
    public ResponseEntity<byte[]> getNew(@RequestParam("mobile") @ApiIgnore String mobile,
                                         @RequestParam(value = "type", required = false, defaultValue = "G") @ApiIgnore String type) {
        String words = this.captchaImgService.getWord(mobile);
        System.out.println(words);
        return CageUtil.generate(type, words);
    }
}
