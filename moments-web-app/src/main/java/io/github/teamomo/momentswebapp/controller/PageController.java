package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.client.MomentClientPublic;
import io.github.teamomo.momentswebapp.dto.CityDto;
import io.github.teamomo.momentswebapp.dto.MomentRequestDto;
import io.github.teamomo.momentswebapp.dto.MomentResponseDto;
import io.github.teamomo.momentswebapp.dto.PageResponse;
import io.github.teamomo.momentswebapp.entity.Status;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PageController {

  private final MomentClientPublic momentClientPublic;

  @GetMapping("/")
  public String home() {
    return "redirect:/index";
  }

  @GetMapping("/index")
  public String renderIndex(
      MomentRequestDto momentRequestDto,
      @PageableDefault(size = 12, sort = "startDate") Pageable pageable,
      Model model,
      HttpServletRequest request
  ) {
    // Get the current request URL including query parameters, remove parameter for functionality
    addRequestUrlsToModel(model, request);

    // set default sort
    String sort = request.getParameter("sort");
    sort = sort == null || sort.equalsIgnoreCase("UNSORTED") ? "startDate,desc" : sort;
    log.debug("sort request parameter: {}", sort);

    // MOMENTS retrieval
    log.debug("Retrieving moments for index page from backend");
    PageResponse<MomentResponseDto> pageResponse = momentClientPublic.getAllMoments(
        momentRequestDto.getCategory(),
        momentRequestDto.getLocation(),
        momentRequestDto.getPriceFrom(),
        momentRequestDto.getPriceTo(),
        momentRequestDto.getStartDateFrom(),
        momentRequestDto.getStartDateTo(),
        momentRequestDto.getRecurrence(),
        Status.LIVE,
        momentRequestDto.getSearch(),
        pageable.getPageNumber(),
        pageable.getPageSize(),
        sort.replace(": ", ",")
    );
    log.info("getTotalElements {} getTotalPages {} getNumber {} getSize {} getSort {}",
        pageResponse.getTotalElements(),
        pageResponse.getTotalPages(),
        pageResponse.getNumber(), // get current page number
        pageResponse.getSize(),
        pageResponse.getSort());
    log.info("Retrieved moments for index page from backend: {}",
        pageResponse.getContent().size());

    model.addAttribute("moments", pageResponse.getContent());
    model.addAttribute("pageSize", pageResponse.getSize());
    model.addAttribute("totalPages", pageResponse.getTotalPages());
    model.addAttribute("totalElements", pageResponse.getTotalElements());
    model.addAttribute("currentPage", pageResponse.getNumber() + 1);

    // CATEGORIES retrieval from backend
    momentClientPublic.getCategories(model, request.getRequestURI());

    // CITIES retrieval
    log.debug("Retrieving cities for index page from backend");
    List<CityDto> cities = momentClientPublic.getAllCitiesByMomentsCount();
    log.info("Retrieved cities for index page from backend: {}",
        cities.size());
    model.addAttribute("cities", cities);

    return "index";
  }

  private static void addRequestUrlsToModel(Model model, HttpServletRequest request) {
    // Add the full URL to the model
    String currentUrl = request.getRequestURL().toString();
    String queryString = request.getQueryString();
    String fullUrl = queryString != null ? currentUrl + "?" + queryString : currentUrl + "?";
    model.addAttribute("currentUrl", fullUrl);
    log.debug("currentUrl: {}", fullUrl);

    // Add url without corresponding query parameter for functionality
    // PAGE AND SIZE
    String currentUrlWithoutPage = fullUrl.replaceAll("(&)?page=\\d+", "");
    model.addAttribute("currentUrlWithoutPage", currentUrlWithoutPage);
    String currentUrlWithoutSize = fullUrl.replaceAll("(&)?size=\\d+", "");
    model.addAttribute("currentUrlWithoutSize",currentUrlWithoutSize);

    // SORTING
    String currentUrlWithoutSort = fullUrl.replaceAll("(&)?sort=[a-zA-Z0-9]+(,)+(asc)?(ASC)?(desc)?(DESC)?", "");
    model.addAttribute("currentUrlWithoutSort",currentUrlWithoutSort);
    // capture sort parameter of the current request
    String sort = request.getParameter("sort");
    log.debug("sort parameter: {}", sort);
    sort = sort == null || sort.equalsIgnoreCase("UNSORTED") ? "startDate,desc" : sort;
    String sortArray[] = sort.split(",");
    String sortType = sortArray[0];
    String sortDirection = sortArray[1];
    // add to model
    model.addAttribute("sortType", sortType);
    model.addAttribute("sortDirection", sortDirection);

    // CATEGORIES
    String currentUrlWithoutCategory = fullUrl.replaceAll("(&)?category=[^&]*", "");
    model.addAttribute("currentUrlWithoutCategory",currentUrlWithoutCategory);
    String category = request.getParameter("category");
    model.addAttribute("categorySelected", category);

    // CITIES
    String currentUrlWithoutLocation = fullUrl.replaceAll("(&)?location=[^&]*", "");
    model.addAttribute("currentUrlWithoutLocation",currentUrlWithoutLocation);
    String location = request.getParameter("location");
    model.addAttribute("locationSelected", location);

    // SEARCH
    String currentUrlWithoutSearch = fullUrl.replaceAll("(&)?search=[^&]*", "");
    model.addAttribute("currentUrlWithoutSearch",currentUrlWithoutSearch);
    String search = request.getParameter("search");
    model.addAttribute("search", search);
  }

  @GetMapping("/oauth2/code/moments-web-app")
  public String afterLogin() {
    return "redirect:/index";
  }
}