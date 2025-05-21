package io.github.teamomo.moment.exception;

public class InsufficientTicketsException extends RuntimeException {
  public InsufficientTicketsException(String message) {
    super(message);
  }
}
