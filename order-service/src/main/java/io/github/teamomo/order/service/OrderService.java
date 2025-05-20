package io.github.teamomo.order.service;

import io.github.teamomo.moment.exception.ResourceNotFoundException;
import io.github.teamomo.order.client.MomentClient;
import io.github.teamomo.order.dto.CartDto;
import io.github.teamomo.order.dto.CartItemDto;
import io.github.teamomo.order.dto.CartItemInfoDto;
import io.github.teamomo.order.entity.Cart;
import io.github.teamomo.order.entity.CartItem;
import io.github.teamomo.order.exception.ResourceAlreadyExistsException;
import io.github.teamomo.order.mapper.OrderMapper;
import io.github.teamomo.order.repository.CartItemRepository;
import io.github.teamomo.order.repository.CartRepository;
import io.github.teamomo.order.repository.OrderItemRepository;
import io.github.teamomo.order.repository.OrderRepository;
import io.github.teamomo.order.repository.PaymentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  private final MomentClient momentClient;


  public CartDto findCartByCustomerId(Long customerId) {
    Cart cart = cartRepository
        .findByCustomerId(customerId)
        .orElseGet(() -> {
          Cart newCart = new Cart();
          newCart.setCustomerId(customerId);
          return cartRepository.save(newCart);
        });

    return mapToCartDtoWithUpdatedAvailability(cart);
  }


  public CartDto createCart(Long customerId) {
    if(cartRepository.findByCustomerId(customerId).isPresent()){
      throw new ResourceAlreadyExistsException("Cart already exist with given customer ID " + customerId);
    }
    Cart cart = new Cart();
    cart.setCustomerId(customerId);
    Cart savedCart = cartRepository.save(cart);

    return orderMapper.toCartDto(savedCart);
  }


  public CartDto updateCart(Long customerId, CartDto cartDto) {

    Cart cart = cartRepository
        .findByCustomerId(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart", "customerId", customerId.toString()));

    Cart updatedCart = orderMapper.toCartEntity(cartDto);
    updatedCart.setId(cart.getId());

    Cart savedCart = cartRepository.save(updatedCart);

    return mapToCartDtoWithUpdatedAvailability(savedCart);
  }

  public void deleteCart(Long customerId) {
    Cart cart = cartRepository
        .findByCustomerId(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart", "customerId", customerId.toString()));
    cartRepository.delete(cart);
  }

  public List<CartItemInfoDto> getAllCartItems(Long customerId) {
    Cart cart = cartRepository
        .findByCustomerId(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart", "customerId", customerId.toString()));
    List<CartItem> items = cart.getCartItems();
    List<CartItemInfoDto> itemDtos = items.stream()
        .map(orderMapper::toCartItemInfoDto)
        .toList();

    return itemDtos;
  }


  public CartItemInfoDto createCartItem(Long customerId, CartItemInfoDto cartItemDto) {
   //create cart if it is the firs item for customer
    Cart cart = cartRepository
        .findByCustomerId(customerId)
        .orElseGet(() -> {
          Cart newCart = new Cart();
          newCart.setCustomerId(customerId);
          return cartRepository.save(newCart);
        });
    CartItem item = orderMapper.toCartItemEntity(cartItemDto);
    item.setCart(cart);

    CartItem savedItem = cartItemRepository.save(item);

    cart.getCartItems().add(savedItem);
    cartRepository.save(cart);

    return orderMapper.toCartItemInfoDto(savedItem);
  }

  public CartItemInfoDto updateCartItem(Long itemId, CartItemInfoDto cartItemDto) {
    CartItem item = cartItemRepository.findById(itemId)
        .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId.toString()));

    Cart cart = cartRepository.findById(item.getCart().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", item.getCart().getId().toString()));

    CartItem updatedItem = orderMapper.toCartItemEntity(cartItemDto);

    updatedItem.setId(item.getId());
    updatedItem.setCart(cart);

    cart.getCartItems().removeIf(cartItem -> cartItem.getId().equals(itemId));
    cart.getCartItems().add(updatedItem);

    cartRepository.save(cart);

    CartItem savedItem = cartItemRepository.findById(itemId)
        .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId.toString()));

    return orderMapper.toCartItemInfoDto(savedItem);
  }

  public void deleteCartItem(Long itemId) {
    CartItem item = cartItemRepository.findById(itemId)
        .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId.toString()));

    Cart cart = cartRepository.findById(item.getCart().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", item.getCart().getId().toString()));

    cart.getCartItems().removeIf(cartItem -> cartItem.getId().equals(itemId));

    cartRepository.save(cart);
  }

  private List<CartItemInfoDto> updateItemsAvailability(List<CartItemInfoDto> cartItemDtos){

    List<CartItemInfoDto> updatedItemDtos = cartItemDtos.stream()
        .map(item -> new CartItemInfoDto(
            item.id(),
            item.cartId(),
            item.momentId(),
            item.quantity(),
            momentClient.checkTicketAvailability(item.momentId(), item.quantity())
        )).toList();

    return updatedItemDtos;
  }

  private CartDto mapToCartDtoWithUpdatedAvailability(Cart cart){

    List<CartItemInfoDto> updatedItemDtos = updateItemsAvailability(
        cart.getCartItems()
            .stream()
            .map(orderMapper::toCartItemInfoDto)
            .toList());

    CartDto cartDto = orderMapper.toCartDto(cart);

    return new CartDto(cartDto.id(), cartDto.customerId(), updatedItemDtos);
  }

}
