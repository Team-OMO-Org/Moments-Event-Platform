package io.github.teamomo.order.controller;

import io.github.teamomo.moment.dto.ErrorResponseDto;
import io.github.teamomo.order.client.MomentClient;
import io.github.teamomo.order.dto.CartItemDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    List<Long> momentIds = List.of(1L, 2L, 3L);
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

  //todo: added for testing purposes, remove it later, call from OrderService
  @Operation(
      summary = "Check ticket availability for a specific moment via Order Service",
      description = "This endpoint checks if the required number of tickets are available for a specific moment by its ID using the Moment Service.",
      tags = {"Orders"},
      parameters = {
          @Parameter(
              name = "id",
              description = "The ID of the moment to check ticket availability for",
              required = true,
              example = "1"
          ),
          @Parameter(
              name = "requiredTickets",
              description = "The number of tickets required",
              required = true,
              example = "5"
          )
      }
  )
  @GetMapping("/moments/{id}/check-availability")
  public boolean checkTicketAvailability(@PathVariable Long id, @RequestParam int requiredTickets) {
    log.info("Checking ticket availability for moment with id: {} and required tickets: {}", id,
        requiredTickets);
    boolean availability = momentClient.checkTicketAvailability(id, requiredTickets);
    log.info("Ticket availability for moment with id {}: {}", id, availability);
    return availability;
  }

  //todo: added for testing purposes, remove it later, call from OrderService
  @Operation(
      summary = "Book tickets for a specific moment via Order Service",
      description = "This endpoint books the required number of tickets for a specific moment by its ID using the Moment Service.",
      tags = {"Orders"},
      parameters = {
          @Parameter(
              name = "id",
              description = "The ID of the moment to book tickets for",
              required = true,
              example = "1"
          ),
          @Parameter(
              name = "requiredTickets",
              description = "The number of tickets to book",
              required = true,
              example = "5"
          )
      }
  )
  @GetMapping("/moments/{id}/book-tickets")
  public void bookTickets(@PathVariable Long id, @RequestParam int requiredTickets) {
    log.info("Booking tickets for moment with id: {} and required tickets: {}", id,
        requiredTickets);
    momentClient.bookTickets(id, requiredTickets);
    log.info("Booked {} tickets for moment with id {}", requiredTickets, id);
  }
}

