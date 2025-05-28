package io.github.teamomo.momentswebapp.util;

import io.github.teamomo.momentswebapp.client.CustomerClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class CustomerManager {

  private static final String CUSTOMER_ID_COOKIE_NAME = "customerId";

  private final CookieUtil cookieUtil;
  private final CustomerClient customerClient;

  public Long getCustomerId() {
    // Retrieve the current HttpServletRequest
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes == null) {
      throw new IllegalStateException("No request context available.");
    }

    HttpServletRequest request = attributes.getRequest();
    HttpServletResponse response = attributes.getResponse();

    if (response == null) {
      throw new IllegalStateException("Response is not available.");
    }

     return checkCustomerId(request, response);
  }

  // Retrieve customerId from cookie or update it from Customer Service if not present/expired
  public Long checkCustomerId(HttpServletRequest request, HttpServletResponse response) {
    // Get the userId from the token
    String userId = getUserIdFromToken();

    // Retrieve customerId from the cookie
    String customerId = cookieUtil.getCookieValue(request, CUSTOMER_ID_COOKIE_NAME);

    if (customerId == null) {
      // Call the Customer service to get the customerId
      Long fetchedCustomerId = customerClient.checkUserByKeycloakId(userId);

      // Store the fetched customerId in a cookie
      cookieUtil.storeCookie(response, CUSTOMER_ID_COOKIE_NAME, fetchedCustomerId.toString());

      return fetchedCustomerId;
    }

    // Refresh the cookie expiration time
    cookieUtil.storeCookie(response, CUSTOMER_ID_COOKIE_NAME, customerId);

    return Long.valueOf(customerId);
  }

  private static String getUserIdFromToken() {
    // Retrieve the Authentication object from the SecurityContext
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !(authentication.getPrincipal() instanceof DefaultOidcUser oidcUser)) {
      throw new IllegalStateException("Authentication is missing or invalid.");
    }

    String userId = oidcUser.getAttribute("sub");

    if (userId == null) {
      throw new IllegalStateException("User ID is missing in the JWT.");
    }
    return userId;
  }

}
