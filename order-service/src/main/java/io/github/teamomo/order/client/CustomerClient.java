package io.github.teamomo.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.teamomo.order.dto.CartItemDto;
import io.github.teamomo.order.dto.CustomerDto;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface CustomerClient {

  Logger logger = LoggerFactory.getLogger(CustomerClient.class);

  @GetExchange("/api/v1/customers/{id}")
  @CircuitBreaker(name = "customer")
  @Retry(name = "customer")
  CustomerDto getCustomerById(@PathVariable Long id);


//    default boolean fallbackMethod(String skuCode, Integer quantity, Throwable t) {
//        logger.error("Can not get inventory for skuCode {}, failure reason: {}", skuCode, t
//        .getMessage());
//        return false;
//    }
}
