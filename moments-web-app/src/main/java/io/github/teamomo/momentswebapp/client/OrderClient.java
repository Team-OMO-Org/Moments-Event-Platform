package io.github.teamomo.momentswebapp.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.teamomo.momentswebapp.dto.CartDto;
import io.github.teamomo.momentswebapp.dto.CartItemInfoDto;
import io.github.teamomo.momentswebapp.dto.OrderDto;
import io.github.teamomo.momentswebapp.dto.OrderInfoDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange("/api/v1/orders")
@CircuitBreaker(name = "backend")
@Retry(name = "backend")
public interface OrderClient {

  Logger logger = LoggerFactory.getLogger(OrderClient.class);

  @GetExchange("/carts/{customerId}")
  CartDto getCartByCustomerId(@PathVariable Long customerId);

  @DeleteExchange("/carts/{customerId}")
  void deleteCart(@PathVariable Long customerId);

  @PutExchange("/carts/{customerId}")
  CartDto updateCart(@PathVariable Long customerId, @Valid @RequestBody CartDto cartDto);

  @PostExchange("/{customerId}")
  OrderDto createOrderByCustomerId(@PathVariable Long customerId);

  @PostExchange("/carts/{customerId}/items")
  CartItemInfoDto createCartItem(@PathVariable Long customerId, @Valid @RequestBody CartItemInfoDto cartItemDto);

  @GetExchange("/{orderId}")
  OrderInfoDto getOrderById(@PathVariable Long orderId);

  @DeleteExchange("carts/{customerId}/items/{itemId}")
  void deleteCartItem(@PathVariable Long customerId, @PathVariable Long itemId);
}
