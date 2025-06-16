package io.github.teamomo.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CustomerDto(
    Long id,

    @NotNull(message = "KeycloakUserID cannot be null")
    String keycloakUserId,

    @Size(min = 1, max = 100, message = "Profile name must be between 1 and 100 characters")
    String profileName,

    @Email(message = "Profile email must be a valid email address")
    String profileEmail,

    @Size(max = 255, message = "Profile picture URL size must be between 1 and 255 characters")
    String profilePicture,

    @Size(max = 255, message = "Profile site URL size must be between 1 and 255 characters")
    @Pattern(regexp = "^$|^(https?://)?[\\w.-]+(?:\\.[\\w\\.-]+)+[/#?]?.*$", message = "Profile site URL must be a valid URL or empty")
    String profileSiteUrl,

    @Size(max = 500, message = "Profile description must be between 1 and 500 characters")
    String profileDescription,

    @NotNull(message = "Customer status cannot be null")
    Boolean active
) {

}