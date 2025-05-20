package io.github.teamomo.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record CartDto(
    Long id,

    @NotNull(message = "Customer ID cannot be null")
    @Positive(message = "Customer ID must be positive")
    Long customerId,

    @NotNull(message = "Cart items cannot be null")
    @Valid
    List<CartItemInfoDto> cartItems) {}
