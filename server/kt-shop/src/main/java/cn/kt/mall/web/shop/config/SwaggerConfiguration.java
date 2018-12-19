package cn.kt.mall.web.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2 配置类
 * Created by jerry on 2017/7/25.
 */
@Profile({"dev", "test"})
@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

    /**
     * 文档初始化
     * @return Docket
     */
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(operationParameters())
                .useDefaultResponseMessages(false)
                .apiInfo(buildApiInf())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.kt.mall"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo buildApiInf(){
        return new ApiInfoBuilder()
                .title("商城线上商城  API Docs")
                .description("商城线上商城  API Docs")
                .contact(new Contact("wqt", "NONE", "NONE"))
                .version("1.0")
                .build();
    }

    private List<Parameter> operationParameters() {
        List<Parameter> list = new ArrayList<>();
        list.add(new ParameterBuilder()
                .name("access-token")
                .description("访问令牌")
                .parameterType("header")
                .required(true)
                .modelRef(new ModelRef("string"))
                .build());
        return list;
    }

}
