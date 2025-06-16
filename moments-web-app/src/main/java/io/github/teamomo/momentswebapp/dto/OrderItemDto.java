package io.github.teamomo.momentswebapp.dto;


import java.math.BigDecimal;

public record OrderItemDto(int quantity, BigDecimal price) {}
