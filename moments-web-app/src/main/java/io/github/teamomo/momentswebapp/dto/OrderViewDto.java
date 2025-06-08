package io.github.teamomo.momentswebapp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderViewDto {
  private Long id;
  private OrderStatus status;
  private BigDecimal total;
  private List<OrderItemViewDto> orderItems;
}
