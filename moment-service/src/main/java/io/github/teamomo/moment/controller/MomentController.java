package io.github.teamomo.moment.controller;

import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.dto.MomentFilterRequestDto;
import io.github.teamomo.moment.dto.MomentFilterResponseDto;
import io.github.teamomo.moment.entity.Moment;
import io.github.teamomo.moment.service.MomentService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/moments")
@RequiredArgsConstructor
public class MomentController {

  private final MomentService momentService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<Moment> getAllMoments(@RequestParam(required = false) Instant startDate,
      Pageable pageable) {
    if (startDate == null) {
      startDate = Instant.now();
    }
    return momentService.getAllMoments(startDate, pageable);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createMoment(@Valid @RequestBody MomentDto momentDto) {
    momentService.createMoment(momentDto);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MomentDto getMomentById(@PathVariable Long id) {
    return momentService.getMomentById(id);
  }

  @GetMapping("/filtered")
  @ResponseStatus(HttpStatus.OK)
  public Page<MomentFilterResponseDto> getAllFilteredMoments(
      MomentFilterRequestDto momentFilterRequestDto,
      @PageableDefault(size = 10, sort = "startDate") Pageable pageable
  ) {
    return momentService.getAllMoments(momentFilterRequestDto, pageable);
  }
}
