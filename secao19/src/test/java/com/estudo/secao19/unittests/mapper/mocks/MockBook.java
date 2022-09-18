package com.estudo.secao19.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.estudo.secao19.data.vo.v1.BookVO;
import com.estudo.secao19.model.Book;

public class MockBook {
  
  public Book mockEntity() {
    return mockEntity(0);
  }

  public BookVO mockVO() {
    return mockVO(0);
  }

  public List<Book> mockEntityList() {
    List<Book> books = new ArrayList<>();
    for (int i = 0; i < 14; i++) {
      books.add(mockEntity(i));
    }
    return books;
  }

  public List<BookVO> mockVOList() {
    List<BookVO> bookVO = new ArrayList<>();
    for (int i = 0; i < 14; i++) {
      bookVO.add(mockVO(i));
    }
    return bookVO;
  }

  public Book mockEntity(Integer number) {
    Book book = new Book();
    
    book.setAuthor("Author Test" + number);
    book.setLaunchDate(new Date());
    book.setPrice(25D);
    book.setId(number.longValue());
    book.setTitle("Title Test" + number);

    return book;
  }

  public BookVO mockVO(Integer number) {
    BookVO book = new BookVO();
    
    book.setAuthor("Author Test" + number);
    book.setLaunchDate(new Date());
    book.setPrice(25D);
    book.setKey(number.longValue());
    book.setTitle("Title Test" + number);

    return book;
  }

}
