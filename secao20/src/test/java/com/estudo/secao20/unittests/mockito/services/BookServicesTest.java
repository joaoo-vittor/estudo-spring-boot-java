package com.estudo.secao20.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.estudo.secao20.data.vo.v1.BookVO;
import com.estudo.secao20.exceptions.RequiredObjectIsNullException;
import com.estudo.secao20.model.Book;
import com.estudo.secao20.repositories.BookRepository;
import com.estudo.secao20.services.BookServices;
import com.estudo.secao20.unittests.mapper.mocks.MockBook;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServicesTest {

  MockBook input;

  @BeforeEach
  void setUpMocks() throws Exception {
    input = new MockBook();
    MockitoAnnotations.openMocks(this);
  }

  @InjectMocks
  private BookServices services;

  @Mock
  BookRepository repository;
  
  @Test
  @DisplayName("Should PersonService find person by id with success")
  void testFindById() {
    Book entity = input.mockEntity(1);
    entity.setId(1L);

    when(repository.findById(1L)).thenReturn(Optional.of(entity));

    var result = services.findById(1L);
    assertNotNull(result);
    assertNotNull(result.getKey());
    assertNotNull(result.getLinks());
    assertTrue(result.toString().contains("links: [</api/v1/book/1>;rel=\"self\"]"));
    
    assertEquals("Author Test1", result.getAuthor());
    assertEquals("Title Test1", result.getTitle());
    assertEquals(25D, result.getPrice());
    assertNotNull(result.getLaunchDate());
  }

  @Test
  @DisplayName("Should PersonService create person with success")
  void testCreate() {
    Book entity = input.mockEntity(1);
    Book persistend = entity;
    persistend.setId(1L);

    BookVO vo = input.mockVO(1);
    vo.setKey(1L);

    when(repository.save(entity)).thenReturn(persistend);

    var result = services.create(vo);
    
    assertNotNull(result);
    assertNotNull(result.getKey());
    assertNotNull(result.getLinks());
    assertTrue(result.toString().contains("links: [</api/v1/book/1>;rel=\"self\"]"));
    
    assertEquals("Author Test1", result.getAuthor());
    assertEquals("Title Test1", result.getTitle());
    assertEquals(25D, result.getPrice());
    assertNotNull(result.getLaunchDate());
  }

  @Test
  @DisplayName("Should throws if pass null to create")
  void testCreateWithNullPerson() {
    Exception exception = assertThrows(
      RequiredObjectIsNullException.class, 
      () -> {
        services.create(null);
      }
    );

    String expectedMessage = "It is not allowed to persist a null object!";
    String actualMessage = exception.getMessage();

    assertTrue(expectedMessage.contains(actualMessage));
  }

  @Test
  @DisplayName("Should throws if pass null to update")
  void testUpdateWithNullPerson() {
    Exception exception = assertThrows(
      RequiredObjectIsNullException.class, 
      () -> {
        services.update(null);
      }
    );

    String expectedMessage = "It is not allowed to persist a null object!";
    String actualMessage = exception.getMessage();

    assertTrue(expectedMessage.contains(actualMessage));
  }

  @Test
  @DisplayName("Should PersonService update person with success")
  void testUpdate() {
    Book entity = input.mockEntity(1);
    entity.setId(1L);

    Book persistend = entity;
    persistend.setId(1L);

    BookVO vo = input.mockVO(1);
    vo.setKey(1L);

    when(repository.findById(1L)).thenReturn(Optional.of(entity));
    when(repository.save(entity)).thenReturn(persistend);

    var result = services.update(vo);

    assertNotNull(result);
    assertNotNull(result.getKey());
    assertNotNull(result.getLinks());
    assertTrue(result.toString().contains("links: [</api/v1/book/1>;rel=\"self\"]"));
    
    assertEquals("Author Test1", result.getAuthor());
    assertEquals("Title Test1", result.getTitle());
    assertEquals(25D, result.getPrice());
    assertNotNull(result.getLaunchDate());
  }

  @Test
  void testDelete() {
    Book entity = input.mockEntity(1);
    entity.setId(1L);

    when(repository.findById(1L)).thenReturn(Optional.of(entity));

    services.delete(1L);
  }
}
