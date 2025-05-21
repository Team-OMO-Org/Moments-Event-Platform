package io.github.teamomo.order.controller;

import io.github.teamomo.order.dto.CartDto;
import io.github.teamomo.order.dto.CartItemInfoDto;
import io.github.teamomo.order.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {

  private final CartService cartService;

  @GetMapping("/{customerId}")
  public CartDto getCartByCustomerId(@PathVariable Long customerId) {
    log.info("Fetching cart for customer ID: {}", customerId);
    return cartService.findCartByCustomerId(customerId);
  }

  @PostMapping("/{customerId}")
  @ResponseStatus(HttpStatus.CREATED)
  public CartDto createCart(@PathVariable Long customerId) {
    log.info("Creating cart for customer ID: {}", customerId);
    return cartService.createCart(customerId);
  }

  @PutMapping("/{customerId}")
  public CartDto updateCart(@PathVariable Long customerId, @Valid @RequestBody CartDto cartDto) {
    log.info("Updating cart for customer ID: {}", customerId);
    return cartService.updateCart(customerId, cartDto);
  }

  @DeleteMapping("/{customerId}")
  public void deleteCart(@PathVariable Long customerId) {
    log.info("Deleting cart for customer ID: {}", customerId);
    cartService.deleteCart(customerId);
  }

  @GetMapping("/{customerId}/items")
  public List<CartItemInfoDto> getAllCartItems(@PathVariable Long customerId) {
    log.info("Getting all cart items for customer ID: {}", customerId);
    return cartService.getAllCartItems(customerId);
  }

  @PostMapping("/{customerId}/items")
  @ResponseStatus(HttpStatus.CREATED)
  public CartItemInfoDto createCartItem(@PathVariable Long customerId, @Valid @RequestBody CartItemInfoDto cartItemDto) {
    log.info("Creating cart item for customer ID: {}", customerId);
    return cartService.createCartItem(customerId, cartItemDto);
  }

  @PutMapping("/{customerId}/items/{itemId}")
  public CartItemInfoDto updateCartItem(@PathVariable Long customerId, @PathVariable Long itemId, @Valid @RequestBody CartItemInfoDto cartItemDto) {
    log.info("Updating cart item with ID: {}", itemId);
    return cartService.updateCartItem(itemId, cartItemDto);
  }

  @DeleteMapping("/{customerId}/items/{itemId}")
  public void deleteCartItem(@PathVariable Long customerId, @PathVariable Long itemId) {
    log.info("Deleting cart item with ID: {}", itemId);
    cartService.deleteCartItem(itemId);
  }
}
