package cn.kt.mall.im.rong.config;

import io.rong.RongCloud;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 融云即时通讯配置
 * Created by Administrator on 2017/6/18.
 */
@Configuration
public class RongConfiguration {
    @Value("${rongCloud.appKey}")
    private String appKey;
    @Value("${rongCloud.appSecret}")
    private String appSecret;

    @Bean
    public RongCloud rongCloud() {
        return RongCloud.getInstance(appKey, appSecret);
    }
}
