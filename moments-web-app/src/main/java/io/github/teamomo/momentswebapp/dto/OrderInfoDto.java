package io.github.teamomo.momentswebapp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderInfoDto(
    Long id,
    OrderStatus orderStatus,
    BigDecimal totalPrice,
    List<OrderItemInfoDto> orderItems
) {}
