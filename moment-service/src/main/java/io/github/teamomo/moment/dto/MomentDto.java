package io.github.teamomo.moment.dto;

import io.github.teamomo.moment.entity.Recurrence;
import io.github.teamomo.moment.entity.Status;
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
    Recurrence recurrence,
    BigDecimal price,
    Status status,
    Integer ticketCount
) {

}
