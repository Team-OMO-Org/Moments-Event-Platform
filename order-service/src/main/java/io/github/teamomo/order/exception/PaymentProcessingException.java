package io.github.teamomo.order.exception;

public class PaymentProcessingException extends RuntimeException {
  public PaymentProcessingException(String message) {
    super(message);
  }
}
