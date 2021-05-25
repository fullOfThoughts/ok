package com.bmd.common.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class swaggerConfig {
    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)      //  swagger2规范
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bmd"))       //  扫描接口的包
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title("admin文档")
                        .description("详细信息，描述")
                        .version("1.1")
                        .contact(new Contact("sen", "www.///.com", "email"))       //  接口维护人
                        .license("the baidu license")       //  没有可以忽略掉
                        .licenseUrl("ww.sadsad.com")        //  没有可以忽略掉
                        .build());
    }
}
