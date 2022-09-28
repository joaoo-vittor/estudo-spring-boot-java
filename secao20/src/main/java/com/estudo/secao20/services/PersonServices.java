package com.estudo.secao20.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.estudo.secao20.controllers.PersonController;
import com.estudo.secao20.data.vo.v1.PersonVO;
import com.estudo.secao20.exceptions.RequiredObjectIsNullException;
import com.estudo.secao20.exceptions.ResourceNotFoundException;
import com.estudo.secao20.mapper.DozerMapper;
import com.estudo.secao20.model.Person;
import com.estudo.secao20.repositories.PersonRepository;

import jakarta.transaction.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {
  
  private Logger logger = Logger.getLogger(PersonServices.class.getName());

  @Autowired
  PersonRepository repository;

  @Autowired
  PagedResourcesAssembler<PersonVO> assembler;

  public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
    logger.info("Finding All people!");

    var personPage = repository.findAll(pageable);

    var personVosPage = personPage.map(
      p -> DozerMapper.parseObject(p, PersonVO.class)
    );

    personVosPage
      .map(
        p -> 
          p.add(
            linkTo(
              methodOn(PersonController.class).findById(p.getKey())
            ).withSelfRel()
          )
      );

    Link link = linkTo(
      methodOn(PersonController.class).findAll(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        "asc"
      )
    ).withSelfRel();
    return assembler.toModel(personVosPage, link);
  }

  public PagedModel<EntityModel<PersonVO>> findPersonsByName(String firstName, Pageable pageable) {
    logger.info("Finding All people!");

    var personPage = repository.findPersonsByName(firstName, pageable);

    var personVosPage = personPage.map(
      p -> DozerMapper.parseObject(p, PersonVO.class)
    );

    personVosPage
      .map(
        p -> 
          p.add(
            linkTo(
              methodOn(PersonController.class).findById(p.getKey())
            ).withSelfRel()
          )
      );

    Link link = linkTo(
      methodOn(PersonController.class).findAll(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        "asc"
      )
    ).withSelfRel();
  
    return assembler.toModel(personVosPage, link);
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

  @Transactional // ACID (database) - de ser empregado!
  public PersonVO disablePerson(Long id) {
    logger.info("Disabling one person by ID " + id);
    
    repository.disablePerson(id);

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
