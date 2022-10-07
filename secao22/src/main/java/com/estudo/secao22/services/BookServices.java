package com.estudo.secao22.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.estudo.secao22.controllers.BookController;
import com.estudo.secao22.data.vo.v1.BookVO;
import com.estudo.secao22.exceptions.RequiredObjectIsNullException;
import com.estudo.secao22.exceptions.ResourceNotFoundException;
import com.estudo.secao22.mapper.DozerMapper;
import com.estudo.secao22.model.Book;
import com.estudo.secao22.repositories.BookRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

  private Logger logger = Logger.getLogger(BookServices.class.getName());

  @Autowired
  BookRepository repository;

  @Autowired
  PagedResourcesAssembler<BookVO> assembler;

  public BookVO findById(Long id) {
    logger.info("Finding one Book");

    var entity = repository.findById(id).orElseThrow(
      () -> new ResourceNotFoundException("No records found for this ID!")
    );

    var vo = DozerMapper.parseObject(entity, BookVO.class);

    vo.add(
      linkTo(
        methodOn(BookController.class).findById(id)
      ).withSelfRel()
    );

    return vo;
  }

  public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {
    logger.info("Finding All Books");

    var bookPage = repository.findAll(pageable);
    
    var bookVosPage = bookPage.map(
      b -> DozerMapper.parseObject(b, BookVO.class)
    );

    bookVosPage
      .map(
        b -> 
          b.add(
            linkTo(
              methodOn(BookController.class).findById(b.getKey())
            ).withSelfRel()
          )
      );
    
    Link link = linkTo(
      methodOn(BookController.class).findAll(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        "asc"  
      )
    ).withSelfRel();

    return assembler.toModel(bookVosPage, link);
  }

  public BookVO create(BookVO bookVO) {

    if (bookVO == null) throw new RequiredObjectIsNullException();

    logger.info("Creating an book");

    var entity = DozerMapper.parseObject(bookVO, Book.class);
    var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);

    vo.add(
      linkTo(
        methodOn(BookController.class).findById(vo.getKey())
      ).withSelfRel()
    );

    return vo;
  }

  public BookVO update(BookVO bookVO) {
    
    if (bookVO == null) throw new RequiredObjectIsNullException();

    logger.info("Update an person");

    var entity = repository.findById(bookVO.getKey()).orElseThrow(
      () -> new ResourceNotFoundException("No records found for this ID!")
    );

    entity.setAuthor(bookVO.getAuthor());
    entity.setLaunchDate(bookVO.getLaunchDate());
    entity.setPrice(bookVO.getPrice());
    entity.setTitle(bookVO.getTitle());

    var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);

    vo.add(
      linkTo(
        methodOn(BookController.class).findById(vo.getKey())
      ).withSelfRel()
    );

    return vo;
  }

  public void delete(Long id) {
    logger.info("Delete one Book with ID: " + id);

    var entity = repository.findById(id).orElseThrow(
      () -> new ResourceAccessException("No records found for this ID!")
    );

    repository.delete(entity);
  }

}
