package com.estudo.secao19.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estudo.secao19.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {}
