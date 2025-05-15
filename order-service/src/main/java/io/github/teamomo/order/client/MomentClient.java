package io.github.teamomo.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.teamomo.order.dto.CartItemDto;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface MomentClient {

  Logger logger = LoggerFactory.getLogger(MomentClient.class);

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
