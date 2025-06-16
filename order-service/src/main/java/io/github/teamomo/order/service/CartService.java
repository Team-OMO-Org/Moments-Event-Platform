package io.github.teamomo.order.service;

import io.github.teamomo.order.client.MomentClient;
import io.github.teamomo.order.dto.CartDto;
import io.github.teamomo.order.dto.CartItemDto;
import io.github.teamomo.order.dto.CartItemInfoDto;
import io.github.teamomo.order.entity.Cart;
import io.github.teamomo.order.entity.CartItem;
import io.github.teamomo.order.exception.ResourceAlreadyExistsException;
import io.github.teamomo.order.exception.ResourceNotFoundException;
import io.github.teamomo.order.mapper.OrderMapper;
import io.github.teamomo.order.repository.CartItemRepository;
import io.github.teamomo.order.repository.CartRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
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
    if (cartRepository.findByCustomerId(customerId).isPresent()) {
      throw new ResourceAlreadyExistsException("Cart already exists for customer ID " + customerId);
    }
    Cart cart = new Cart();
    cart.setCustomerId(customerId);
    Cart savedCart = cartRepository.save(cart);
    return orderMapper.toCartDto(savedCart);
  }

  @Transactional
  public CartDto updateCart(Long customerId, CartDto cartDto) {
    Cart cart = cartRepository.findByCustomerId(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart", "customerId", customerId.toString()));
    Cart updatedCart = orderMapper.toCartEntity(cartDto);
    updatedCart.setId(cart.getId());
   /* if (updatedCart.getCartItems() != null) {
      updatedCart.getCartItems().forEach(item -> item.setCart(updatedCart));
    }*/
    Cart savedCart = cartRepository.save(updatedCart);
    return mapToCartDtoWithUpdatedAvailability(savedCart);
  }

  @Transactional
  public void deleteCart(Long customerId) {
    Cart cart = cartRepository.findByCustomerId(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart", "customerId", customerId.toString()));
    cartRepository.delete(cart);
  }

  public List<CartItemInfoDto> getAllCartItems(Long customerId) {
    Cart cart = cartRepository.findByCustomerId(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart", "customerId", customerId.toString()));
    return cart.getCartItems()
        .stream()
        .map(orderMapper::toCartItemInfoDto)
        .toList();
  }

  @Transactional
  public CartItemInfoDto createCartItem(Long customerId, CartItemInfoDto cartItemDto) {
    Cart cart = cartRepository.findByCustomerId(customerId)
        .orElseGet(() -> {
          Cart newCart = new Cart();
          newCart.setCustomerId(customerId);
          return cartRepository.save(newCart);
        });
    //check if same item exists in the cart, increase ticket quantity and update
    CartItem existItem = cartItemRepository.findByCartIdAndMomentId(cart.getId(), cartItemDto.momentId());
    if(existItem != null){
      Integer quantity = cartItemDto.quantity() + existItem.getQuantity();
      CartItemInfoDto cartItem = new CartItemInfoDto(existItem.getId(), cart.getId(), cartItemDto.momentId(), quantity, cartItemDto.isAvailable());
      return updateCartItem(existItem.getId(), cartItem);
    }
    CartItem item = orderMapper.toCartItemEntity(cartItemDto);
    item.setCart(cart);
    CartItem savedItem = cartItemRepository.save(item);
    cart.getCartItems().add(savedItem);
    cartRepository.save(cart);
    return orderMapper.toCartItemInfoDto(savedItem);
  }

  @Transactional
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
    //todo: do I need check ticket availability here?
    return orderMapper.toCartItemInfoDto(savedItem);
  }

  @Transactional
  public void deleteCartItem(Long itemId) {
    CartItem item = cartItemRepository.findById(itemId)
        .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId.toString()));
    Cart cart = cartRepository.findById(item.getCart().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", item.getCart().getId().toString()));
    cart.getCartItems().removeIf(cartItem -> cartItem.getId().equals(itemId));
    cartRepository.save(cart);
  }

  private List<CartItemInfoDto> updateItemsAvailability(List<CartItemInfoDto> cartItemDtos) {
    return cartItemDtos.stream()
        .map(item -> new CartItemInfoDto(
            item.id(),
            item.cartId(),
            item.momentId(),
            item.quantity(),
            momentClient.checkTicketAvailability(item.momentId(), item.quantity())
        ))
        .toList();
  }

  private CartDto mapToCartDtoWithUpdatedAvailability(Cart cart) {
    List<CartItemInfoDto> updatedItems = updateItemsAvailability(
        cart.getCartItems()
            .stream()
            .map(orderMapper::toCartItemInfoDto)
            .toList());
    CartDto cartDto = orderMapper.toCartDto(cart);
    return new CartDto(cartDto.id(), cartDto.customerId(), updatedItems);
  }

}
