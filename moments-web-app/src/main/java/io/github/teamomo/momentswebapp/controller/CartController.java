package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.BackendClient;
import io.github.teamomo.momentswebapp.client.OrderClient;
import io.github.teamomo.momentswebapp.dto.CartDto;
import io.github.teamomo.momentswebapp.dto.CartItemViewDto;
import io.github.teamomo.momentswebapp.dto.CartViewDto;
import io.github.teamomo.momentswebapp.dto.MomentDto;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CartController {

  private final OrderClient orderClient;
  private final BackendClient backendClient;

  @GetMapping("/carts/{customerId}")
  public String renderCart(@PathVariable Long customerId, Model model){

    log.debug("Retrieving cart for cart page from backend");
    CartDto cartDto = orderClient.getCartByCustomerId(customerId);
    log.info("Retrieved cart for cart page from backend: {}",
        cartDto);

    log.debug("Retrieving list of items for cart page from backend");
    List<CartItemViewDto> items = cartDto.cartItems().stream()
        .map(cartItem -> {
          log.debug("Retrieving moment for cart page from backend");
          MomentDto moment = backendClient.getMomentById(cartItem.momentId());
          log.info("Retrieved moment for cart page from backend: {}",
              moment);
          BigDecimal totalPrice = moment.price().multiply(BigDecimal.valueOf(cartItem.quantity()));
          return new CartItemViewDto(
              moment.id(),
              moment.title(),
              moment.thumbnail(),
              moment.price(),
              cartItem.quantity(),
              cartItem.isAvailable(),
              totalPrice);
        }).toList();
    log.info("Retrieved list of items for cart page from backend: {}",
        items);

    BigDecimal subtotal = items.stream()
        .map(CartItemViewDto::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    CartViewDto cartViewDto = new CartViewDto(customerId, items, subtotal);
    model.addAttribute("cartView", cartViewDto);

    return "cart";
  }
  @GetMapping("/clear-cart")
  public String renderClearCartPage() {
    return "clear-cart";
  }

  /*@PostMapping("/carts/{customerId}/clear")
  public String clearCart(@PathVariable Long customerId) {
    //delete cart from db in backend
    return "redirect:/clear-cart";
  }*/

  @DeleteMapping("/carts/{customerId}")
  public String clearCart(@PathVariable Long customerId) {
    log.debug("Deleting cart for customerId from backend");
    orderClient.deleteCart(customerId);
    log.info("Deleted cart for customer ID: {}", customerId);
    return "redirect:/clear-cart";
  }
}
