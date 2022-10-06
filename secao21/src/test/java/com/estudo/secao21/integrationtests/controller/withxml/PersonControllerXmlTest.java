package com.estudo.secao21.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import com.estudo.secao21.configs.TestConfigs;
import com.estudo.secao21.data.vo.v1.security.TokenVO;
import com.estudo.secao21.integrationtests.testcontainers.AbstractIntegrationTest;
import com.estudo.secao21.integrationtests.vo.AccountCredentialsVO;
import com.estudo.secao21.integrationtests.vo.PersonVO;
import com.estudo.secao21.integrationtests.vo.pagedmodels.PagedModelPerson;
import com.fasterxml.jackson.core.JsonProcessingException;
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
		
    PagedModelPerson wrapper = objectMapper.readValue(
      content, 
      PagedModelPerson.class
    );

    var people = wrapper.getContent();
    PersonVO foundPersonOne = people.get(0);

    assertNotNull(foundPersonOne);

    assertNotNull(foundPersonOne.getAddress());
    assertNotNull(foundPersonOne.getFirstName());
    assertNotNull(foundPersonOne.getLastName());
    assertNotNull(foundPersonOne.getGender());
    assertNotNull(foundPersonOne.getId());

		assertEquals(986, foundPersonOne.getId());

    assertEquals("56 Bellgrove Lane", foundPersonOne.getAddress());
    assertEquals("Abby", foundPersonOne.getFirstName());
    assertEquals("Romagnosi", foundPersonOne.getLastName());
    assertEquals("Female", foundPersonOne.getGender());
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

  @Test
  @Order(7)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {

    var content =
			given()
        .spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_XML)
        .accept(TestConfigs.CONTENT_TYPE_XML)
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
		
    PagedModelPerson wrapper = objectMapper.readValue(
      content, 
      PagedModelPerson.class
    );

    var people = wrapper.getContent();
    PersonVO foundPersonOne = people.get(0);

    assertNotNull(foundPersonOne);

    assertNotNull(foundPersonOne.getAddress());
    assertNotNull(foundPersonOne.getFirstName());
    assertNotNull(foundPersonOne.getLastName());
    assertNotNull(foundPersonOne.getGender());
    assertNotNull(foundPersonOne.getId());

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
    person.setEnabled(false);
  }
}
