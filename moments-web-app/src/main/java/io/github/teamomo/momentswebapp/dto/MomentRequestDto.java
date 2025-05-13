package io.github.teamomo.momentswebapp.dto;

import io.github.teamomo.momentswebapp.entity.Recurrence ;
import io.github.teamomo.momentswebapp.entity.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class MomentRequestDto {
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
