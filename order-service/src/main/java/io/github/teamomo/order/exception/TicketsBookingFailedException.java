package io.github.teamomo.order.exception;

public class TicketsBookingFailedException extends RuntimeException {
  public TicketsBookingFailedException(Long momentId, Throwable cause) {
    super("Failed to book tickets for moment ID: " + momentId, cause);
  }

  public TicketsBookingFailedException(Long momentId) {
    super("Failed to book tickets for moment ID: " + momentId);
  }
}
