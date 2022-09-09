package com.estudo.secao8.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estudo.secao8.exceptions.ResourceNotFoundException;
import com.estudo.secao8.model.Person;
import com.estudo.secao8.repositories.PersonRepository;

@Service
public class PersonServices {
  
  private Logger logger = Logger.getLogger(PersonServices.class.getName());

  @Autowired
  PersonRepository repository;

  public List<Person> findAll() {
    
    logger.info("Finding All people!");

    return repository.findAll();
  }

  public Person create(Person person) {
    logger.info("Creating an person");
    return repository.save(person);
  }

  public Person update(Person person) {
    logger.info("Update an person");

    var entity = repository.findById(person.getId()).orElseThrow(
      () -> new ResourceNotFoundException("No records found for this ID!")
    );

    entity.setFirstName(person.getFirstName());
    entity.setLastName(person.getLastName());
    entity.setAddress(person.getAddress());
    entity.setGender(person.getGender());

    return repository.save(person);
  }

  public void delete(Long id) {
    logger.info("Delete an person");
    var entity = repository.findById(id).orElseThrow(
      () -> new ResourceNotFoundException("No records found for this ID!")
    );

    repository.delete(entity);
  }

  public Person findById(Long id) {
    
    logger.info("Finding one person!");

    // Person person = new Person();
    
    // person.setId(this.counter.incrementAndGet());
    // person.setFirstName("Joao");
    // person.setLastName("Silva");
    // person.setAddress("Campina grande - Paraiba - Brasil");
    // person.setGender("Male");

    return repository.findById(id).orElseThrow(
      () -> new ResourceNotFoundException("No records found for this ID!")
    );
  }

  // private Person mockPerson(int i) {
  //   Person person = new Person();
    
  //   // person.setId(this.counter.incrementAndGet());
  //   person.setFirstName("Person Name" + i);
  //   person.setLastName("Person Last Name" + i);
  //   person.setAddress("Address" + i);
  //   person.setGender(i % 2 == 0 ? "Male" : "Female");

  //   return person;
  // }
  
}
