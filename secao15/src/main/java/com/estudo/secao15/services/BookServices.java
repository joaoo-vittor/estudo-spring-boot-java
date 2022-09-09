package com.estudo.secao15.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.estudo.secao15.controllers.BookController;
import com.estudo.secao15.data.vo.v1.BookVO;
import com.estudo.secao15.exceptions.RequiredObjectIsNullException;
import com.estudo.secao15.exceptions.ResourceNotFoundException;
import com.estudo.secao15.mapper.DozerMapper;
import com.estudo.secao15.model.Books;
import com.estudo.secao15.repositories.BookRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

  private Logger logger = Logger.getLogger(BookServices.class.getName());

  @Autowired
  BookRepository repository;

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

  public List<BookVO> findAll() {
    logger.info("Finding All Books");

    var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);

    books 
      .stream()
      .forEach(
        p -> p.add(
          linkTo(
            methodOn(BookController.class).findById(p.getKey())
          ).withSelfRel()
        )
      );

    return books;
  }

  public BookVO create(BookVO bookVO) {

    if (bookVO == null) throw new RequiredObjectIsNullException();

    logger.info("Creating an book");

    var entity = DozerMapper.parseObject(bookVO, Books.class);
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
    logger.info("Delete an Book");

    var entity = repository.findById(id).orElseThrow(
      () -> new ResourceAccessException("No records found for this ID!")
    );

    repository.delete(entity);
  }

}
