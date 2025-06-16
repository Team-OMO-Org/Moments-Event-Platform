package io.github.teamomo.momentswebapp.dto;

import java.math.BigDecimal;

public record OrderItemInfoDto(
    Long momentId,
    int quantity,
    BigDecimal price
) {
}
