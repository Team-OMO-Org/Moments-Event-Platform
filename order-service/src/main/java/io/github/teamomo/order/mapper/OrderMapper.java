package io.github.teamomo.order.mapper;

import io.github.teamomo.order.dto.OrderDto;
import io.github.teamomo.order.dto.OrderItemDto;
import io.github.teamomo.order.entity.Order;
import io.github.teamomo.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  @Mapping(target = "orderItems", source = "orderItems")
  OrderDto toDto(Order order);

  List<OrderItemDto> toOrderItemDtos(List<OrderItem> orderItems);


  @Mapping(target = "quantity", source = "quantity")
  @Mapping(target = "price", source = "price")
  OrderItemDto toOrderItemDto(OrderItem orderItem);
}
