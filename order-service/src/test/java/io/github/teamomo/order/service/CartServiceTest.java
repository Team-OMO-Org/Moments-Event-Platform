package io.github.teamomo.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.teamomo.order.client.MomentClient;
import io.github.teamomo.order.dto.CartDto;
import io.github.teamomo.order.dto.CartItemInfoDto;
import io.github.teamomo.order.entity.Cart;
import io.github.teamomo.order.entity.CartItem;
import io.github.teamomo.order.exception.ResourceAlreadyExistsException;
import io.github.teamomo.order.exception.ResourceNotFoundException;
import io.github.teamomo.order.mapper.OrderMapper;
import io.github.teamomo.order.repository.CartItemRepository;
import io.github.teamomo.order.repository.CartRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CartServiceTest {

  @Mock
  private CartRepository cartRepository;

  @Mock
  private CartItemRepository cartItemRepository;

  @Mock
  private MomentClient momentClient;

  @Mock
  private OrderMapper orderMapper;

  @InjectMocks
  private CartService cartService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void findCartByCustomerId_ShouldCreateCart(){
    Long customerId = 1L;
    Cart newCart = new Cart();
    newCart.setCustomerId(customerId);

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());
    when(cartRepository.save(any(Cart.class))).thenReturn(newCart);
    when(orderMapper.toCartDto(newCart)).thenReturn(new CartDto(1L, customerId, List.of()));

    CartDto result = cartService.findCartByCustomerId(customerId);

    assertNotNull(result);
    assertEquals(customerId, result.customerId());
    verify(cartRepository, times(1)).save(any(Cart.class));
  }

  @Test
  void findCartByCustomerId_ShouldFindCart(){
    Long customerId = 1L;
    Cart cart = new Cart();
    cart.setCustomerId(customerId);

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
    when(orderMapper.toCartDto(cart)).thenReturn(new CartDto(1L, customerId, List.of()));

    CartDto result = cartService.findCartByCustomerId(customerId);

    assertNotNull(result);
    assertEquals(customerId, result.customerId());
    verify(cartRepository, times(1)).findByCustomerId(customerId);

  }

  @Test
  void createCart_shouldThrowExceptionWhenCartExists() {
    Long customerId = 1L;

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(new Cart()));

    assertThrows(ResourceAlreadyExistsException.class, () -> cartService.createCart(customerId));
    verify(cartRepository, times(1)).findByCustomerId(customerId);
  }

  @Test
  void updateCart_shouldUpdate() {
    Long customerId = 1L;
    Cart existingCart = new Cart();
    existingCart.setCustomerId(customerId);

    CartDto cartDto = new CartDto(1L, customerId, List.of());
    Cart updatedCart = new Cart();
    updatedCart.setCustomerId(customerId);

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(existingCart));
    when(orderMapper.toCartEntity(cartDto)).thenReturn(updatedCart);
    when(cartRepository.save(updatedCart)).thenReturn(updatedCart);
    when(orderMapper.toCartDto(updatedCart)).thenReturn(cartDto);

    CartDto result = cartService.updateCart(customerId, cartDto);

    assertNotNull(result);
    assertEquals(customerId, result.customerId());
    verify(cartRepository, times(1)).save(updatedCart);
  }

  @Test
  void deleteCart_ShouldDeleteCart() {
    Long customerId = 1L;
    Cart cart = new Cart();
    cart.setCustomerId(customerId);

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

    cartService.deleteCart(customerId);

    verify(cartRepository, times(1)).delete(cart);
  }

  @Test
  void deleteCart_ShouldThrowExceptionWhenCartNotExists() {
    Long customerId = 1L;

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> cartService.deleteCart(customerId));
    verify(cartRepository, times(1)).findByCustomerId(customerId);
  }

  @Test
  void createCartItem_ShouldCreateNewItem() {
    Long customerId = 1L;
    Cart cart = new Cart();
    cart.setCustomerId(customerId);

    CartItemInfoDto cartItemDto = new CartItemInfoDto(1L, 1L, 1L, 2, true);
    CartItem cartItem = new CartItem();
    cartItem.setQuantity(2);

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
    when(orderMapper.toCartItemEntity(cartItemDto)).thenReturn(cartItem);
    when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
    when(orderMapper.toCartItemInfoDto(cartItem)).thenReturn(cartItemDto);

    CartItemInfoDto result = cartService.createCartItem(customerId, cartItemDto);

    assertNotNull(result);
    assertEquals(2, result.quantity());
    verify(cartItemRepository, times(1)).save(any(CartItem.class));
  }

  @Test
  void updateCartItem_ShouldUpdateExistingItem() {
    Long itemId = 1L;
    CartItemInfoDto cartItemDto = new CartItemInfoDto(itemId, 1L, 1L, 3, true);
    CartItem existingItem = new CartItem();
    existingItem.setId(itemId);
    existingItem.setQuantity(2);

    Cart cart = new Cart();
    cart.setId(1L);
    existingItem.setCart(cart);

    when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
    when(cartRepository.findById(existingItem.getCart().getId())).thenReturn(Optional.of(cart));
    when(orderMapper.toCartItemEntity(cartItemDto)).thenReturn(existingItem);
    when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
    when(orderMapper.toCartItemInfoDto(existingItem)).thenReturn(cartItemDto);

    CartItemInfoDto result = cartService.updateCartItem(itemId, cartItemDto);

    assertNotNull(result);
    assertEquals(3, result.quantity());
    verify(cartItemRepository, times(2)).findById(itemId);
  }

  @Test
  void deleteCartItem_ShouldDeleteItem() {
    Long itemId = 1L;
    CartItem cartItem = new CartItem();
    cartItem.setId(itemId);

    Cart cart = new Cart();
    cart.setId(1L);
    cartItem.setCart(cart);

    when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(cartItem));
    when(cartRepository.findById(cartItem.getCart().getId())).thenReturn(Optional.of(cart));

    cartService.deleteCartItem(itemId);

    verify(cartItemRepository, times(1)).findById(itemId);
    verify(cartRepository, times(1)).save(cart);
  }

  @Test
  void deleteCartItem_ShouldThrowExceptionWhenItemNotFound() {
    Long itemId = 1L;

    when(cartItemRepository.findById(itemId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> cartService.deleteCartItem(itemId));
    verify(cartItemRepository, times(1)).findById(itemId);
    verify(cartRepository, times(0)).findById(any());
  }
}
