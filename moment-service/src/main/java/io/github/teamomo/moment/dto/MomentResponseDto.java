package io.github.teamomo.moment.dto;

import io.github.teamomo.moment.entity.Recurrence;
import io.github.teamomo.moment.entity.Status;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Data;

public record MomentResponseDto(
    Long id,
    String title,
    String category,
    String location,  // city
    BigDecimal price,
    LocalDateTime startDate,
    Recurrence recurrence,
    Status status,
    String shortDescription
) {}