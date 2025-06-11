package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.MomentClientPublic;
import io.github.teamomo.momentswebapp.client.OrderClient;
import io.github.teamomo.momentswebapp.dto.CartDto;
import io.github.teamomo.momentswebapp.dto.CartItemInfoDto;
import io.github.teamomo.momentswebapp.dto.CartItemViewDto;
import io.github.teamomo.momentswebapp.dto.CartViewDto;
import io.github.teamomo.momentswebapp.dto.MomentDto;
import io.github.teamomo.momentswebapp.util.CustomerManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CartController {

  private final OrderClient orderClient;
  private final CustomerManager customerManager;
  private final MomentClientPublic momentClientPublic;

  @GetMapping("/clear-cart")
  public String renderClearCartPage() {
    return "clear-cart";
  }


  @DeleteMapping("/carts/{customerId}")
  public String clearCart(@PathVariable Long customerId) {
    log.debug("Deleting cart for customerId from backend");
    orderClient.deleteCart(customerId);
    log.info("Deleted cart for customer ID: {}", customerId);
    return "redirect:/clear-cart";
  }

  @GetMapping("/carts")
  public String showCartUpdateForm(Model model) {

    Long customerId = customerManager.getCustomerId();
    CartViewDto cartViewDto = getCartViewDto(customerId);
    model.addAttribute("cartView", cartViewDto);
    model.addAttribute("customerId", customerId);
    return "cart";
  }

  @PutMapping("/carts/{customerId}")
  public String updateCart(
      @PathVariable Long customerId,
      @Valid @ModelAttribute("cartView") CartViewDto cartView,
      BindingResult bindingResult,
      @RequestParam(required = false) String action,
      Model model) {

    if (action != null && action.startsWith("delete-")) {
      Long itemId = Long.valueOf(action.substring("delete-".length()));
      orderClient.deleteCartItem(customerId, itemId);
      return "redirect:/carts";
    }

    log.info("cartView items START:");
    if (bindingResult.hasErrors()) {

      log.warn("Validation errors occurred, restoring moment data for display");

      List<CartItemViewDto> updatedItems = new ArrayList<>();

      for (CartItemViewDto item : cartView.getItems()) {
        MomentDto moment = momentClientPublic.getMomentById(item.getMomentId());

        BigDecimal totalPrice = BigDecimal.ZERO;

        Integer quantity = item.getQuantity();
        if (quantity != null && quantity > 0) {
          totalPrice = moment.price().multiply(BigDecimal.valueOf(quantity));
        }

        updatedItems.add(
            new CartItemViewDto(
                item.getId(),
                item.getCartId(),
                moment.id(),
                moment.title(),
                moment.thumbnail(),
                moment.price(),
                item.getQuantity(),
                item.getIsAvailable(),
                totalPrice));
      }

      BigDecimal subtotal =
          updatedItems.stream()
              .map(CartItemViewDto::getTotalPrice)
              .reduce(BigDecimal.ZERO, BigDecimal::add);

      cartView.setItems(updatedItems);
      cartView.setSubtotal(subtotal);

      model.addAttribute("cartView", cartView);
      model.addAttribute("customerId", customerId);
      return "cart";
    }

    log.info("cartView items BEFORE mapping:");
    List<CartItemInfoDto> cartItems =
        cartView.getItems().stream()
            .map(
                item ->
                    new CartItemInfoDto(
                        item.getId(),
                        item.getCartId(),
                        item.getMomentId(),
                        item.getQuantity(),
                        item.getIsAvailable()))
            .toList();

    CartDto cartDto = new CartDto(cartView.getId(), customerId, cartItems);

    orderClient.updateCart(customerId, cartDto);

    return "redirect:/carts";
  }

  @GetMapping("/carts/{customerId}/checkout")
  public String checkout(@PathVariable Long customerId, Model model) {

    CartViewDto cartViewDto = getCartViewDto(customerId);
    model.addAttribute("cartView", cartViewDto);
    return "order_checkout";
  }

  private CartViewDto getCartViewDto(Long customerId) {

    log.debug("Retrieving cart for cart page from backend");
    CartDto cartDto = orderClient.getCartByCustomerId(customerId);
    log.info("Retrieved cart for cart page from backend: {}", cartDto);

    log.debug("Retrieving list of items for cart page from backend");
    List<CartItemViewDto> items =
        cartDto.cartItems().stream()
            .map(
                cartItem -> {
                  log.debug("Retrieving moment for cart page from backend");
                  MomentDto moment = momentClientPublic.getMomentById(cartItem.momentId());
                  log.info("Retrieved moment for cart page from backend: {}", moment);
                  BigDecimal totalPrice =
                      moment.price().multiply(BigDecimal.valueOf(cartItem.quantity()));
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
                })
            .toList();
    log.info("Retrieved list of items for cart page from backend: {}", items);

    BigDecimal subtotal =
        items.stream().map(CartItemViewDto::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

    return new CartViewDto(cartDto.id(), customerId, new ArrayList<>(items), subtotal);
  }

  @GetMapping("/carts/item")
  public String addCartItem(@RequestParam Long momentId, @RequestParam Integer quantity) {
    log.info("Retrieved request param momentId: {}", momentId);
    log.info("Retrieved request param quantity: {}", quantity);

    // todo: add validation of quantity
    Long customerId = customerManager.getCustomerId();
    CartItemInfoDto cartItem = new CartItemInfoDto(null, null, momentId, quantity, true);
    log.info("Retrieving saved cartItem for customerId: {}", customerId);
    orderClient.createCartItem(customerId, cartItem);
    log.info("Retrieved saved cartItem for customerId: {}", customerId);
    return "redirect:/carts";
  }
}
