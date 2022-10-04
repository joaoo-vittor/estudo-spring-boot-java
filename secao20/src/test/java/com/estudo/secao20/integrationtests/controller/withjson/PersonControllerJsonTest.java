package com.estudo.secao20.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import com.estudo.secao20.configs.TestConfigs;
import com.estudo.secao20.integrationtests.testcontainers.AbstractIntegrationTest;
import com.estudo.secao20.integrationtests.vo.AccountCredentialsVO;
import com.estudo.secao20.integrationtests.vo.PersonVO;
import com.estudo.secao20.integrationtests.vo.TokenVO;
import com.estudo.secao20.integrationtests.vo.wrappers.WrapperPersonVO;
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
public class PersonControllerJsonTest extends AbstractIntegrationTest {
  
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
    assertTrue(createdPerson.getEnabled());

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
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
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
    assertTrue(createdPerson.getEnabled());

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
    assertTrue(createdPerson.getEnabled());

		assertTrue(createdPerson.getId() > 0);

    assertEquals("England, UK", createdPerson.getAddress());
    assertEquals("Allan", createdPerson.getFirstName());
    assertEquals("Turing", createdPerson.getLastName());
    assertEquals("Male", createdPerson.getGender());
  }

  @Test
  @Order(4)
	public void testDisabledPersonById() throws JsonMappingException, JsonProcessingException {

    var content = given()
      .spec(specification)
      .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_DEVJOAO)
        .pathParam("id", person.getId())
        .when()
        .patch("{id}")
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
    assertFalse(createdPerson.getEnabled());

		assertTrue(createdPerson.getId() > 0);

    assertEquals("England, UK", createdPerson.getAddress());
    assertEquals("Allan", createdPerson.getFirstName());
    assertEquals("Turing", createdPerson.getLastName());
    assertEquals("Male", createdPerson.getGender());
  }

  @Test
  @Order(5)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
    given()
      .spec(specification)
      .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .pathParam("id", person.getId())
        .when()
        .delete("{id}")
			.then()
        .statusCode(204);
  }

  @Test
  @Order(6)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

    var content =
			given()
        .spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .queryParams(
          "page",
          1, 
          "size", 
          4,
          "direction",
          "asc"
        )
          .when()
            .get()
				.then()
          .statusCode(200)
            .extract()
              .body()
                .asString();
		
    WrapperPersonVO wrapper = objectMapper.readValue(
      content, 
      WrapperPersonVO.class
    );

    var people = wrapper.getEmbedded().getPerson();

    PersonVO foundPersonOne = people.get(0);

    assertNotNull(foundPersonOne);

    assertNotNull(foundPersonOne.getAddress());
    assertNotNull(foundPersonOne.getFirstName());
    assertNotNull(foundPersonOne.getLastName());
    assertNotNull(foundPersonOne.getGender());
    assertNotNull(foundPersonOne.getId());
    assertFalse(foundPersonOne.getEnabled());

		assertEquals(986, foundPersonOne.getId());

    assertEquals("56 Bellgrove Lane", foundPersonOne.getAddress());
    assertEquals("Abby", foundPersonOne.getFirstName());
    assertEquals("Romagnosi", foundPersonOne.getLastName());
    assertEquals("Female", foundPersonOne.getGender());
  }

  @Test
  @Order(7)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

    RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
      .setBasePath("/api/v1/person")
      .setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
      .build();

    given()
      .spec(specificationWithoutToken)
      .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .when()
          .get()
      .then()
        .statusCode(403);
		
  }

  @Test
  @Order(8)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {

    var content =
			given()
        .spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .pathParam("firstName", "dway")
        .queryParams(
          "page",
          0, 
          "size", 
          6,
          "direction",
          "asc"
        )
          .when()
            .get("findPersonByName/{firstName}")
				.then()
          .statusCode(200)
            .extract()
              .body()
                .asString();
		
    WrapperPersonVO wrapper = objectMapper.readValue(
      content, 
      WrapperPersonVO.class
    );

    var people = wrapper.getEmbedded().getPerson();

    PersonVO foundPersonOne = people.get(0);

    assertNotNull(foundPersonOne);

    assertNotNull(foundPersonOne.getAddress());
    assertNotNull(foundPersonOne.getFirstName());
    assertNotNull(foundPersonOne.getLastName());
    assertNotNull(foundPersonOne.getGender());
    assertNotNull(foundPersonOne.getId());
    assertFalse(foundPersonOne.getEnabled());

		assertEquals(603, foundPersonOne.getId());

    assertEquals("96 Clemons Court", foundPersonOne.getAddress());
    assertEquals("Dwayne", foundPersonOne.getFirstName());
    assertEquals("Leming", foundPersonOne.getLastName());
    assertEquals("Male", foundPersonOne.getGender());
  }

  private void mockPerson() {
    person.setFirstName("Allan");
    person.setLastName("Turin");
    person.setAddress("England, UK");
    person.setGender("Male");
    person.setEnabled(true);
  }
}
