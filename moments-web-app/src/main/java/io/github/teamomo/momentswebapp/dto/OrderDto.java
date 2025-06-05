package io.github.teamomo.momentswebapp.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto(
    Long id,
    Long customerId,
    OrderStatus orderStatus,
    BigDecimal totalPrice,
    List<OrderItemDto> orderItems
) {}
