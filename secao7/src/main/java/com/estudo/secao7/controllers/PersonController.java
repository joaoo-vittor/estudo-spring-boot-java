package com.estudo.secao7.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.estudo.secao7.model.Person;
import com.estudo.secao7.services.PersonServices;

@RestController
@RequestMapping("/person")
public class PersonController {

  // O spring cuida da instaciacao e injecao de dependencia
  @Autowired 
  private PersonServices services;
  // private PersonServices services = new PersonServices();

  @RequestMapping(
    value = "/{id}", 
    method=RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Person findById(@PathVariable(value = "id") String id) {
    return services.findById(id);
  }

  @RequestMapping( 
    method=RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public List<Person> findAll() {
    return services.findAll();
  }

  @RequestMapping(
    method=RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Person create(@RequestBody Person person) {
    return services.create(person);
  }

  @RequestMapping(
    method=RequestMethod.PUT,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Person update(@RequestBody Person person) {
    return services.create(person);
  }

  @RequestMapping(
    value = "/{id}", 
    method=RequestMethod.DELETE
  )
  public void deleteById(@PathVariable(value = "id") String id) {
    services.delete(id);
  }

}
