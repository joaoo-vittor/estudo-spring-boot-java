package com.estudo.secao15.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
      .info(
        new Info()
          .title("RESTfull API with Java 18 and Spring Boot 3")
          .version("v1")
          .description("Some description about your API")
          .termsOfService("https://github.com/git/git-scm.com/blob/main/MIT-LICENSE.txt")
          .license(
            new License()
              .name("MIT")
              .url("https://github.com/git/git-scm.com/blob/main/MIT-LICENSE.txt")
          )
      );
  }

}
