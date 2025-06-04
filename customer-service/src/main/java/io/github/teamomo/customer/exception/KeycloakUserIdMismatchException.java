package io.github.teamomo.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class KeycloakUserIdMismatchException extends RuntimeException {
  public KeycloakUserIdMismatchException(String message) {
    super(message);
  }
}
