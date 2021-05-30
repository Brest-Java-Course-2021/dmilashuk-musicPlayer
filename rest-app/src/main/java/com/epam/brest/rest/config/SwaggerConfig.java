package com.epam.brest.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
  
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.epam.brest.rest"))
        .paths(PathSelectors.any())
        .build()
            .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo(){
    return new ApiInfo(
            "Music player Rest API ",
            "Allows to store song descriptions into playlists",
            "1.0",
            null,
            new Contact("Denis Milashuk", "https://github.com/Brest-Java-Course-2021/dmilashuk-musicPlayer", "ilovehardrock@mail.ru"),
            null, null, Collections.emptyList());
  }
}
