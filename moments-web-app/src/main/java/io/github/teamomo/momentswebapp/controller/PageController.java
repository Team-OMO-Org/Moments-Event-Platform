package io.github.teamomo.momentswebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

  @GetMapping("/{page}")
  public String renderPage(@PathVariable String page) {
    return page; // Maps to `templates/{page}.html`
  }
}