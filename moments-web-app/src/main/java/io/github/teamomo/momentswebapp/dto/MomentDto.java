package io.github.teamomo.momentswebapp.dto;

import io.github.teamomo.momentswebapp.entity.Recurrence;
import io.github.teamomo.momentswebapp.entity.Status;
import io.github.teamomo.momentswebapp.entity.Location;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MomentDto(
    // ToDo: rename to MomentDetailsResponseDto, also need request Dtp,
    //  change fields to be able to post a moment and read all MomentDetails
    Long id,

    @NotNull(message = "Host ID cannot be null")
    @Positive(message = "Host ID must be positive")
    Long hostId,

    @NotNull(message = "Category ID cannot be null")
    @Positive(message = "Category ID must be positive")
    Long categoryId,

    Location location,

    @NotNull(message = "Title cannot be null")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    String title,

    @Size(min = 1, max = 255, message = "Short description must be between 1 and 255 characters")
    String shortDescription,

    @Size(min = 1, max = 255, message = "Thumbnail URL size must be between 1 and 255 characters")
    String thumbnail,

    @NotNull(message = "Start date cannot be null")
    LocalDateTime startDate,

    @NotNull(message = "Recurrence cannot be null")
    @Pattern(regexp = "ONETIME|REGULAR", message = "Recurrence must be either 'ONETIME' or 'REGULAR'")
    Recurrence recurrence,

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    BigDecimal price,

    @NotNull(message = "Status cannot be null")
    @Pattern(regexp = "DRAFT|LIVE|PAUSED", message = "Status must be one of 'DRAFT', 'LIVE', or 'PAUSED'")
    Status status,

    @NotNull(message = "Ticket count cannot be null")
    @PositiveOrZero(message = "Ticket count must be positive or zero")
    Integer ticketCount
) {

}