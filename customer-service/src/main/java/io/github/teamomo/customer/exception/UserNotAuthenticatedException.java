package io.github.teamomo.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UserNotAuthenticatedException extends RuntimeException {
  public UserNotAuthenticatedException(String message) {
    super(message);
  }
}
