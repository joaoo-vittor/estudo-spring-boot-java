package com.estudo.secao7.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.estudo.secao7.model.Person;

@Service
public class PersonServices {
  
  private final AtomicLong counter = new AtomicLong();
  private Logger logger = Logger.getLogger(PersonServices.class.getName());

  public List<Person> findAll() {
    List<Person> persons = new ArrayList<>();
    
    for (int i = 0; i < 8; i++) {
      Person person = mockPerson(i);
      persons.add(person);
    }
    
    logger.info("Finding All people!");

    return persons;
  }

  public Person create(Person person) {
    logger.info("Creating an person");
    return person;
  }

  public Person update(Person person) {
    logger.info("Update an person");
    return person;
  }

  public void delete(String id) {
    logger.info("Delete an person");
  }

  public Person findById(String id) {
    
    logger.info("Finding one person!");

    Person person = new Person();
    
    person.setId(this.counter.incrementAndGet());
    person.setFirstName("Joao");
    person.setLastName("Silva");
    person.setAddress("Campina grande - Paraiba - Brasil");
    person.setGender("Male");

    return person;
  }

  private Person mockPerson(int i) {
    Person person = new Person();
    
    person.setId(this.counter.incrementAndGet());
    person.setFirstName("Person Name" + i);
    person.setLastName("Person Last Name" + i);
    person.setAddress("Address" + i);
    person.setGender(i % 2 == 0 ? "Male" : "Female");

    return person;
  }
  
}
