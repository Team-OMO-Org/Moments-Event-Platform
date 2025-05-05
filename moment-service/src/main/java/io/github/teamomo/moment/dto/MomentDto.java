package io.github.teamomo.moment.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record MomentDto(
    Long id,
    Long hostId,
    Long categoryId,
    Long locationId,
    String title,
    byte[] thumbnail,
    Instant startDate,
    String recurrence,
    BigDecimal price,
    String status,
    Integer ticketCount
) {

}
