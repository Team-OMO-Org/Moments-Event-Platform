package io.github.teamomo.momentswebapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OAuth2ExceptionHandler {

  @ExceptionHandler(ClientAuthorizationException.class)
  public String handleOAuth2ClientError(ClientAuthorizationException ex, HttpServletRequest request) {
    return "redirect:/oauth2/authorization/moments-web-app";
  }
}
