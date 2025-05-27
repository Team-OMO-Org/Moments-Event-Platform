package io.github.teamomo.momentswebapp.dto;

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
    private Long customerId;
    private List<CartItemViewDto> items;
    private BigDecimal subtotal;
    }
