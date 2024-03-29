package com.estudo.secao22.integrationtests.controller.withjson;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import com.estudo.secao22.configs.TestConfigs;
import com.estudo.secao22.data.vo.v1.security.TokenVO;
import com.estudo.secao22.integrationtests.testcontainers.AbstractIntegrationTest;
import com.estudo.secao22.integrationtests.vo.AccountCredentialsVO;
import com.estudo.secao22.integrationtests.vo.BookVO;
import com.estudo.secao22.integrationtests.vo.wrappers.WrapperBookVO;
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
public class BookControllerJsonTest extends AbstractIntegrationTest {
  
  private static RequestSpecification specification;
  private static ObjectMapper objectMapper;

  private static BookVO book;

  @BeforeAll
  public static void setup() {
    objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    book = new BookVO();
  }

  @Test
  @Order(1)
  public void testAuthorization() {
    AccountCredentialsVO user = new AccountCredentialsVO(
      "joao", "admin234"
    );

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
      .setBasePath("/api/v1/book")
      .setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
      .build();
  }

  @Test
  @Order(2)
  public void testCreate() throws JsonMappingException, JsonProcessingException {
    mockBook();

    var content = given().spec(specification)
      .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .body(book)
        .when()
        .post()
      .then()
        .statusCode(200)
          .extract()
          .body()
            .asString();

    book = objectMapper.readValue(content, BookVO.class);

    assertNotNull(book.getId());
    assertNotNull(book.getTitle());
    assertNotNull(book.getAuthor());
    assertNotNull(book.getPrice());
    assertTrue(book.getId() > 0);
    assertEquals("Docker Deep Dive", book.getTitle());
    assertEquals("Nigel Poulton", book.getAuthor());
    assertEquals(55.99, book.getPrice());
  }

  @Test
  @Order(3)
  public void testUpdate() throws JsonMappingException, JsonProcessingException {
    book.setTitle("Docker Deep Dive - Updated");

    var content = given().spec(specification)
      .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .body(book)
        .when()
        .put()
      .then()
        .statusCode(200)
          .extract()
          .body()
            .asString();

    BookVO bookUpdated = objectMapper.readValue(content, BookVO.class);

    assertNotNull(bookUpdated.getId());
    assertNotNull(bookUpdated.getTitle());
    assertNotNull(bookUpdated.getAuthor());
    assertNotNull(bookUpdated.getPrice());
    assertEquals(bookUpdated.getId(), book.getId());
    assertEquals("Docker Deep Dive - Updated", bookUpdated.getTitle());
    assertEquals("Nigel Poulton", bookUpdated.getAuthor());
    assertEquals(55.99, bookUpdated.getPrice());
  }

  @Test
  @Order(4)
  public void testFindById() throws JsonMappingException, JsonProcessingException {
    var content = given().spec(specification)
      .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .pathParam("id", book.getId())
      .when()
        .get("{id}")
      .then()
        .statusCode(200)
          .extract()
          .body()
            .asString();

    BookVO foundBook = objectMapper.readValue(content, BookVO.class);

    assertNotNull(foundBook.getId());
    assertNotNull(foundBook.getTitle());
    assertNotNull(foundBook.getAuthor());
    assertNotNull(foundBook.getPrice());
    assertEquals(foundBook.getId(), book.getId());
    assertEquals("Docker Deep Dive - Updated", foundBook.getTitle());
    assertEquals("Nigel Poulton", foundBook.getAuthor());
    assertEquals(55.99, foundBook.getPrice());
  }

  @Test
  @Order(5)
  public void testDelete() throws JsonMappingException, JsonProcessingException {
    given()
      .spec(specification)
      .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .pathParam("id", book.getId())
      .when()
        .delete("{id}")
      .then()
        .statusCode(204);
  }

  @Test
  @Order(6)
  public void testFindAll() throws JsonMappingException, JsonProcessingException {
    
    var content = given().spec(specification)
      .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .when()
        .get()
      .then()
        .statusCode(200)
          .extract()
          .body()
            .asString();

    WrapperBookVO wrapper = objectMapper.readValue(
      content, 
      WrapperBookVO.class
    );
    List<BookVO> books = wrapper.getEmbedded().getBooks();

    BookVO foundBookOne = books.get(0);

    assertNotNull(foundBookOne.getId());
    assertNotNull(foundBookOne.getTitle());
    assertNotNull(foundBookOne.getAuthor());
    assertNotNull(foundBookOne.getPrice());
    assertEquals(12, foundBookOne.getId());
    assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", foundBookOne.getTitle());
    assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", foundBookOne.getAuthor());
    assertEquals(54.00, foundBookOne.getPrice());
  }

  private void mockBook() {
    book.setTitle("Docker Deep Dive");
    book.setAuthor("Nigel Poulton");
    book.setPrice(Double.valueOf(55.99));
    book.setLaunchDate(new Date());
  }
}
