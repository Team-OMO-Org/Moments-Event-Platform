package io.github.teamomo.moment.dto;

import io.github.teamomo.moment.entity.Recurrence;
import io.github.teamomo.moment.entity.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * MomentRequestDto is a Data Transfer Object (DTO) used for transferring moment-related data
 * between the client and server.
 *
 * <p>This DTO contains various fields that can be used to filter or search for moments based on
 * different criteria.
 */
public record MomentRequestDto(
    String category,
    String location,
    BigDecimal priceFrom,
    BigDecimal priceTo,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateFrom,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTo,
    Recurrence recurrence,
    Status status,
    String search
) {}