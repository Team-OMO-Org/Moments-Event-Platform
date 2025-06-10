package io.github.teamomo.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  private final String[] freeResourceUrls = {
//            "/actuator/**",
      "/swagger-ui.html",
      "/swagger-ui/**",
      "/v3/api-docs/**",
      "/swagger-resources/**",
      "/api-docs/**"
  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .cors().disable()
        .csrf(CsrfConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(freeResourceUrls).permitAll()
            .anyRequest().authenticated())
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .build();
  }
}