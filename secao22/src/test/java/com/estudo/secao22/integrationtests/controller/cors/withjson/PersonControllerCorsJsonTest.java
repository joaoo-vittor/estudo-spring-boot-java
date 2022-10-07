package com.estudo.secao22.integrationtests.controller.cors.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import com.estudo.secao22.configs.TestConfigs;
import com.estudo.secao22.integrationtests.testcontainers.AbstractIntegrationTest;
import com.estudo.secao22.integrationtests.vo.AccountCredentialsVO;
import com.estudo.secao22.integrationtests.vo.PersonVO;
import com.estudo.secao22.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerCorsJsonTest extends AbstractIntegrationTest {
  
  private static RequestSpecification specification;
  private static ObjectMapper objectMapper;

  private static PersonVO person;

  @BeforeAll
  public static void setup() {
    objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    person = new PersonVO();
  }

  @Test
  @Order(0)
	public void testAuthorization() throws JsonMappingException, JsonProcessingException {
    AccountCredentialsVO user = new AccountCredentialsVO("joao", "admin234");

    var accessToken =
			given()
        .basePath("/auth/signin")
          .port(TestConfigs.SERVER_PORT)
          .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .body(user)
          .when()
        .post()
				.then()
          .statusCode(200)
            .extract()
              .body()
                .as(TokenVO.class)
              .getAccessToken();
    
    specification = new RequestSpecBuilder()
      .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
      .setBasePath("/api/v1/person")
      .setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
      .build();
  }

  @Test
  @Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
    mockPerson();

    var content =
			given()
        .spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_DEVJOAO)
            .body(person)
          .when()
            .post()
				.then()
          .statusCode(200)
            .extract()
              .body()
                .asString();
		
    PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
    person = createdPerson;

    assertNotNull(createdPerson);

    assertNotNull(createdPerson.getAddress());
    assertNotNull(createdPerson.getFirstName());
    assertNotNull(createdPerson.getLastName());
    assertNotNull(createdPerson.getGender());
    assertNotNull(createdPerson.getId());

		assertTrue(createdPerson.getId() > 0);

    assertEquals("New York City, New York, US", createdPerson.getAddress());
    assertEquals("Richard", createdPerson.getFirstName());
    assertEquals("Stallman", createdPerson.getLastName());
    assertEquals("Male", createdPerson.getGender());
  }

  @Test
  @Order(2)
	public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
    mockPerson();

    var content =
			given()
        .spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_TESTDEVJOAO)
            .body(person)
          .when()
            .post()
				.then()
          .statusCode(403)
            .extract()
              .body()
                .asString();

    assertNotNull(content);
    assertEquals("Invalid CORS request", content);
  }

  @Test
  @Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
    mockPerson();

    var content = given()
      .spec(specification)
      .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_DEVJOAO)
        .pathParam("id", person.getId())
        .when()
        .get("{id}")
			.then()
        .statusCode(200)
          .extract()
          .body()
            .asString();
		
    PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
    person = createdPerson;

    assertNotNull(createdPerson);

    assertNotNull(createdPerson.getAddress());
    assertNotNull(createdPerson.getFirstName());
    assertNotNull(createdPerson.getLastName());
    assertNotNull(createdPerson.getGender());
    assertNotNull(createdPerson.getId());

		assertTrue(createdPerson.getId() > 0);

    assertEquals("New York City, New York, US", createdPerson.getAddress());
    assertEquals("Richard", createdPerson.getFirstName());
    assertEquals("Stallman", createdPerson.getLastName());
    assertEquals("Male", createdPerson.getGender());
  }

  @Test
  @Order(4)
	public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
    mockPerson();

    var content =
			given()
        .spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_TESTDEVJOAO)
            .pathParam("id", person.getId())
          .when()
            .get("{id}")
				.then()
          .statusCode(403)
            .extract()
              .body()
                .asString();

    assertNotNull(content);
    assertEquals("Invalid CORS request", content);
  }

  private void mockPerson() {
    person.setFirstName("Richard");
    person.setLastName("Stallman");
    person.setAddress("New York City, New York, US");
    person.setGender("Male");
  }

}
