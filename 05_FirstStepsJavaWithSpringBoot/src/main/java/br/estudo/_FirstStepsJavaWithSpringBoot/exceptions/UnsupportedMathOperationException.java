package br.estudo._FirstStepsJavaWithSpringBoot.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedMathOperationException extends RuntimeException {
  
  public UnsupportedMathOperationException(String messageError) {
    super(messageError);
  }
  
  private static final long serialVersionUID = 1L;
}
