package io.github.teamomo.moment.dto;

import io.github.teamomo.moment.entity.Recurrence;
import io.github.teamomo.moment.entity.Status;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MomentResponseDto {
  private Long id;
  private String title;
  private String category;
  private String location;  // city
  private BigDecimal price;
  private LocalDateTime startDate;
  private Recurrence recurrence;
  private Status status;
  private String shortDescription;
}