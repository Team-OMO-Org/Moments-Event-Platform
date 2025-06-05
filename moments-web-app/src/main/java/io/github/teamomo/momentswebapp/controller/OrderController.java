package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.BackendClient;
import io.github.teamomo.momentswebapp.client.OrderClient;
import io.github.teamomo.momentswebapp.dto.DateTimeDto;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import io.github.teamomo.momentswebapp.dto.OrderDto;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private final OrderClient orderClient;
  private final BackendClient backendClient;

  @PostMapping("/{customerId}")
  String placeOrder(@PathVariable Long customerId, Model model) {

   /* log.debug("Retrieving order for confirmation page from backend");
    OrderDto orderDto = orderClient.createOrderByCustomerId(customerId);
    model.addAttribute("orderDto", orderDto);
    log.info("Retrieved order for confirmation page from backend: {}", orderDto);
*/
    DateTimeDto dateTimeDto = DateTimeDto.from(LocalDateTime.now());
    model.addAttribute("dateTimeDto", dateTimeDto);

    return "confirmation";
  }
}
