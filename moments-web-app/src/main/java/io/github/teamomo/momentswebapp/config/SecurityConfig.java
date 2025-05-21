package io.github.teamomo.momentswebapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  private final String[] freeResourceUrls = {
      "/*",
      "/index*",
  };

  // ToDo: even if root is not secured, currently a jwt needs to be sent to the back-end
//  @Bean
//  public WebSecurityCustomizer webSecurityCustomizer() {
//    return (web) -> web
//        .ignoring()
//        .requestMatchers(freeResourceUrls);
//  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(freeResourceUrls).permitAll()
            .anyRequest().authenticated())
        .oauth2Login(Customizer.withDefaults())
        .build();
  }
}