package com.spring.boot.demo.configuration;


import com.google.common.base.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.regex;

@Slf4j
@EnableSwagger2
@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket postsApi() {
        log.info("swagger configuration initializing");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("public-api")
                .apiInfo(apiInfo())
                .select()
                .paths(paths())
                .build();


    }

    private Predicate<String> paths() {
        return regex("/v1/api/naceData/*");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().
                title("Spring Boot Demo").description("Spring Boot Application with  RESTful API following Swagger Specification").build();
    }
}
