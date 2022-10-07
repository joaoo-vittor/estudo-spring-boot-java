package com.estudo.secao22.integrationtests.vo.pagedmodels;

import java.io.Serializable;
import java.util.List;

import com.estudo.secao22.integrationtests.vo.PersonVO;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelPerson implements Serializable  {

  @XmlElement(name = "content")
  private List<PersonVO> content;

  public PagedModelPerson() {}

  public List<PersonVO> getContent() {
    return content;
  }

  public void setContent(List<PersonVO> content) {
    this.content = content;
  }

}
