package com.example.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                // 보여주고 싶은 컨트롤러만 보여줄 수 있음
                .apis(RequestHandlerSelectors.basePackage("com.example.weather"))
                // .paths(PathSelectors.any("/read/**")) : 컨트롤러 안에서 /read/로 시작하는 API만 보여줌
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        String description = "날씨 일기를 CRUD 할 수 있는 프로젝트입니다.";
        return new ApiInfoBuilder()
                .title("영서의 날씨 일기 프로젝트 :)")
                .description(description)
                .version("2.0")
                .build();
    }
}
