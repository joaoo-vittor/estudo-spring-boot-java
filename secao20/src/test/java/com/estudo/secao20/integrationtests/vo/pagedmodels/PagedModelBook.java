package com.estudo.secao20.integrationtests.vo.pagedmodels;

import java.util.List;

import com.estudo.secao20.integrationtests.vo.BookVO;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelBook {
  
  @XmlElement(name = "content")
  private List<BookVO> content;

  public PagedModelBook() {}

  public List<BookVO> getContent() {
    return content;
  }

  public void setContent(List<BookVO> content) {
    this.content = content;
  }

}
