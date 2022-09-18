package com.estudo.secao18.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import com.estudo.secao18.configs.TestConfigs;
import com.estudo.secao18.data.vo.v1.security.TokenVO;
import com.estudo.secao18.integrationtests.testcontainers.AbstractIntegrationTest;
import com.estudo.secao18.integrationtests.vo.AccountCredentialsVO;
import com.estudo.secao18.integrationtests.vo.PersonVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {
  
  private static RequestSpecification specification;
  private static XmlMapper objectMapper;

  private static PersonVO person;

  @BeforeAll
  public static void setup() {
    objectMapper = new XmlMapper();
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
          .contentType(TestConfigs.CONTENT_TYPE_XML)
          .accept(TestConfigs.CONTENT_TYPE_XML)
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
			given().spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_XML)
        .accept(TestConfigs.CONTENT_TYPE_XML)
          .body(person)
          .when()
          .post()
				.then()
          .statusCode(200)
            .extract()
            .body()
              .asString();

    System.out.println(content.toString());
		
    PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
    person = createdPerson;

    assertNotNull(createdPerson);

    assertNotNull(createdPerson.getAddress());
    assertNotNull(createdPerson.getFirstName());
    assertNotNull(createdPerson.getLastName());
    assertNotNull(createdPerson.getGender());
    assertNotNull(createdPerson.getId());

		assertTrue(createdPerson.getId() > 0);

    assertEquals("England, UK", createdPerson.getAddress());
    assertEquals("Allan", createdPerson.getFirstName());
    assertEquals("Turin", createdPerson.getLastName());
    assertEquals("Male", createdPerson.getGender());
  }
  
  @Test
  @Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
    person.setLastName("Turing");

    var content =
			given()
        .spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_XML)
        .accept(TestConfigs.CONTENT_TYPE_XML)
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

		assertEquals(person.getId(), createdPerson.getId());

    assertEquals("England, UK", createdPerson.getAddress());
    assertEquals("Allan", createdPerson.getFirstName());
    assertEquals("Turing", createdPerson.getLastName());
    assertEquals("Male", createdPerson.getGender());
  }

  @Test
  @Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
    mockPerson();

    var content = given()
      .spec(specification)
      .contentType(TestConfigs.CONTENT_TYPE_XML)
      .accept(TestConfigs.CONTENT_TYPE_XML)
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

    assertEquals("England, UK", createdPerson.getAddress());
    assertEquals("Allan", createdPerson.getFirstName());
    assertEquals("Turing", createdPerson.getLastName());
    assertEquals("Male", createdPerson.getGender());
  }

  @Test
  @Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
    given()
      .spec(specification)
      .contentType(TestConfigs.CONTENT_TYPE_XML)
      .accept(TestConfigs.CONTENT_TYPE_XML)
        .pathParam("id", person.getId())
        .when()
        .delete("{id}")
			.then()
        .statusCode(204);
  }

  @Test
  @Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

    var content =
			given()
        .spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_XML)
        .accept(TestConfigs.CONTENT_TYPE_XML)
          .when()
            .get()
				.then()
          .statusCode(200)
            .extract()
              .body()
                .asString();
		
    List<PersonVO> people = objectMapper.readValue(
      content, 
      new TypeReference<List<PersonVO>>() {}
    );

    PersonVO foundPersonOne = people.get(0);

    assertNotNull(foundPersonOne);

    assertNotNull(foundPersonOne.getAddress());
    assertNotNull(foundPersonOne.getFirstName());
    assertNotNull(foundPersonOne.getLastName());
    assertNotNull(foundPersonOne.getGender());
    assertNotNull(foundPersonOne.getId());

		assertEquals(1, foundPersonOne.getId());

    assertEquals("Campina grande", foundPersonOne.getAddress());
    assertEquals("joao vitor", foundPersonOne.getFirstName());
    assertEquals("silva", foundPersonOne.getLastName());
    assertEquals("Male", foundPersonOne.getGender());
  }

  @Test
  @Order(6)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

    RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
      .setBasePath("/api/v1/person")
      .setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
      .build();

    given()
      .spec(specificationWithoutToken)
      .contentType(TestConfigs.CONTENT_TYPE_XML)
      .accept(TestConfigs.CONTENT_TYPE_XML)
        .when()
          .get()
      .then()
        .statusCode(403);
		
  }

  private void mockPerson() {
    person.setFirstName("Allan");
    person.setLastName("Turin");
    person.setAddress("England, UK");
    person.setGender("Male");
  }
}
