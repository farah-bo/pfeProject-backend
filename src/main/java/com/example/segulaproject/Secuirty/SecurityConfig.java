package com.example.segulaproject.Secuirty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /* @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/signIn").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic();

        return http.build();
    } */

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
              .cors().and()
              .csrf().disable()
              .authorizeHttpRequests(auth -> auth
                      .requestMatchers(HttpMethod.POST, "/api/auth/signIn").permitAll()
                      .requestMatchers(HttpMethod.POST, "/api/auth/signup/**").permitAll()
                      .requestMatchers(HttpMethod.POST, "/api/auth//signupadmin").permitAll()
                      .requestMatchers(HttpMethod.POST, "/api/auth//signupPatient").permitAll()

                      .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                      .anyRequest().permitAll()
              )
              .httpBasic();
      return http.build();
  }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
