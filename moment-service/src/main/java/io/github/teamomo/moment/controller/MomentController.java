package io.github.teamomo.moment.controller;

import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.dto.MomentRequestDto;
import io.github.teamomo.moment.dto.MomentResponseDto;
import io.github.teamomo.moment.entity.Moment;
import io.github.teamomo.moment.service.MomentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/moments")
@RequiredArgsConstructor
public class MomentController {

  private final MomentService momentService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<MomentResponseDto> getAllMoments(
      MomentRequestDto momentRequestDto,
      @PageableDefault(size = 12, sort = "startDate") Pageable pageable
  ) {
    // ToDo: Default filtering: LIVE, FUTURE
    return momentService.getAllMoments(momentRequestDto, pageable);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MomentDto createMoment(@Valid @RequestBody MomentDto momentDto) {
    return momentService.createMoment(momentDto);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MomentDto getMomentById(@PathVariable Long id) {
    return momentService.getMomentById(id);
  }
}
