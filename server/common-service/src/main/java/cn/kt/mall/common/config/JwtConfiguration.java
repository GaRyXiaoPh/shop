package cn.kt.mall.common.config;

import cn.kt.mall.common.jwt.JwtInterceptor;
import cn.kt.mall.common.jwt.JwtValidator;
import cn.kt.mall.common.jwt.PermissionValidator;
import cn.kt.mall.common.mvc.GlobalExceptionControllerAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * JWT 配置类
 * Created by jerry on 2018/1/2.
 */
@Configuration
public class JwtConfiguration extends WebMvcConfigurerAdapter {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire}")
    private int expire;

    @Value("${jwt.publicKey}")
    private String publicKey;

    @Value("${jwt.privateKey}")
    private String privateKey;

    @Autowired
    private JwtValidator jwtValidator;
    @Autowired
    private PermissionValidator permissionValidator;

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor(jwtValidator, permissionValidator);
    }

    @Bean
    public GlobalExceptionControllerAdvice globalExceptionControllerAdvice() {
        return new GlobalExceptionControllerAdvice();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger*/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/**/api-docs/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register/**")
                .excludePathPatterns("/forget/**")
                .excludePathPatterns("/schedule/**")
                .excludePathPatterns("/captcha/**")
                .excludePathPatterns("/test/**");
    }
}
