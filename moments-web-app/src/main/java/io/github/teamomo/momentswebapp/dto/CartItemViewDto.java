package io.github.teamomo.momentswebapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
  private Long id;
  private Long cartId;
  private Long momentId;
  private String title;
  private String thumbnail;
  private BigDecimal price;
  @NotNull(message = "Quantity cannot be empty")
  @Positive(message = "Quantity must be positive")
  private Integer quantity;
  private Boolean isAvailable;
  private BigDecimal totalPrice;
}
