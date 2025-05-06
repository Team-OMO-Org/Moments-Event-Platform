package io.github.teamomo.moment.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record MomentDto(
    Long id,

    @NotNull(message = "Host ID cannot be null")
    @Positive(message = "Host ID must be positive")
    Long hostId,

    @NotNull(message = "Category ID cannot be null")
    @Positive(message = "Category ID must be positive")
    Long categoryId,

    @NotNull(message = "Location ID cannot be null")
    @Positive(message = "Location ID must be positive")
    Long locationId,

    @NotNull(message = "Title cannot be null")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    String title,

    @Size(min = 1, max = 16777216, message = "Thumbnail size must be between 1 byte and 16 MB")
    byte[] thumbnail,

    @NotNull(message = "Start date cannot be null")
    Instant startDate,

    @NotNull(message = "Recurrence cannot be null")
    @Pattern(regexp = "ONETIME|REGULAR", message = "Recurrence must be either 'ONETIME' or 'REGULAR'")
    String recurrence,

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    BigDecimal price,

    @NotNull(message = "Status cannot be null")
    @Pattern(regexp = "DRAFT|LIVE|PAUSED", message = "Status must be one of 'DRAFT', 'LIVE', or 'PAUSED'")
    String status,

    @NotNull(message = "Ticket count cannot be null")
    @PositiveOrZero(message = "Ticket count must be positive or zero")
    Integer ticketCount
) {

}