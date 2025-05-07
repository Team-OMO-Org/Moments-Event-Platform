package io.github.teamomo.moment.dto;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Data;

@Data
public class MomentFilterResponseDto {
  private Long id;
  private String title;
  private String category;
  private String location;
  private BigDecimal price;
  private Instant startDate;
  private String recurrence;
  private String status;
  private String description;
}
