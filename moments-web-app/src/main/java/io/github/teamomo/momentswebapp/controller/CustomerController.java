package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.MomentClientPublic;
import io.github.teamomo.momentswebapp.client.CustomerClient;
import io.github.teamomo.momentswebapp.dto.CustomerDto;
import io.github.teamomo.momentswebapp.util.CustomerManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

  private final CustomerManager customerManager;
  private final CustomerClient customerClient;
  private final MomentClientPublic momentClientPublic;

  @GetMapping("/customerId")
  @ResponseBody
  public String getCustomerId() {

    log.debug("Checking customerId for current Keycloak user from backend or cookie");
    Long customerId = customerManager.getCustomerId();
    log.info("Checked customerId for current Keycloak user from backend or cookie: {}",
        customerId);
    return String.format("CustomerId for current Keycloak user is %d", customerId);
  }


  @GetMapping("/profile")
  public String showProfile(
      Model model
//      ,@AuthenticationPrincipal OidcUser oidcUser  // ToDo: if you need user info/keycloak user id
  ) {
//    System.out.println(oidcUser != null ? oidcUser.getSubject() : "no user");

    Long customerId = customerManager.getCustomerId();

    log.debug("Retrieving customer info for customer profile page from backend");
    CustomerDto customerDto = customerClient.getCustomerById(customerId);
    log.info("Customer info for customer profile page from backend: {}",
        customerDto);
    model.addAttribute("customerDto", customerDto);

  return "profile";
  }

  @GetMapping("/profile/update/{id}")
  public String updateProfileForm(@PathVariable Long id, Model model) {

    log.debug("Retrieving customer info for customer edit profile page from backend");
    CustomerDto customerDto = customerClient.getCustomerById(id);
    log.info("Customer info for customer edit profile page from backend: {}",  customerDto);
    model.addAttribute("customerDto", customerDto);

    return "profile-form";
  }

  @PostMapping("/profile")
  public String updateProfile(
      @ModelAttribute @Valid CustomerDto customerDto,
      BindingResult bindingResult,
      Model model) {

    if (bindingResult.hasErrors()) {
      log.warn("Validation errors occurred: {}", bindingResult.getAllErrors());
      model.addAttribute("customerDto", customerDto);
      return "profile-form"; // Return the form view with errors
    }

    CustomerDto responseDto = null;
      log.debug("Update profile in backend: {}", customerDto);
      try {
        responseDto = customerClient.updateCustomer(customerDto.id(), customerDto);
      } catch (Exception e) {
        log.error("Failed to update profile: {}", e.getMessage(), e);
        model.addAttribute("message", e.getMessage());
        return "profile-form";
      }
      log.info("Complete update moment in backend: {}", responseDto);


    return "redirect:/profile";

  }
}
