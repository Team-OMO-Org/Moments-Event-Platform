package io.github.teamomo.order.dto;

import io.github.teamomo.order.entity.OrderStatus;
import java.math.BigDecimal;
import java.util.List;

public record OrderDto(
    Long id,
    Long customerId,
    OrderStatus orderStatus,
    BigDecimal totalPrice,
    List<OrderItemDto> orderItems
) {}
