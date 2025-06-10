package io.github.teamomo.momentswebapp.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.teamomo.momentswebapp.dto.CategoryDto;
import io.github.teamomo.momentswebapp.dto.CityDto;
import io.github.teamomo.momentswebapp.dto.MomentDto;
import io.github.teamomo.momentswebapp.dto.MomentResponseDto;
import io.github.teamomo.momentswebapp.dto.PageResponse;
import io.github.teamomo.momentswebapp.entity.Recurrence;
import io.github.teamomo.momentswebapp.entity.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange("/api/v1/moments")
@CircuitBreaker(name = "backend")
@Retry(name = "backend")
public interface MomentClientPublic {

  Logger log = LoggerFactory.getLogger(MomentClientPublic.class);

  @GetExchange
  PageResponse<MomentResponseDto> getAllMoments(
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String location,
      @RequestParam(required = false) BigDecimal priceFrom,
      @RequestParam(required = false) BigDecimal priceTo,
      @RequestParam(required = false) LocalDateTime startDateFrom,
      @RequestParam(required = false) LocalDateTime startDateTo,
      @RequestParam(required = false) Recurrence recurrence,
      @RequestParam(required = false) Status status,
      @RequestParam(required = false) String search,
      @RequestParam(required = false) int page,
      @RequestParam(required = false, defaultValue = "12") int size,
      @RequestParam(required = false, defaultValue = "startDate") String sort
  );

  @GetExchange("/host/{id}")
  List<MomentDto> getMomentsByHostId(@PathVariable Long id);

  @GetExchange("/categories")
  List<CategoryDto> getAllCategoriesByMomentsCount();

  @GetExchange("/cities")
  List<CityDto> getAllCitiesByMomentsCount();

  @GetExchange("/{id}")
  MomentDto getMomentById(@PathVariable Long id);

  @GetExchange("/categories/{id}")
  CategoryDto getCategoryById(@PathVariable Long id);

  /**
   * Retrieves categories from the backend and adds them to the model.
   *
   * @param model the model to which categories will be added
   */
  default void getCategories(Model model, String endpoint) {
    log.debug("Retrieving categories from backend for endpoint: {}", endpoint);
    List<CategoryDto> categories = getAllCategoriesByMomentsCount();
    log.info("Retrieved categories from backend for endpoint {}: {}", endpoint, categories.size());
    model.addAttribute("categories", categories);
  }

//    default boolean fallbackMethod(String skuCode, Integer quantity, Throwable t) {
//        logger.error("Can not get inventory for skuCode {}, failure reason: {}", skuCode, t
//        .getMessage());
//        return false;
//    }
}
