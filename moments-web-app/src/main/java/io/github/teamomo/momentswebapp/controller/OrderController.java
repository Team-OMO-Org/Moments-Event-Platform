package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.MomentClientPublic;
import io.github.teamomo.momentswebapp.client.OrderClient;
import io.github.teamomo.momentswebapp.dto.CartDto;
import io.github.teamomo.momentswebapp.dto.CartItemViewDto;
import io.github.teamomo.momentswebapp.dto.CartViewDto;
import io.github.teamomo.momentswebapp.dto.DateTimeDto;
import io.github.teamomo.momentswebapp.dto.MomentDto;
import io.github.teamomo.momentswebapp.dto.OrderInfoDto;
import io.github.teamomo.momentswebapp.dto.OrderItemDto;
import io.github.teamomo.momentswebapp.dto.OrderItemViewDto;
import io.github.teamomo.momentswebapp.dto.OrderStatus;
import io.github.teamomo.momentswebapp.dto.OrderViewDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import io.github.teamomo.momentswebapp.dto.OrderDto;
import org.springframework.web.client.RestClientException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private final OrderClient orderClient;
  private final MomentClientPublic momentClientPublic;

  @PostMapping("/{customerId}")
  String placeOrder(@PathVariable Long customerId, Model model) {

    try{
    log.debug("Retrieving order for confirmation page from backend");
    OrderDto orderDto = orderClient.createOrderByCustomerId(customerId);
    model.addAttribute("orderDto", orderDto);
    log.info("Retrieved order for confirmation page from backend: {}", orderDto);
    /*DateTimeDto dateTimeDto = DateTimeDto.from(LocalDateTime.now());
    model.addAttribute("dateTimeDto", dateTimeDto);*/

    return "redirect:/orders/"+ orderDto.id() + "/confirmation";
    }catch(RestClientException e){
      log.error("Order client error during order placement: {}", e.getMessage(), e);
      model.addAttribute("error", "Sorry, something went wrong. Please try again.");
      model.addAttribute("customerId", customerId);
      return "order_error";
    }catch(Exception e){
      log.debug("Exception occurs in ordering process {}", e.getMessage());
      model.addAttribute("error", "Order service is temporarily unavailable. Please try again.");
      model.addAttribute("customerId", customerId);
      return "order_error";
    }
  }

  @GetMapping("/orders/{orderId}/confirmation")
  String renderConfirmation(@PathVariable Long orderId, Model model){

    OrderInfoDto orderInfoDto = orderClient.getOrderById(orderId);
    if(orderInfoDto.orderStatus().equals(OrderStatus.CANCELLED)){
      return "blog";
    }

    OrderViewDto orderViewDto = toOrderViewDto(orderInfoDto);
    model.addAttribute("orderView", orderViewDto);
    DateTimeDto dateTimeDto = DateTimeDto.from(LocalDateTime.now());
    model.addAttribute("dateTimeDto", dateTimeDto);


    return "confirmation";
  }

  private OrderViewDto toOrderViewDto(OrderInfoDto orderInfoDto) {

    log.debug("Retrieving list of order items for confirmation page from backend");
    List<OrderItemViewDto> items = orderInfoDto.orderItems().stream()
        .map(orderItem -> {
          log.debug("Retrieving moment for confirmation page from backend");
          MomentDto moment = momentClientPublic.getMomentById(orderItem.momentId());
          log.info("Retrieved moment for cart page from backend: {}",
              moment);
          BigDecimal totalPrice = orderItem.price().multiply(BigDecimal.valueOf(orderItem.quantity()));
          return new OrderItemViewDto(
              moment.title(),
              orderItem.quantity(),
              totalPrice);
        }).toList();
    log.info("Retrieved list of order items for confirmation page from backend: {}",
        items);

    return new OrderViewDto(orderInfoDto.id(),
        orderInfoDto.orderStatus(),
        orderInfoDto.totalPrice(),
        new ArrayList<>(items));
  }
}
