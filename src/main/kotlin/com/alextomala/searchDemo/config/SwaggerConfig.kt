package com.alextomala.searchDemo.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

//@Configuration
//@EnableSwagger2
class SwaggerConfig : WebMvcConfigurationSupport() {

    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.alextomala.searchDemo"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiEndpointsInfo())
    }

    fun apiEndpointsInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("Spring Boot Search Code Example Api")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/license/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }
}