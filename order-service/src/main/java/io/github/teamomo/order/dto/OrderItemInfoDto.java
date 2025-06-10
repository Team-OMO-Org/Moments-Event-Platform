package io.github.teamomo.order.dto;

import java.math.BigDecimal;

public record OrderItemInfoDto(
    Long momentId,
    int quantity,
    BigDecimal price
) {
}
