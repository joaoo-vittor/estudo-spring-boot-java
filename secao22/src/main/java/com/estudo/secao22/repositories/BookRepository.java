package com.estudo.secao22.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estudo.secao22.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {}
