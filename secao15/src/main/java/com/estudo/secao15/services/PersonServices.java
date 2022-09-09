package com.estudo.secao15.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estudo.secao15.controllers.PersonController;
import com.estudo.secao15.data.vo.v1.PersonVO;
import com.estudo.secao15.exceptions.RequiredObjectIsNullException;
import com.estudo.secao15.exceptions.ResourceNotFoundException;
import com.estudo.secao15.mapper.DozerMapper;
import com.estudo.secao15.model.Person;
import com.estudo.secao15.repositories.PersonRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {
  
  private Logger logger = Logger.getLogger(PersonServices.class.getName());

  @Autowired
  PersonRepository repository;

  public List<PersonVO> findAll() {
    logger.info("Finding All people!");

    var persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
    
    persons
      .stream()
      .forEach(
        p -> p.add(
          linkTo(
            methodOn(PersonController.class).findById(p.getKey())
          ).withSelfRel()
        )
      );

    return persons;
  }

  public PersonVO create(PersonVO personVO) {

    if (personVO == null) throw new RequiredObjectIsNullException();

    logger.info("Creating an person");

    var entity = DozerMapper.parseObject(personVO, Person.class);
    var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
    
    vo.add(
      linkTo(
        methodOn(PersonController.class).findById(vo.getKey())
      ).withSelfRel()
    );

    return vo;
  }

  public PersonVO update(PersonVO personVO) {

    if (personVO == null) throw new RequiredObjectIsNullException();

    logger.info("Update an person");

    var entity = repository.findById(personVO.getKey()).orElseThrow(
      () -> new ResourceNotFoundException("No records found for this ID!")
    );

    entity.setFirstName(personVO.getFirstName());
    entity.setLastName(personVO.getLastName());
    entity.setAddress(personVO.getAddress());
    entity.setGender(personVO.getGender());

    var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
    
    vo.add(
      linkTo(
        methodOn(PersonController.class).findById(vo.getKey())
      ).withSelfRel()
    );

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
  
    var vo = DozerMapper.parseObject(entity, PersonVO.class);
    
    vo.add(
      linkTo(
        methodOn(PersonController.class).findById(id)
      ).withSelfRel()
    );

    return vo;
  }
  
}
