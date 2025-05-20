package io.github.teamomo.order.mapper;

import io.github.teamomo.order.dto.CartDto;
import io.github.teamomo.order.dto.CartItemDto;
import io.github.teamomo.order.dto.CartItemInfoDto;
import io.github.teamomo.order.entity.Cart;
import io.github.teamomo.order.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderMapper {


  CartDto toCartDto(Cart cart);

  Cart toCartEntity(CartDto cartDto);

  @Mapping(target = "cartId", source = "cart.id")
  CartItemInfoDto toCartItemInfoDto(CartItem cartItem);

  @Mapping(target = "cart", source = "cartId", qualifiedByName = "mapCart")
  CartItem toCartItemEntity(CartItemInfoDto cartItemInfoDto);

  @Named("mapCart")
  default Cart mapCart(Long cartId) {
    if (cartId == null) {
      return null;
    }
    Cart cart = new Cart();
    cart.setId(cartId);
    return cart;
  }

}
