package io.github.teamomo.order.dto;

import io.github.teamomo.order.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderInfoDto(
    Long id,
    OrderStatus orderStatus,
    BigDecimal totalPrice,
    List<OrderItemInfoDto> orderItems
) {}
