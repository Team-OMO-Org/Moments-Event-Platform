package io.github.teamomo.order.dto;

import io.github.teamomo.order.entity.Order;

import java.math.BigDecimal;

public record OrderItemDto(int quantity, BigDecimal price) {}
