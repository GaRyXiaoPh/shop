package cn.kt.mall.common.captcha.service;

/**
 * 图形验证码服务接口
 * Created by wqt on 2018/2/26.
 */
public interface CaptchaImgService {

    String getWord(String sno);

    void check(String sno, String words);
}
