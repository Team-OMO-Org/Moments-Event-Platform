package io.github.teamomo.moment.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/moments")
@RequiredArgsConstructor
public class MomentController {

  @GetMapping
  public Map<String, String> home() {
    return Map.of("Home", "damn right");
  }
}
