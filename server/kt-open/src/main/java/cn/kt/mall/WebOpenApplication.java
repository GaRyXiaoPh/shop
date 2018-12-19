package cn.kt.mall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = { "cn.kt.mall" })
public class WebOpenApplication {

	private static Logger logger = LoggerFactory.getLogger(WebOpenApplication.class);

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(WebOpenApplication.class, args);
		Environment env = ctx.getEnvironment();
		String profiles = env.getProperty("spring.profiles.active");
		logger.info("WebShopApplication使用环境为:{}", profiles);
	}
}
