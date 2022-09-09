package com.estudo.secao13.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estudo.secao13.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {}
