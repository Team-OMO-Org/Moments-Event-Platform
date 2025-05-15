package io.github.teamomo.order.controller;

import io.github.teamomo.order.client.MomentClient;
import io.github.teamomo.order.dto.CartItemDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private final MomentClient momentClient;

  @GetMapping("/call")
  public void renderIndex() {
    List<Long> momentIds = List.of(1L,2L,3L);
    log.debug("Retrieving ...");

/*
      // simple RestClient call example
      List<CartItemDto> response = RestClient.builder() // build RestClient
        .baseUrl("http://localhost:8081")
        .build()
        // HTTP Request
        .post()
        .uri("/api/v1/moments/cart-items")
        .body(momentIds)
        // block and wait for response
        .retrieve()
        // get response body and map with Jackson to List<CartItemDto>
        .body(new ParameterizedTypeReference<List<CartItemDto>>() {});*/

    List<CartItemDto> response = momentClient.getCartItems(momentIds);
    log.info("Retrieved ... : {}",
        response);
  }
}