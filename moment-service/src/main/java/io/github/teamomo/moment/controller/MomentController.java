package io.github.teamomo.moment.controller;

import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.service.MomentService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @GetMapping("/home")
  public Map<String, String> home() {
    return Map.of("Home", "damn right");
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<MomentDto> getAllMoments() {
    return momentService.getAllMoments();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createMoment(@RequestBody MomentDto momentDto) {
    momentService.createMoment(momentDto);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MomentDto getMomentById(@PathVariable Long id) {
    return momentService.getMomentById(id);
  }

}
