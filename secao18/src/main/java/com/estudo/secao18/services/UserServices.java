package com.estudo.secao18.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.estudo.secao18.repositories.UserRepository;

@Service
public class UserServices implements UserDetailsService {
  
  private Logger logger = Logger.getLogger(UserServices.class.getName());

  @Autowired
  UserRepository repository;
  
  // @Autowired
  public UserServices(UserRepository repository) {
    this.repository = repository;
  }
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    logger.info("Finding one User by name" + username + "!");
    var user = this.repository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("Username " + username + " not found!");
    }
    return user;
  }

}
