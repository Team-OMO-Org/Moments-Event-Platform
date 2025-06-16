package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.MomentClient;
import io.github.teamomo.momentswebapp.client.MomentClientPublic;
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
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClientException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EventController {

  private final CustomerManager customerManager;
  private final MomentClientPublic momentClientPublic;
  private final MomentClient momentClient;

  @GetMapping("/events")
  public String showEvents(
      Model model
      ,HttpServletRequest request
//    ,@AuthenticationPrincipal OidcUser oidcUser  // ToDo: if you need user info/keycloak user id
  ) {
//    System.out.println(oidcUser != null ? oidcUser.getSubject() : "no user");

    Long customerId = customerManager.getCustomerId();
    List<MomentDto> moments = null;
    try{
      // CATEGORIES retrieval
      log.debug("Retrieving list of moments for customer from backend");
      moments = momentClientPublic.getMomentsByHostId(customerId);
      // moments.stream().map()
      log.info("Retrieved list of moments for customer from backend: {}",
          moments.size());

      List<CategoryDto> categories = momentClientPublic.getAllCategoriesByMomentsCount();

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
    }
    catch (RestClientException e) {
      log.error("Failed to retrieve moments from backend", e);
      model.addAttribute("message", "Failed to retrieve events. Please try again later.");
    }

    if (moments == null || moments.isEmpty()) {
      log.info("No moments found for customer with ID: {}", customerId);
      model.addAttribute("moments", List.of()); // Add an empty list to avoid null pointer exceptions
      model.addAttribute("message", "No events found.");
    }

    return "events";
  }

  @GetMapping("/events/delete/{id}")
  public String deleteMoment(@PathVariable Long id, Model model) {
    try {
      log.debug("Deleting moment in the backend");
      momentClient.deleteMoment(id);
      log.info("Moment deleted in the backend");
      //  model.addAttribute("message", "Event cannot be deleted. If you want to cancel it, please change its status to CANCELLED.");
    }
    catch(Exception e){
      log.error("Failed to delete moment: {}", id, e);
      model.addAttribute("message", e.getMessage());
      //  return "events";
    }
    return "redirect:/events";

  }
}