package com.estudo.secao22.integrationtests.vo.wrappers;

import java.io.Serializable;
import java.util.List;

import com.estudo.secao22.integrationtests.vo.BookVO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookEmbeddedVO implements Serializable {
  
  private static final long serialVersionUID = 1L;

  @JsonProperty("books")
  private List<BookVO> books;

  public BookEmbeddedVO() {}

  public List<BookVO> getBooks() {
    return books;
  }

  public void setBooks(List<BookVO> books) {
    this.books = books;
  }

}
