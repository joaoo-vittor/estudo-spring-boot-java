package com.estudo.secao21.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estudo.secao21.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {}
