package com.estudo.secao21.integrationtests.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.estudo.secao21.integrationtests.testcontainers.AbstractIntegrationTest;
import com.estudo.secao21.model.Person;
import com.estudo.secao21.repositories.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {

  @Autowired
  public PersonRepository repository;

  private static Person person;

  @BeforeAll
  public static void setup() {
    person = new Person();
  }

  @Test
  @Order(0)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {

    Pageable pageable = PageRequest.of(
      0, 
      6, 
      Sort.by(
        Direction.ASC, 
        "firstName"
      )
    );

    person = repository.findPersonsByName("job", pageable)
      .getContent()
      .get(0);

    assertNotNull(person);

    assertNotNull(person.getAddress());
    assertNotNull(person.getFirstName());
    assertNotNull(person.getLastName());
    assertNotNull(person.getGender());
    assertNotNull(person.getId());
    assertTrue(person.getEnabled());

		assertEquals(508, person.getId());

    assertEquals("9 Derek Center", person.getAddress());
    assertEquals("Jobey", person.getFirstName());
    assertEquals("Risom", person.getLastName());
    assertEquals("Female", person.getGender());
  }

  @Test
  @Order(1)
	public void testDisablePerson() throws JsonMappingException, JsonProcessingException {

    repository.disablePerson(person.getId());

    Pageable pageable = PageRequest.of(
      0, 
      6, 
      Sort.by(
        Direction.ASC, 
        "firstName"
      )
    );

    person = repository.findPersonsByName("job", pageable)
      .getContent()
      .get(0);

    assertNotNull(person);

    assertNotNull(person.getAddress());
    assertNotNull(person.getFirstName());
    assertNotNull(person.getLastName());
    assertNotNull(person.getGender());
    assertNotNull(person.getId());
    
    assertFalse(person.getEnabled());

		assertEquals(508, person.getId());

    assertEquals("9 Derek Center", person.getAddress());
    assertEquals("Jobey", person.getFirstName());
    assertEquals("Risom", person.getLastName());
    assertEquals("Female", person.getGender());
  }

}
