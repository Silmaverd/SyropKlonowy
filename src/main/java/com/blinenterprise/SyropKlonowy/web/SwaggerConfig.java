package com.blinenterprise.SyropKlonowy.web;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//@PropertySource(Array("classpath:swagger.properties"))
//@ComponentScan(basePackageClasses = new Array(classOf[BooksAPI]))
class SwaggerConfig {
    private final String swaggerApiVersion = "1.0";
    private final String licenseText = "License";
    private final String title = "REST API";
    private final String description = "API for Library Managment";

    private final ApiInfo apiInfo =
            new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(swaggerApiVersion)
                .license(licenseText)
                .build();

    public Docket docket = new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(this.apiInfo)
      .pathMapping("/")
      .select()
      .paths(PathSelectors.regex("/api.*"))
            .build();


}
