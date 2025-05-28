package io.github.teamomo.momentswebapp.security;

import io.github.teamomo.momentswebapp.client.CustomerClient;
import io.github.teamomo.momentswebapp.util.CustomerManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.jwt.Jwt;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // This class is used to handle successful authentication events.
  private final CustomerManager customerManager;

    // This method is called when authentication is successful.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
      customerManager.checkCustomerId(request, response);
    }

}
