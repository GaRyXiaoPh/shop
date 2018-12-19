package cn.kt.mall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Spring Boot 启动类
 * Created by Administrator on 2017/6/14.
 */
@SpringBootApplication
@ComponentScan("cn.kt.mall")
@EnableAutoConfiguration
@EnableAspectJAutoProxy(exposeProxy=true)
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
@EnableJms
public class FrontApplication extends SpringBootServletInitializer {
    private static Logger logger = LoggerFactory.getLogger(FrontApplication.class);

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(FrontApplication.class, args);
        Environment env = ctx.getEnvironment();
        String profiles = env.getProperty("spring.profiles.active");
        logger.info("FrontApplication使用环境为:{}" , profiles);
    }

}