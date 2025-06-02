package io.github.teamomo.momentswebapp.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.teamomo.momentswebapp.dto.CustomerDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/v1/customers")
@CircuitBreaker(name = "backend")
@Retry(name = "backend")
public interface CustomerClient {

  @PostExchange("/check")
  Long checkUserByKeycloakId(@RequestBody String keycloakUserId);

  @GetExchange("/{id}")
  CustomerDto getCustomerById(@PathVariable Long id);
}

