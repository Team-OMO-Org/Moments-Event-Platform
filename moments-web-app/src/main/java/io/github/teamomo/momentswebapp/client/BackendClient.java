package io.github.teamomo.momentswebapp.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.teamomo.momentswebapp.dto.CategoryDto;
import io.github.teamomo.momentswebapp.dto.CityDto;
import io.github.teamomo.momentswebapp.dto.MomentResponseDto;
import io.github.teamomo.momentswebapp.dto.PageResponse;
import io.github.teamomo.momentswebapp.entity.Recurrence;
import io.github.teamomo.momentswebapp.entity.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/v1/moments")
@CircuitBreaker(name = "backend")
@Retry(name = "backend")
public interface BackendClient {

  Logger logger = LoggerFactory.getLogger(BackendClient.class);

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
      @RequestParam(required = false) int page,
      @RequestParam(required = false, defaultValue = "12") int size,
      @RequestParam(required = false, defaultValue = "startDate") String sort
  );

  @GetExchange("/categories")
  List<CategoryDto> getAllCategoriesByMomentsCount();

  @GetExchange("/cities")
  List<CityDto> getAllCitiesByMomentsCount();

//    default boolean fallbackMethod(String skuCode, Integer quantity, Throwable t) {
//        logger.error("Can not get inventory for skuCode {}, failure reason: {}", skuCode, t
//        .getMessage());
//        return false;
//    }
}
