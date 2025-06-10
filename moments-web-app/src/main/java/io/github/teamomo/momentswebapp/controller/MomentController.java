package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.MomentClient;
import io.github.teamomo.momentswebapp.client.MomentClientPublic;
import io.github.teamomo.momentswebapp.dto.CategoryDto;
import io.github.teamomo.momentswebapp.dto.DateTimeDto;
import io.github.teamomo.momentswebapp.dto.MomentDetail;
import io.github.teamomo.momentswebapp.dto.MomentDto;
import io.github.teamomo.momentswebapp.entity.Location;
import io.github.teamomo.momentswebapp.util.CustomerManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MomentController {

  private final MomentClientPublic momentClientPublic;
  private final MomentClient momentClient;
  private final CustomerManager customerManager;


  @GetMapping("/moment/{id}")
  public String renderMoment(
      @PathVariable Long id,
      Model model
//      ,@AuthenticationPrincipal OidcUser oidcUser
  ) {
//    System.out.println(oidcUser != null ? oidcUser.getSubject() : "no user");

    log.debug("Retrieving moment for moment-details page from backend");
    MomentDto momentDto = momentClientPublic.getMomentById(id);
    log.info("Retrieved moment for moment-details page from backend: {}",
        momentDto);
    model.addAttribute("momentDto", momentDto);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    String formattedDate = momentDto.startDate().format(formatter);
    model.addAttribute("startDate", formattedDate);

    log.debug("Retrieving category for moment-details page from backend for categoryId: {}",
        momentDto.categoryId());
    CategoryDto category = momentClientPublic.getCategoryById(momentDto.categoryId());
    log.info("Retrieved category for moment-details page from backend for categoryId: {}",
        momentDto.categoryId());

    model.addAttribute("category", category);
    Integer quantity = 1;
    model.addAttribute("quantity", quantity);

    return "moment-details";
  }

  @GetMapping("/moment/add")
  public String addMomentForm(Model model, HttpServletRequest request) {
    Long customerId = customerManager.getCustomerId();
    MomentDto momentDto = MomentDto.builder()
        .momentDetails(new MomentDetail())
        .location(new Location())
        .hostId(customerId)
        .build();

    model.addAttribute("momentDto", momentDto);

    DateTimeDto dateTimeDto = DateTimeDto.from(LocalDateTime.now());
    model.addAttribute("dateTimeDto", dateTimeDto);

    // CATEGORIES retrieval from backend
    momentClientPublic.getCategories(model, request.getRequestURI());

    return "moment-details-form";
  }

  @GetMapping("/moment/update/{id}")
  public String updateMomentForm(@PathVariable Long id, Model model, HttpServletRequest request) {
    log.debug("Retrieving moment for moment-details-form page from backend");
    MomentDto momentDto = momentClientPublic.getMomentById(id);
    log.info("Retrieved moment for moment-details-form page from backend: {}",
        momentDto);
    model.addAttribute("momentDto", momentDto);

    DateTimeDto dateTimeDto = DateTimeDto.from(momentDto.startDate());
    model.addAttribute("dateTimeDto", dateTimeDto);

    // CATEGORIES retrieval from backend
    momentClientPublic.getCategories(model, request.getRequestURI());

    return "moment-details-form";
  }

  @PostMapping("/moment")
  public String addMoment(
      @ModelAttribute @Valid MomentDto momentDto,
      @ModelAttribute @Valid DateTimeDto dateTimeDto,
      BindingResult bindingResult,
      Model model) {

    if (bindingResult.hasErrors()) {
      log.warn("Validation errors occurred: {}", bindingResult.getAllErrors());
      model.addAttribute("momentDto", momentDto);
      return "moment-details-form"; // Return the form view with errors
    }

    momentDto = momentDto.withStartDate(dateTimeDto.toLocalDateTime());
    MomentDto momentDtoResponse = null;
    if (momentDto.id() == null) {
      log.debug("Post moment to backend: {}", momentDto);
      try {
        momentDtoResponse = momentClient.addMoment(momentDto);
      } catch (Exception e) {
        log.error("Failed to add moment: {}", momentDto, e);
        model.addAttribute("message", e.getMessage());
        return "moment-details-form";
      }
      log.info("Complete post moment to backend: {}", momentDtoResponse);
    } else {
      log.debug("Update moment in backend: {}", momentDto);
      try {
        momentDtoResponse = momentClient.updateMoment(momentDto.id(), momentDto);
      } catch (Exception e) {
        log.error("Failed to update moment: {}", e.getMessage(), e);
        model.addAttribute("message", e.getMessage());
        return "moment-details-form";
      }
      log.info("Complete update moment in backend: {}", momentDtoResponse);
    }

    //return "redirect:/moment/" + momentDtoResponse.id();
    return "redirect:/events";
  }
}
