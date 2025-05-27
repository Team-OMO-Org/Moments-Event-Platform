package io.github.teamomo.momentswebapp.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/v1/orders")
@CircuitBreaker(name = "order")
@Retry(name = "order")
public interface OrderClient {

  Logger logger = LoggerFactory.getLogger(OrderClient.class);
}
