package io.github.teamomo.moment.dto;

import java.math.BigDecimal;

public record CartItemDto(Long momentId, BigDecimal price, Integer ticketCount) {
}