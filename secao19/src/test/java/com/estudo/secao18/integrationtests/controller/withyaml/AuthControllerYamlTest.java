package com.estudo.secao18.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import com.estudo.secao18.configs.TestConfigs;
import com.estudo.secao18.integrationtests.controller.withyaml.mapper.YAMLMapper;
import com.estudo.secao18.integrationtests.testcontainers.AbstractIntegrationTest;
import com.estudo.secao18.integrationtests.vo.AccountCredentialsVO;
import com.estudo.secao18.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerYamlTest extends AbstractIntegrationTest {
  
  private static YAMLMapper objectMapper;
  private static TokenVO tokenVO;

  @BeforeAll
  public static void setup() {
    objectMapper = new YAMLMapper();
  }

  @Test
  @Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException {
    AccountCredentialsVO user = new AccountCredentialsVO("joao", "admin234");

    tokenVO =
			given()
        .config(
          RestAssuredConfig
            .config()
            .encoderConfig(
              EncoderConfig
                .encoderConfig()
                .encodeContentTypeAs(
                  TestConfigs.CONTENT_TYPE_YAML, 
                  ContentType.TEXT
                )
            )
        )
        .accept(TestConfigs.CONTENT_TYPE_YAML)
        .basePath("/auth/signin")
          .port(TestConfigs.SERVER_PORT)
          .contentType(TestConfigs.CONTENT_TYPE_YAML)
        .body(user, objectMapper)
          .when()
        .post()
				.then()
          .statusCode(200)
            .extract()
              .body()
                .as(TokenVO.class, objectMapper);
      
    assertNotNull(tokenVO.getAccessToken());
    assertNotNull(tokenVO.getRefreshToken());
  }

  @Test
  @Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException {
    var newTokenVO =
			given()
      .config(
        RestAssuredConfig
          .config()
          .encoderConfig(
            EncoderConfig
              .encoderConfig()
              .encodeContentTypeAs(
                TestConfigs.CONTENT_TYPE_YAML, 
                ContentType.TEXT
              )
          )
      )
      .accept(TestConfigs.CONTENT_TYPE_YAML)
        .basePath("/auth/refresh")
          .port(TestConfigs.SERVER_PORT)
          .contentType(TestConfigs.CONTENT_TYPE_YAML)
            .pathParam("username", tokenVO.getUsername())
            .header(
              TestConfigs.HEADER_PARAM_AUTHORIZATION,
              "Bearer " + tokenVO.getRefreshToken()
            )
        .when()
          .put("{username}")
				.then()
          .statusCode(200)
        .extract()
          .body()
            .as(TokenVO.class, objectMapper);
      
    assertNotNull(newTokenVO.getAccessToken());
    assertNotNull(newTokenVO.getRefreshToken());
  }

}
