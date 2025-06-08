package io.github.teamomo.momentswebapp.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemViewDto {
  private String momentTitle;
  private int quantity;
  private BigDecimal totalPrice;
}
