package io.github.teamomo.moment.dto;

import io.github.teamomo.moment.entity.Recurrence;
import io.github.teamomo.moment.entity.Status;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class MomentFilterRequestDto {
    private String category;
    private String location;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDateFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDateTo;
    private Recurrence recurrence;
    private Status status;
    private String search;
}
