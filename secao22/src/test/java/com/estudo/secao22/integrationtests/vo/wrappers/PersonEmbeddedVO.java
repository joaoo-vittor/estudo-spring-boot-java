package com.estudo.secao22.integrationtests.vo.wrappers;

import java.io.Serializable;
import java.util.List;

import com.estudo.secao22.integrationtests.vo.PersonVO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonEmbeddedVO implements Serializable {
  
  private static final long serialVersionUID = 1L;

  @JsonProperty("persons")
  private List<PersonVO> person;

  public PersonEmbeddedVO() {}

  public List<PersonVO> getPerson() {
    return person;
  }

  public void setPerson(List<PersonVO> person) {
    this.person = person;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((person == null) ? 0 : person.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PersonEmbeddedVO other = (PersonEmbeddedVO) obj;
    if (person == null) {
      if (other.person != null)
        return false;
    } else if (!person.equals(other.person))
      return false;
    return true;
  }

}
