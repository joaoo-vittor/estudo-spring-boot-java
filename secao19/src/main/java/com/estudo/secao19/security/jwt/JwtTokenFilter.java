package com.estudo.secao19.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.estudo.secao19.exceptions.InvalidJwtTokenException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class JwtTokenFilter extends GenericFilterBean {

  @Autowired
  private JwtTokenProvider tokenProvider;

  public JwtTokenFilter(JwtTokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @Override
  public void doFilter(
    ServletRequest request, ServletResponse response, FilterChain chain
  ) throws IOException, ServletException {
    // Obtem o token da request
    String token = tokenProvider.resolveToken((HttpServletRequest) request);
    
    // Valida o token
    if (token != null && tokenProvider.validateToken(token)) {
      // obtem uma autenticacao
      Authentication auth = tokenProvider.getAuthentication(token);
      if (auth != null) {
        // Seta a auteenticacao na secao do spring
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    }

    chain.doFilter(request, response);
  }

  // @Override
  // protected void doFilterInternal(
  //   HttpServletRequest request, 
  //   HttpServletResponse response, 
  //   FilterChain chain
  // ) throws ServletException, IOException {
  //   Boolean tokenIsValid = false;
  //   // Obtem o token da request
  //   String token = tokenProvider.resolveToken((HttpServletRequest) request);
    
  //   // Valida o token
  //   if (token != null && tokenProvider.validateToken(token)) {
  //     // obtem uma autenticacao
  //     Authentication auth = tokenProvider.getAuthentication(token);
  //     tokenIsValid = true;
  //     if (auth != null) {
  //       // Seta a auteenticacao na secao do spring
  //       SecurityContextHolder.getContext().setAuthentication(auth);
  //     }

  //   }
    
  //   if(!tokenIsValid) {
  //     // response.
  //     throw new InvalidJwtTokenException();
  //   }

  //   chain.doFilter(request, response);
  // }
  
}
