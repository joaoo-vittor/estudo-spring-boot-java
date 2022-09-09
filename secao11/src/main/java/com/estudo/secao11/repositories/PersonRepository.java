package com.estudo.secao11.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estudo.secao11.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {}
