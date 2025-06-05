package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.MomentClientPublic;
import io.github.teamomo.momentswebapp.client.CustomerClient;
import io.github.teamomo.momentswebapp.dto.MomentDto;
import io.github.teamomo.momentswebapp.util.CustomerManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EventController {

  private final CustomerManager customerManager;
  private final CustomerClient customerClient;
  private final MomentClientPublic momentClientPublic;

  @GetMapping("/events")
  public String showEvents(
      Model model
//      ,@AuthenticationPrincipal OidcUser oidcUser  // ToDo: if you need user info/keycloak user id
  ) {
//    System.out.println(oidcUser != null ? oidcUser.getSubject() : "no user");

    Long customerId = customerManager.getCustomerId();

    // CATEGORIES retrieval
    log.debug("Retrieving list of moments for customer from backend");
    List<MomentDto> moments = momentClientPublic.getMomentsByHostId(customerId);
   // moments.stream().map()
    log.info("Retrieved list of moments for customer from backend: {}",
        moments.size());
    model.addAttribute("moments", moments);

  return "events";
  }
}
