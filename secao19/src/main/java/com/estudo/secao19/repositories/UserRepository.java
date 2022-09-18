package com.estudo.secao19.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.estudo.secao19.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
  
  @Query("SELECT u FROM User u WHERE u.userName =:userName")
  User findByUsername(@Param("userName") String userName);

}
