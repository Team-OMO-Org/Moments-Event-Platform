package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.CustomerClient;
import io.github.teamomo.momentswebapp.dto.CategoryDto;
import io.github.teamomo.momentswebapp.dto.MomentDto;
import io.github.teamomo.momentswebapp.util.CustomerManager;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

  private final CustomerManager customerManager;
  private final CustomerClient customerClient;

  @GetMapping("/customerId")
  @ResponseBody
  public String getCustomerId() {

    log.debug("Checking customerId for moment-details page from backend");
    Long customerId = customerManager.getCustomerId();
    log.info("Checked customerId for moment-details page from backend: {}",
        customerId);
    return String.format("CustomerId for current Keycloak user is %d", customerId);
  }
}
