package io.github.teamomo.order.mapper;

import io.github.teamomo.order.dto.CartDto;
import io.github.teamomo.order.dto.CartItemDto;
import io.github.teamomo.order.entity.Cart;
import io.github.teamomo.order.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {


  CartDto toCartDto(Cart cart);
  Cart toCartEntity(CartDto cartDto);

  @Mapping(target = "cart", ignore = true)
  CartItemDto toCartItemDto(CartItem cartItem);

  @Mapping(target = "cart", ignore = true)
  CartItem toCartItemEntity(CartItemDto cartItemDto);

}
