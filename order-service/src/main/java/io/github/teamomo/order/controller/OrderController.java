package io.github.teamomo.order.controller;

import io.github.teamomo.order.client.MomentClient;
import io.github.teamomo.order.dto.CartDto;
import io.github.teamomo.order.dto.CartItemDto;
import io.github.teamomo.order.dto.CartItemInfoDto;
import io.github.teamomo.order.dto.OrderDto;
import io.github.teamomo.order.dto.OrderItemDto;
import io.github.teamomo.order.entity.Order;
import io.github.teamomo.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/v1")
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
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Order.class)
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Customer not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponseDto.class)
              )
          )
      }
  )
  @PostMapping("/orders/{customerId}")
  @ResponseStatus(HttpStatus.CREATED)
  public OrderDto createOrderByCustomerId(@PathVariable Long customerId) {
    log.info("Creating order for customer ID: {}", customerId);
    OrderDto orderDto = orderService.createOrderByCustomerId(customerId);
    log.info("Order created for customer ID: {} with order ID: {}", customerId, orderDto.id());
    return orderDto;
  }

  @GetMapping("orders/call")
  public void renderIndex() {
    List<Long> momentIds = List.of(1L, 2L, 3L);
    log.debug("Retrieving ...");

    List<CartItemDto> response = momentClient.getCartItems(momentIds);
    log.info("Retrieved ... : {}", response);
  }

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
    log.info("Checking ticket availability for moment with id: {} and required tickets: {}", id, requiredTickets);
    boolean availability = momentClient.checkTicketAvailability(id, requiredTickets);
    log.info("Ticket availability for moment with id {}: {}", id, availability);
    return availability;
  }

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
  public BigDecimal bookTickets(@PathVariable Long id, @RequestParam int requiredTickets) {
    log.info("Booking tickets for moment with id: {} and required tickets: {}", id, requiredTickets);
    BigDecimal totalSum = momentClient.bookTickets(id, requiredTickets);
    log.info("Booked {} tickets for moment with id {} with total sum: {}", requiredTickets, id, totalSum);
    return totalSum;
  }

  @Operation(
      summary = "Cancel ticket booking if Payment fails for a specific moment via Order Service",
      description = "This endpoint cancels the booking of tickets for a specific moment by its ID using the Moment Service.",
      tags = {"Orders"},
      parameters = {
          @Parameter(
              name = "id",
              description = "The ID of the moment to cancel ticket booking for",
              required = true,
              example = "1"
          ),
          @Parameter(
              name = "ticketsToCancel",
              description = "The number of tickets to cancel",
              required = true,
              example = "2"
          )
      }
  )
  @GetMapping("/moments/{id}/cancel-tickets")
  public void cancelTicketBooking(@PathVariable Long id, @RequestParam int ticketsToCancel) {
    log.info("Cancelling ticket booking for moment with id: {} and tickets to cancel: {}", id, ticketsToCancel);
    momentClient.cancelTicketBooking(id, ticketsToCancel);
    log.info("Cancelled {} tickets for moment with id {}", ticketsToCancel, id);
  }

  @GetMapping("/carts/{customerId}")
  @ResponseStatus(HttpStatus.OK)
  public CartDto getCartByCustomerId(@PathVariable Long customerId) {
    log.info("Fetching cart with customerID: {}", customerId);
    CartDto cartDto = orderService.findCartByCustomerId(customerId);
    log.info("Successfully fetched cart: {}", cartDto);
    return cartDto;
  }

  @PostMapping("/carts/{customerId}")
  @ResponseStatus(HttpStatus.CREATED)
  public CartDto createCart(@PathVariable Long customerId) {
    log.info("Creating new cart with details for customerID: {}", customerId);
    CartDto createdCartDto = orderService.createCart(customerId);
    log.info("Successfully created cart with ID: {}", createdCartDto.id());
    return createdCartDto;
  }

  @PutMapping("/carts/{customerId}")
  @ResponseStatus(HttpStatus.OK)
  public CartDto updateCart(@PathVariable Long customerId, @Valid @RequestBody CartDto cartDto) {
    log.info("Updating cart with customerID: {}", customerId);
    CartDto updatedCartDto = orderService.updateCart(customerId, cartDto);
    log.info("Successfully updated cart with customerID: {}", customerId);
    return updatedCartDto;
  }

  @DeleteMapping("/carts/{customerId}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteCart(@PathVariable Long customerId) {
    log.info("Deleting cart with customerID: {}", customerId);
    orderService.deleteCart(customerId);
    log.info("Successfully deleted cart with customerID: {}", customerId);
  }

  @GetMapping("/carts/{customerId}/items")
  @ResponseStatus(HttpStatus.OK)
  public List<CartItemInfoDto> getAllCartItems(@PathVariable Long customerId) {
    log.info("Fetching all items with customerID: {}", customerId);
    List<CartItemInfoDto> cartItemDtos= orderService.getAllCartItems(customerId);
    log.info("Successfully fetched {} items", cartItemDtos.size());
    return cartItemDtos;
  }

  @PostMapping("/carts/{customerId}/items")
  @ResponseStatus(HttpStatus.CREATED)
  public CartItemInfoDto createCartItem(@PathVariable Long customerId, @Valid @RequestBody CartItemInfoDto cartItemDto) {
    log.info("Creating new cart item with details: {}", cartItemDto);
    CartItemInfoDto createdCartItemDto = orderService.createCartItem(customerId, cartItemDto);
    log.info("Successfully created cart item with ID: {}", createdCartItemDto.id());
    return createdCartItemDto;
  }

  @PutMapping("/carts/{customerId}/items/{itemId}")
  @ResponseStatus(HttpStatus.OK)
  public CartItemInfoDto updateCartItem(@PathVariable Long customerId, @PathVariable Long itemId, @Valid @RequestBody CartItemInfoDto cartItemDto) {
    log.info("Updating cart item with ID: {}", itemId);
    CartItemInfoDto updatedCartItemDto = orderService.updateCartItem(itemId, cartItemDto);
    log.info("Successfully updated cart item with ID: {}", itemId);
    return updatedCartItemDto;
  }

  @DeleteMapping("/carts/{customerId}/items/{itemId}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteCartItem(@PathVariable Long customerId, @PathVariable Long itemId) {
    log.info("Deleting cart item with ID: {}", itemId);
    orderService.deleteCartItem(itemId);
    log.info("Successfully deleted cart item with ID: {}", itemId);
  }
}
