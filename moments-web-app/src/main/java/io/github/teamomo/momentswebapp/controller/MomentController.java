package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.BackendClient;
import io.github.teamomo.momentswebapp.dto.CategoryDto;
import io.github.teamomo.momentswebapp.dto.MomentDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MomentController {

  private final BackendClient backendClient;


  @GetMapping("/moment/{id}")
  public String renderMoment(
      @PathVariable Long id,
      Model model
//      ,@AuthenticationPrincipal OidcUser oidcUser
  ) {
//    System.out.println(oidcUser != null ? oidcUser.getSubject() : "no user");

    log.debug("Retrieving moment for moment-details page from backend");
    MomentDto momentDto = backendClient.getMomentById(id);
    log.info("Retrieved moment for moment-details page from backend: {}",
        momentDto);
    model.addAttribute("momentDto", momentDto);


    return "moment-details";
  }
}
