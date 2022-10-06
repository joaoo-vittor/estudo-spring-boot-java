package com.estudo.secao21.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.estudo.secao21.security.jwt.JwtConfigurer;
import com.estudo.secao21.security.jwt.JwtTokenProvider;

@Configuration
public class SecurityConfig {
  
  @Autowired
  private JwtTokenProvider tokenProvider;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .httpBasic().disable()
      .csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
          .authorizeRequests()
          .antMatchers(
            "/auth/signin", 
            "/auth/refresh",
            "/api-docs/**",
            "/swagger-ui/index.html**"
          ).permitAll()
          .antMatchers("/api/**").authenticated()
          .antMatchers("/users").denyAll()
        .and()
          .cors()
        .and()
        .apply(new JwtConfigurer(tokenProvider));
    
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManagerBean(
    AuthenticationConfiguration authenticationConfiguration
  ) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
    DelegatingPasswordEncoder passwordEncoder = 
      new DelegatingPasswordEncoder("pbkdf2", encoders);
    
    passwordEncoder.setDefaultPasswordEncoderForMatches(
      new Pbkdf2PasswordEncoder()
    );

    return passwordEncoder;
  }

}
