package io.github.teamomo.momentswebapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ContactDto(
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    String name,

    @Email(message = "Profile email must be a valid email address")
    String email,

    @Size(max = 100, message = "Subject must be between 1 and 100 characters")
    String subject,

    @Size(max = 500, message = "Message must be between 1 and 500 characters")
    String message

) {

}