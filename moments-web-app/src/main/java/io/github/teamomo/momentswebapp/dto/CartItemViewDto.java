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
public class CartItemViewDto{
  private Long momentId;
  private String title;
  private String thumbnail;
  private BigDecimal price;
  private Integer quantity;
  private Boolean isAvailable;
  private BigDecimal totalPrice;
}
