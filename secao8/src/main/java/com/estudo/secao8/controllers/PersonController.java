package com.estudo.secao8.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estudo.secao8.model.Person;
import com.estudo.secao8.services.PersonServices;

@RestController
@RequestMapping("/person")
public class PersonController {

  // O spring cuida da instaciacao e injecao de dependencia
  @Autowired 
  private PersonServices services;
  // private PersonServices services = new PersonServices();

  @GetMapping(
    value = "/{id}", 
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Person findById(@PathVariable(value = "id") Long id) {
    return services.findById(id);
  }

  // @RequestMapping
  @GetMapping(
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public List<Person> findAll() {
    return services.findAll();
  }

  // @RequestMapping
  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Person create(@RequestBody Person person) {
    return services.create(person);
  }

  @PutMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Person update(@RequestBody Person person) {
    return services.create(person);
  }

  @DeleteMapping(
    value = "/{id}" 
  )
  public ResponseEntity<?> deleteById(@PathVariable(value = "id") Long id) {
    services.delete(id);
    return ResponseEntity.noContent().build();
  }

}
