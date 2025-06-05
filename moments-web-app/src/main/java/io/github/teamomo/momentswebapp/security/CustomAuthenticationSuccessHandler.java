package io.github.teamomo.momentswebapp.security;

import io.github.teamomo.momentswebapp.util.CustomerManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.jwt.Jwt;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // This class is used to handle successful authentication events.
  private final CustomerManager customerManager;

    // This method is called when authentication is successful.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
      log.info("Authentication successful for user: {}", authentication.getName());

        // Retrieve the userId from the JWT token
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getClaimAsString("sub");
        log.info("User ID from JWT token: {}", userId);

        // Check and update the customerId based on the userId
      Long customerId =  customerManager.checkCustomerId(request, response);
      log.info("Check customerID from back-end: {}", customerId);
    }

}
