package com.estudo.secao20.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estudo.secao20.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {}
