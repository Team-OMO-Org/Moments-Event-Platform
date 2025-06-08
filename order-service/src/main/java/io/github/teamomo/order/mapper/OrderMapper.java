package io.github.teamomo.order.mapper;

import io.github.teamomo.order.dto.CartDto;
import io.github.teamomo.order.dto.CartItemInfoDto;
import io.github.teamomo.order.dto.OrderDto;
import io.github.teamomo.order.dto.OrderInfoDto;
import io.github.teamomo.order.dto.OrderItemDto;
import io.github.teamomo.order.dto.OrderItemInfoDto;
import io.github.teamomo.order.entity.Cart;
import io.github.teamomo.order.entity.CartItem;
import io.github.teamomo.order.entity.Order;
import io.github.teamomo.order.entity.OrderItem;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  // Order mappings
  @Mapping(target = "orderItems", source = "orderItems")
  OrderDto toDto(Order order);

  List<OrderItemDto> toOrderItemDtos(List<OrderItem> orderItems);

  @Mapping(target = "quantity", source = "quantity")
  @Mapping(target = "price", source = "price")
  OrderItemDto toOrderItemDto(OrderItem orderItem);


  OrderInfoDto toOrderInfoDto(Order order);

  List<OrderItemInfoDto> toOrderItemInfoDtos(List<OrderItem> orderItems);

  OrderItemInfoDto toOrderItemInfoDto(OrderItem orderItem);

  // Cart mappings
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

