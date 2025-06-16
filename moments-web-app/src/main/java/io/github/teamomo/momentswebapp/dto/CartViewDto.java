package io.github.teamomo.momentswebapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartViewDto{
    private Long id;
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    @Valid
    private List<CartItemViewDto> items;
    private BigDecimal subtotal;
    }
