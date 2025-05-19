package io.github.teamomo.order.controller;

import io.github.teamomo.order.client.MomentClient;
import io.github.teamomo.order.dto.CartDto;
import io.github.teamomo.order.dto.CartItemDto;
import io.github.teamomo.order.entity.Cart;
import io.github.teamomo.order.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

  private final MomentClient momentClient;
  private final OrderService orderService;

  @GetMapping("orders/call")
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

  @GetMapping("/carts/{customerId}")
  @ResponseStatus(HttpStatus.OK)
  public CartDto getCartByCustomerId(@PathVariable Long customerId){
    logger.info("Fetching cart with customerID: {}", customerId);
    CartDto cartDto = orderService.findCartByCustomerId(customerId);
    logger.info("Successfully fetched cart: {}", cartDto);

    return cartDto;
  }

  @PutMapping("/carts/{customerId}")
  @ResponseStatus(HttpStatus.OK)
  public CartDto updateCart(@PathVariable Long customerId, @Valid @RequestBody CartDto cartDto) {


    logger.info("Updating cart with customerID: {}", customerId);
    CartDto updatedCartDto = orderService.updateCart(customerId, cartDto);
    logger.info("Successfully updated cart with customerID: {}", customerId);

    return updatedCartDto;
  }

  @DeleteMapping("/carts/{customerId}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteCart(@PathVariable Long customerId) {

    logger.info("Deleting cart with customerID: {}", customerId);
    orderService.deleteCart(customerId);
    logger.info("Successfully deleted cart with customerID: {}", customerId);
  }

  @GetMapping("/carts/{customerId}/items")
  @ResponseStatus(HttpStatus.OK)
  public List<CartItemDto> getAllCartItems(@PathVariable Long customerId
  ) {

    logger.info("Fetching all items with customerID: {}", customerId);
    List<CartItemDto> cartItemDtos= orderService.getAllCartItems(customerId);
    logger.info("Successfully fetched {} items", cartItemDtos.size());

    return cartItemDtos;

  }

  @PostMapping("/carts/{customerId}/items")
  @ResponseStatus(HttpStatus.CREATED)
  public CartItemDto createCartItem(@PathVariable Long customerId, @Valid @RequestBody CartItemDto cartItemDto) {

    logger.info("Creating new cart item with details: {}", cartItemDto);
    CartItemDto createdcartItemDto = orderService.createCartItem(cartItemDto);
    logger.info("Successfully created cart item with ID: {}", createdcartItemDto.id());

    return createdcartItemDto;
  }

  @PutMapping("/carts/{customerId}/items/{itemId}")
  @ResponseStatus(HttpStatus.OK)
  public CartItemDto updateCartItem(@PathVariable Long customerId, @PathVariable Long itemId, @Valid @RequestBody CartItemDto cartItemDto) {


    logger.info("Updating cart item with ID: {}", itemId);
    CartItemDto updatedCartItemDto = orderService.updateCartItem(itemId, cartItemDto);
    logger.info("Successfully updated cart item with ID: {}", itemId);

    return updatedCartItemDto;
  }

  @DeleteMapping("/carts/{customerId}/items/{itemId}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteCartItem(@PathVariable Long customerId, @PathVariable Long itemId) {

    logger.info("Deleting cart item with ID: {}", itemId);
    orderService.deleteCartItem(itemId);
    logger.info("Successfully deleted cart item with ID: {}", itemId);
  }

}