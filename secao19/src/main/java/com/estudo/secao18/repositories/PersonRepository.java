package com.estudo.secao18.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estudo.secao18.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {}
