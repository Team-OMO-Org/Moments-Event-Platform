package io.github.teamomo.momentswebapp.controller;

import io.github.teamomo.momentswebapp.dto.ContactDto;
import io.github.teamomo.momentswebapp.dto.CustomerDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ContactController {

  @Value("${email-address}")
  private String EMAIL_ADDRESS;
  private final JavaMailSender javaMailSender;

  public void sendEmail(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(to);
    message.setTo(EMAIL_ADDRESS);
    message.setSubject(subject);
    message.setText(text);
    javaMailSender.send(message);
  }

  @GetMapping("/contact")
  public String showContact(
      Model model
  ) {
    ContactDto contactDto = new ContactDto("", "", "", "");


    model.addAttribute("contactDto", contactDto);
    return "contact";
  }

  @PostMapping("/contact")
  public String Contact(
      @ModelAttribute @Valid ContactDto contactDto,
      BindingResult bindingResult,
      Model model
      , HttpServletRequest request
  ) {

    if (bindingResult.hasErrors()) {
      log.warn("Validation errors occurred: {}", bindingResult.getAllErrors());
      model.addAttribute("customerDto", contactDto);
      return "contact"; // Return the form view with errors
    }
    boolean isSuccess = false;
    try {
      sendEmail(contactDto.email(), contactDto.subject(), contactDto.message());
      isSuccess = true;
    } catch (Exception e) {
      log.error("Error sending email: {}", e.getMessage());
    }
     model.addAttribute("isSuccess", isSuccess);
    return "contact";
  }
}
