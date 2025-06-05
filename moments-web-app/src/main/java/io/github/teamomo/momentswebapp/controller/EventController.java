package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.BackendClient;
import io.github.teamomo.momentswebapp.client.CustomerClient;
import io.github.teamomo.momentswebapp.dto.CategoryDto;
import io.github.teamomo.momentswebapp.dto.MomentDto;
import io.github.teamomo.momentswebapp.util.CustomerManager;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
  private final BackendClient backendClient;

  @GetMapping("/events")
  public String showEvents(
      Model model
      ,HttpServletRequest request
//    ,@AuthenticationPrincipal OidcUser oidcUser  // ToDo: if you need user info/keycloak user id
  ) {
//    System.out.println(oidcUser != null ? oidcUser.getSubject() : "no user");

    Long customerId = customerManager.getCustomerId();

    // CATEGORIES retrieval
    log.debug("Retrieving list of moments for customer from backend");
    List<MomentDto> moments = backendClient.getMomentsByHostId(customerId);
   // moments.stream().map()
    log.info("Retrieved list of moments for customer from backend: {}",
        moments.size());

    List<CategoryDto> categories = backendClient.getAllCategoriesByMomentsCount();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy");

    // Map categoryName to moments, formatting startDate
    List<MomentDto> updatedMoments = moments.stream()
        .map(moment -> {
          return categories.stream()
              .filter(category -> category.categoryId().equals(moment.categoryId()))
              .findFirst()
              .map(category -> moment.withCategoryName(category.categoryName()))
              .orElse(moment); // If no category is found, keep the original moment
        })
        .toList();

// Format startDate and update moments
    List<MomentDto> formattedMoments = updatedMoments.stream()
        .map(moment -> moment.withFormattedStartDate(moment.startDate().format(formatter)))
        .toList();

    log.info("FormattedStartDates for list of moments  {}",
        formattedMoments.stream()
            .map(MomentDto::formattedStartDate)
            .distinct()
            .toList());

    model.addAttribute("moments", formattedMoments);



  return "events";
  }
}
