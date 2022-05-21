package com.spring.boot.demo.configuration;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Slf4j
@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        log.info("swagger configuration initializing");
        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(Boolean.TRUE)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.spring.boot.demo.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().
                title("Spring Boot Application Using using JPA+Hibernate").description("Spring Boot Application with  RESTFul API following Swagger Specification").build();
    }
}
