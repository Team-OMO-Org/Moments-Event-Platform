package io.github.teamomo.order.exception;

public class CartIsEmptyException extends RuntimeException {
  public CartIsEmptyException(Long customerId) {
    super("Cart is empty for customer ID: " + customerId);
  }
}
