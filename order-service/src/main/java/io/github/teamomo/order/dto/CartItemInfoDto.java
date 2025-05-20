package io.github.teamomo.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemInfoDto(
    Long id,
    Long cartId,

    @NotNull(message = "Moment ID cannot be null")
    @Positive(message = "Moment ID must be positive")
    Long momentId,

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be positive")
    Integer quantity,

    Boolean isAvailable) {}
