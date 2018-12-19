package cn.kt.mall.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SysConfig {

    @Autowired
    private Environment env;

    public String getSysAccount(){
        return env.getProperty("sys.platform_account");
    }
    
    public String getLemHomeUrl() {
    	return env.getProperty("sys.lem_home_url");
    }
    
}
