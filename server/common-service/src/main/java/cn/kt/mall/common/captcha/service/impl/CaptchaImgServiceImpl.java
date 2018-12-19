package cn.kt.mall.common.captcha.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.captcha.service.CaptchaImgService;
import cn.kt.mall.common.captcha.util.CageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 图形验证码业务实现类
 * Created by wqt on 2018/2/26.
 */
@Service
public class CaptchaImgServiceImpl implements CaptchaImgService {

    private static final int LEN = 4;
    private static final int OVERTIME = 3;
    private static final String PRE_KEY = "CAPTCHA_IMG_";

    private RedisTemplate<String, String> redUsersTemplate;

    @Autowired
    public CaptchaImgServiceImpl(RedisTemplate<String, String> redUsersTemplate) {
        this.redUsersTemplate = redUsersTemplate;
        this.redUsersTemplate.setKeySerializer(new StringRedisSerializer());
        this.redUsersTemplate.setValueSerializer(new StringRedisSerializer());
    }

    @Override
    public String getWord(String sno) {
        String word = CageUtil.getWords(LEN);
        this.redUsersTemplate.opsForValue().set(PRE_KEY + sno, word, OVERTIME, TimeUnit.MINUTES);
        return word;
    }

    @Override
    public void check(String sno, String words) {
        String key = PRE_KEY + sno;
        String targetWords = this.redUsersTemplate.opsForValue().get(key);
        A.check(targetWords == null, "图形验证码超时或错误");
        A.check(!targetWords.toLowerCase().equals(words.toLowerCase()), "图形验证码错误");
        this.redUsersTemplate.delete(key);
    }
}
