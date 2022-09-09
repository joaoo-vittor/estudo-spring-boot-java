package com.estudo.secao9.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estudo.secao9.data.vo.v1.PersonVO;
import com.estudo.secao9.exceptions.ResourceNotFoundException;
import com.estudo.secao9.mapper.DozerMapper;
import com.estudo.secao9.model.Person;
import com.estudo.secao9.repositories.PersonRepository;

@Service
public class PersonServices {
  
  private Logger logger = Logger.getLogger(PersonServices.class.getName());

  @Autowired
  PersonRepository repository;

  public List<PersonVO> findAll() {
    logger.info("Finding All people!");

    return DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
  }

  public PersonVO create(PersonVO personVO) {
    logger.info("Creating an person");

    var entity = DozerMapper.parseObject(personVO, Person.class);
    var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
    
    return vo;
  }

  public PersonVO update(PersonVO personVO) {
    logger.info("Update an person");

    var entity = repository.findById(personVO.getId()).orElseThrow(
      () -> new ResourceNotFoundException("No records found for this ID!")
    );

    entity.setFirstName(personVO.getFirstName());
    entity.setLastName(personVO.getLastName());
    entity.setAddress(personVO.getAddress());
    entity.setGender(personVO.getGender());

    var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
    
    return vo;
  }

  public void delete(Long id) {
    logger.info("Delete an person");

    var entity = repository.findById(id).orElseThrow(
      () -> new ResourceNotFoundException("No records found for this ID!")
    );

    repository.delete(entity);
  }

  public PersonVO findById(Long id) {
    logger.info("Finding one person!");

    var entity = repository.findById(id).orElseThrow(
      () -> new ResourceNotFoundException("No records found for this ID!")
    );
  
    return DozerMapper.parseObject(entity, PersonVO.class);
  }
  
}
