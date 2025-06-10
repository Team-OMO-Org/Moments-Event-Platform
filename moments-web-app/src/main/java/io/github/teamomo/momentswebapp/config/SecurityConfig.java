package io.github.teamomo.momentswebapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class SecurityConfig {

  private final String[] freeResourceUrls = {
      "/*",
      "/index*",
      "/moment/*",
      "css/**",
      "fonts/**",
      "img/**",
      "js/**"
  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
      LogoutSuccessHandler keycloakLogoutSuccessHandler) throws Exception {
    return httpSecurity
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(freeResourceUrls).permitAll()
            .anyRequest().authenticated())
        .oauth2Login(Customizer.withDefaults())
        .logout(logout -> logout
            .logoutSuccessHandler(keycloakLogoutSuccessHandler)  // use custom handler
            .permitAll()
        )
        .build();
  }

  @Bean
  public OAuth2AuthorizedClientManager authorizedClientManager(
      ClientRegistrationRepository clientRegistrationRepository,
      OAuth2AuthorizedClientRepository authorizedClientRepository) {

    OAuth2AuthorizedClientProvider authorizedClientProvider =
        OAuth2AuthorizedClientProviderBuilder.builder()
            .authorizationCode()
            .refreshToken() // Enables automatic refresh
            .build();

    DefaultOAuth2AuthorizedClientManager authorizedClientManager =
        new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
    authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

    return authorizedClientManager;
  }

  @Bean
  public LogoutSuccessHandler keycloakLogoutSuccessHandler(
      ClientRegistrationRepository repository) {

    OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler =
        new OidcClientInitiatedLogoutSuccessHandler(repository);

    logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");

    return logoutSuccessHandler;
  }
}