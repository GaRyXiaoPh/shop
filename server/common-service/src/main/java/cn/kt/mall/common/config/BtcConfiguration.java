package cn.kt.mall.common.config;

import cn.kt.mall.common.bitcoin.helper.LEMHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@SuppressWarnings("ALL")
@Configuration
public class BtcConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public LEMHelper btcHelper() {
        String username = env.getProperty("lem.username");
        String password = env.getProperty("lem.password");
        String host = env.getProperty("lem.host");
        String port = env.getProperty("lem.port");
        return new LEMHelper(username, password, host, port);
    }

}
