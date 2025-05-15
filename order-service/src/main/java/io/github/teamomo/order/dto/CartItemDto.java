package io.github.teamomo.order.dto;

import java.math.BigDecimal;

public record CartItemDto(Long momentId, BigDecimal price, Integer ticketCount) {
}