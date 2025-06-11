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
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange("/api/v1/moments")
@CircuitBreaker(name = "backend")
@Retry(name = "backend")
public interface MomentClient {

  Logger log = LoggerFactory.getLogger(MomentClient.class);

  @PostExchange
  MomentDto addMoment(@RequestBody MomentDto momentDto);

  @PutExchange("/{id}")
  MomentDto updateMoment(@PathVariable Long id, @RequestBody MomentDto momentDto);

  @DeleteExchange("/{id}")
  void deleteMoment(@PathVariable Long id) ;
}
