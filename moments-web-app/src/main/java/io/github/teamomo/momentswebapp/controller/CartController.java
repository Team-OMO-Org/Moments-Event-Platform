package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.BackendClient;
import io.github.teamomo.momentswebapp.client.OrderClient;
import io.github.teamomo.momentswebapp.dto.CartDto;
import io.github.teamomo.momentswebapp.dto.CartItemInfoDto;
import io.github.teamomo.momentswebapp.dto.CartItemViewDto;
import io.github.teamomo.momentswebapp.dto.CartViewDto;
import io.github.teamomo.momentswebapp.dto.MomentDto;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CartController {

  private final OrderClient orderClient;
  private final BackendClient backendClient;

  /*@GetMapping("/carts/{customerId}")
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
              cartItem.id(),
              cartItem.cartId(),
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

    CartViewDto cartViewDto = new CartViewDto(cartDto.id(), customerId, new ArrayList<>(items), subtotal);
    model.addAttribute("cartView", cartViewDto);

    return "cart_test_updateform";
  }*/
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

  /*@PostMapping("/carts/{customerId}")
  public String updateCart(@PathVariable Long customerId, @Valid @ModelAttribute("cartView") CartViewDto cartView, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      // Reload moments and subtotal like in GET because your cartView won't have them fully on error
      List<CartItemViewDto> items = cartView.getItems().stream()
          .map(item -> {
            MomentDto moment = backendClient.getMomentById(item.getMomentId());
            BigDecimal totalPrice = moment.price().multiply(BigDecimal.valueOf(item.getQuantity()));
            return new CartItemViewDto(
                item.getId(),
                item.getCartId(),
                moment.id(),
                moment.title(),
                moment.thumbnail(),
                moment.price(),
                item.getQuantity(),
                item.getIsAvailable(),
                totalPrice);
          }).toList();

      BigDecimal subtotal = items.stream()
          .map(CartItemViewDto::getTotalPrice)
          .reduce(BigDecimal.ZERO, BigDecimal::add);

      CartViewDto updatedCartView = new CartViewDto(cartView.getId(), customerId, new ArrayList<>(items), subtotal);
      model.addAttribute("cartView", updatedCartView);

      return "cart_test_updateform"; // return your cart page with error messages
    }

    // No errors, proceed to update backend
    CartDto cartDto = new CartDto(cartView.getId(), customerId,
        cartView.getItems().stream()
            .map(item -> new CartItemInfoDto(
                item.getId(),
                item.getCartId(),
                item.getMomentId(),
                item.getQuantity(),
                item.getIsAvailable()
            ))
            .toList()
    );

    orderClient.updateCart(customerId, cartDto);

    return "redirect:/carts/" + customerId;
  }*/

  @GetMapping("/carts/{customerId}")
  public String showCartUpdateForm(@PathVariable Long customerId, Model model) {
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
              cartItem.id(),
              cartItem.cartId(),
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

    CartViewDto cartViewDto = new CartViewDto(cartDto.id(), customerId, new ArrayList<>(items), subtotal);
    model.addAttribute("cartView", cartViewDto);
    return "cart_update_form";
  }

  @PutMapping("/carts/{customerId}")
  public String updateCart(
      @PathVariable Long customerId,
      @Valid @ModelAttribute("cartView") CartViewDto cartView,
      BindingResult bindingResult,
      Model model) {

    log.info("cartView items START:");
    if (bindingResult.hasErrors()) {
      //TODO: add title and total price to attribute
      return "cart_update_form";
    }

    log.info("cartView items BEFORE mapping:");
    cartView.getItems().forEach(item ->
        log.info("Item - id: {}, cartId: {}, momentId: {}, quantity: {}, isAvailable: {}",
            item.getId(), item.getCartId(), item.getMomentId(), item.getQuantity(), item.getIsAvailable())
    );

    var cartItems = cartView.getItems().stream()
        .map(item -> new CartItemInfoDto(
            item.getId(),
            item.getCartId(),
            item.getMomentId(),
            item.getQuantity(),
            item.getIsAvailable()
        ))
        .toList();

    log.info("Mapped CartItemInfoDto list:");
    cartItems.forEach(item ->
        log.info("CartItemInfoDto - id: {}, cartId: {}, momentId: {}, quantity: {}, isAvailable: {}",
            item.id(), item.cartId(), item.momentId(), item.quantity(), item.isAvailable())
    );
   /* CartDto cartDto = new CartDto(cartView.getId(), customerId,
        cartView.getItems().stream()
            .map(item -> new CartItemInfoDto(
                item.getId(),
                item.getCartId(),
                item.getMomentId(),
                item.getQuantity(),
                item.getIsAvailable()
            ))
            .toList()
    );*/
    CartDto cartDto = new CartDto(cartView.getId(), customerId, cartItems);


    orderClient.updateCart(customerId, cartDto);

    return "redirect:/carts/" + customerId;
  }

}
