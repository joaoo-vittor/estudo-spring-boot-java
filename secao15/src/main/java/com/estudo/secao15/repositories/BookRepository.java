package com.estudo.secao15.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estudo.secao15.model.Books;

@Repository
public interface BookRepository extends JpaRepository<Books, Long> {}
