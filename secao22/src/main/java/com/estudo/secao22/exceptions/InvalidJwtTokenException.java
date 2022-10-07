package com.estudo.secao22.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidJwtTokenException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;

  public InvalidJwtTokenException() {
    super("Expired or invelid JWT token!");
  }

}
