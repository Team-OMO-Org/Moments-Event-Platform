package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.BackendClient;
import io.github.teamomo.momentswebapp.dto.MomentRequestDto;
import io.github.teamomo.momentswebapp.dto.MomentResponseDto;
import io.github.teamomo.momentswebapp.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PageController {

  private final BackendClient backendClient;

  @GetMapping("/")
  public String home() {
    return "redirect:/index";
  }

  @GetMapping("/index")
  public String renderIndex(
      MomentRequestDto momentRequestDto,
      @PageableDefault(size = 12, sort = "startDate") Pageable pageable,
      Model model
//      ,@AuthenticationPrincipal OidcUser oidcUser  // if you need user info/keycloak user id
  ) {
//    System.out.println(oidcUser != null ? oidcUser.getSubject() : "no user");

    log.debug("Retrieving moments for index page from backend");
    PageResponse<MomentResponseDto> pageResponse = backendClient.getAllMoments(
        momentRequestDto.getCategory(),
        momentRequestDto.getLocation(),
        momentRequestDto.getPriceFrom(),
        momentRequestDto.getPriceTo(),
        momentRequestDto.getStartDateFrom(),
        momentRequestDto.getStartDateTo(),
        momentRequestDto.getRecurrence(),
        momentRequestDto.getStatus(),
        pageable.getPageNumber(),
        pageable.getPageSize(),
        pageable.getSort().toString()
    );
    log.info("Retrieved moments for index page from backend: {}",
        pageResponse.getContent().size());

    model.addAttribute("moments", pageResponse.getContent());

    return "index";
  }
}