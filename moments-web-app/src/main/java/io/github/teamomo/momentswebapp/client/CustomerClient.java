package io.github.teamomo.momentswebapp.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/v1/customers")
@CircuitBreaker(name = "backend")
@Retry(name = "backend")
public interface CustomerClient {

  @PostExchange("/check")
  public Long checkUserByKeycloakId(@RequestBody String keycloakUserId);

}
