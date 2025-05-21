package io.github.teamomo.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.teamomo.order.dto.CartItemDto;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface MomentClient {

  Logger logger = LoggerFactory.getLogger(MomentClient.class);

  @PostExchange("/api/v1/moments/{id}/book-tickets")
  @CircuitBreaker(name = "moment")
  @Retry(name = "moment")
  public BigDecimal bookTickets(@PathVariable Long id, @RequestParam int requiredTickets);


  @PostExchange("/api/v1/moments/{id}/cancel-tickets")
  @CircuitBreaker(name = "moment")
  @Retry(name = "moment")
  public void cancelTicketBooking(@PathVariable Long id, @RequestParam int ticketsToCancel);

  @GetExchange("/api/v1/moments/{id}/check-availability")
  @CircuitBreaker(name = "moment")
  @Retry(name = "moment")
  public boolean checkTicketAvailability( @PathVariable Long id,
      @RequestParam int requiredTickets);

  @PostExchange("/api/v1/moments/cart-items")
  @CircuitBreaker(name = "moment")
  @Retry(name = "moment")
  List<CartItemDto> getCartItems(@RequestBody List<Long> momentIds);


//    default boolean fallbackMethod(String skuCode, Integer quantity, Throwable t) {
//        logger.error("Can not get inventory for skuCode {}, failure reason: {}", skuCode, t
//        .getMessage());
//        return false;
//    }
}
