package io.github.teamomo.order.controller;

import io.github.teamomo.moment.dto.ErrorResponseDto;
import io.github.teamomo.order.client.MomentClient;
import io.github.teamomo.order.dto.CartDto;
import io.github.teamomo.order.dto.CartItemDto;
import io.github.teamomo.order.dto.CartItemInfoDto;
import io.github.teamomo.order.dto.OrderDto;
import io.github.teamomo.order.dto.OrderInfoDto;
import io.github.teamomo.order.dto.OrderItemDto;
import io.github.teamomo.order.entity.Order;
import io.github.teamomo.order.service.CartService;
import io.github.teamomo.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import java.math.BigDecimal;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@Tag(
    name = "REST APIs for Order Service in MomentsPlatform",
    description = "REST APIs in MomentsPlatform to FETCH, CREATE, and MANAGE orders and their details"
)
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private final MomentClient momentClient;
  private final OrderService orderService;

  @Operation(
      summary = "Create an order for a specific customer",
      description = "This endpoint creates an order for a specific customer by their ID.",
      tags = {"Orders"},
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Order created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Order.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Customer not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class)))
      })
  @PostMapping("/{customerId}")
  @ResponseStatus(HttpStatus.CREATED)
  public OrderDto createOrderByCustomerId(@PathVariable Long customerId) {
    log.info("Creating order for customer ID: {}", customerId);
    OrderDto orderDto = orderService.createOrderByCustomerId(customerId);
    log.info("Order created for customer ID: {} with order ID: {}", customerId, orderDto.id());
    orderService.sendOrderNotification(orderDto);
    return orderDto;
  }

  // todo: remove later, added as an example
  @GetMapping("/call")
  public void renderIndex() {
    List<Long> momentIds = List.of(1L, 2L, 3L);
    log.debug("Retrieving ...");

    List<CartItemDto> response = momentClient.getCartItems(momentIds);
    log.info("Retrieved ... : {}", response);
  }

  @Operation(
      summary = "Check ticket availability for a specific moment via Order Service",
      description =
          "This endpoint checks if the required number of tickets are available for a specific moment by its ID using the Moment Service.",
      tags = {"Orders"},
      parameters = {
        @Parameter(
            name = "id",
            description = "The ID of the moment to check ticket availability for",
            required = true,
            example = "1"),
        @Parameter(
            name = "requiredTickets",
            description = "The number of tickets required",
            required = true,
            example = "5")
      })
  // todo: remove later, added for testing purpose
  @GetMapping("/moments/{id}/check-availability")
  public boolean checkTicketAvailability(@PathVariable Long id, @RequestParam int requiredTickets) {
    log.info(
        "Checking ticket availability for moment with id: {} and required tickets: {}",
        id,
        requiredTickets);
    boolean availability = momentClient.checkTicketAvailability(id, requiredTickets);
    log.info("Ticket availability for moment with id {}: {}", id, availability);
    return availability;
  }

  @Operation(
      summary = "Book tickets for a specific moment via Order Service",
      description =
          "This endpoint books the required number of tickets for a specific moment by its ID using the Moment Service.",
      tags = {"Orders"},
      parameters = {
        @Parameter(
            name = "id",
            description = "The ID of the moment to book tickets for",
            required = true,
            example = "1"),
        @Parameter(
            name = "requiredTickets",
            description = "The number of tickets to book",
            required = true,
            example = "5")
      })
  // todo: remove later, added for testing purpose
  @GetMapping("/moments/{id}/book-tickets")
  public BigDecimal bookTickets(@PathVariable Long id, @RequestParam int requiredTickets) {
    log.info(
        "Booking tickets for moment with id: {} and required tickets: {}", id, requiredTickets);
    BigDecimal totalSum = momentClient.bookTickets(id, requiredTickets);
    log.info(
        "Booked {} tickets for moment with id {} with total sum: {}",
        requiredTickets,
        id,
        totalSum);
    return totalSum;
  }

  @Operation(
      summary = "Cancel ticket booking if Payment fails for a specific moment via Order Service",
      description =
          "This endpoint cancels the booking of tickets for a specific moment by its ID using the Moment Service.",
      tags = {"Orders"},
      parameters = {
        @Parameter(
            name = "id",
            description = "The ID of the moment to cancel ticket booking for",
            required = true,
            example = "1"),
        @Parameter(
            name = "ticketsToCancel",
            description = "The number of tickets to cancel",
            required = true,
            example = "2")
      })
  // todo: remove later, added for testing purpose
  @GetMapping("/moments/{id}/cancel-tickets")
  public void cancelTicketBooking(@PathVariable Long id, @RequestParam int ticketsToCancel) {
    log.info(
        "Cancelling ticket booking for moment with id: {} and tickets to cancel: {}",
        id,
        ticketsToCancel);
    momentClient.cancelTicketBooking(id, ticketsToCancel);
    log.info("Cancelled {} tickets for moment with id {}", ticketsToCancel, id);
  }

  @GetMapping("/kafka")
  public void testKafka() {
    orderService.testKafka();
  }

  @Operation(
      summary = "Retrieve an order by ID",
      description = "This endpoint retrieves the details of an order identified by its ID.",
      tags = {"Orders"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - Order retrieved successfully",
          content = @Content(
              schema = @Schema(implementation = OrderInfoDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "HTTP Status Not Found - Order not found for the given ID",
          content = @Content(
              schema = @Schema(implementation = ErrorResponseDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "500",
          description = "HTTP Status Internal Server Error",
          content = @Content(
              schema = @Schema(implementation = ErrorResponseDto.class)
          )
      )
  })
  @GetMapping("/{orderId}")
  public OrderInfoDto getOrderById(@PathVariable Long orderId) {
    log.info("Getting order with id: {}", orderId);
    OrderInfoDto orderInfoDto = orderService.getOrderById(orderId);
    log.info("Got order with id: {}", orderId);
    return orderInfoDto;
  }
}
