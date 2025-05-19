package io.github.teamomo.order.service;

import io.github.teamomo.moment.exception.ResourceNotFoundException;
import io.github.teamomo.order.dto.CartDto;
import io.github.teamomo.order.entity.Cart;
import io.github.teamomo.order.exception.ResourceAlreadyExistsException;
import io.github.teamomo.order.mapper.OrderMapper;
import io.github.teamomo.order.repository.CartItemRepository;
import io.github.teamomo.order.repository.CartRepository;
import io.github.teamomo.order.repository.OrderItemRepository;
import io.github.teamomo.order.repository.OrderRepository;
import io.github.teamomo.order.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final PaymentRepository paymentRepository;
  private final OrderMapper orderMapper;


  public CartDto findCartByCustomerId(Long customerId) {
    Cart cart = cartRepository
        .findByCustomerId(customerId)
        .orElseGet(() -> {
          Cart newCart = new Cart();
          newCart.setCustomerId(customerId);
          return newCart;
        });
    return orderMapper.toCartDto(cart);
  }

//todo: create items
  public CartDto createCart(CartDto cartDto) {
    if(cartRepository.findByCustomerId(cartDto.customerId()).isPresent()){
      throw new ResourceAlreadyExistsException("Cart already exist with given customer ID " + cartDto.customerId());
    }
    Cart cart = orderMapper.toCartEntity(cartDto);
    Cart savedCart = cartRepository.save(cart);

    return orderMapper.toCartDto(savedCart);
  }

//todo: update items
  public CartDto updateCart(Long customerId, CartDto cartDto) {

    Cart cart = cartRepository
        .findByCustomerId(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart", "customerId", customerId.toString()));

    Cart updatedCart = orderMapper.toCartEntity(cartDto);
    updatedCart.setId(cart.getId());

    Cart savedCart = cartRepository.save(updatedCart);
    return orderMapper.toCartDto(savedCart);
  }
}
