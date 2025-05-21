package io.github.teamomo.order.service;

import io.github.teamomo.order.client.MomentClient;
import io.github.teamomo.order.dto.OrderDto;
import io.github.teamomo.order.entity.*;
import io.github.teamomo.order.exception.CartIsEmptyException;
import io.github.teamomo.order.exception.ResourceNotFoundException;
import io.github.teamomo.order.mapper.OrderMapper;
import io.github.teamomo.order.repository.CartRepository;
import io.github.teamomo.order.repository.OrderRepository;
import io.github.teamomo.order.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private CartRepository cartRepository;

  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private MomentClient momentClient;

  @Mock
  private OrderMapper orderMapper;

  @InjectMocks
  private OrderService orderService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldThrowExceptionWhenCartIsEmpty() {
    Long customerId = 1L;

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(new Cart()));

    assertThrows(CartIsEmptyException.class, () -> orderService.createOrderByCustomerId(customerId));
    verify(cartRepository, times(1)).findByCustomerId(customerId);
  }

  @Test
  void shouldThrowExceptionWhenCartNotFound() {
    Long customerId = 1L;

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> orderService.createOrderByCustomerId(customerId));
    verify(cartRepository, times(1)).findByCustomerId(customerId);
  }

  @Test
  void shouldCreateOrderSuccessfully() {
    Long customerId = 1L;
    Cart cart = new Cart();
    cart.setCartItems(List.of(new CartItem(1L, cart, 1L,2)));

    Order order = new Order();
    order.setId(1L);
    order.setOrderStatus(OrderStatus.COMPLETED);

    OrderDto orderDto = new OrderDto(1L, customerId, OrderStatus.COMPLETED, BigDecimal.TEN, List.of());

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
    when(momentClient.bookTickets(anyLong(), anyInt())).thenReturn(BigDecimal.TEN);
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
    when(momentClient.bookTickets(anyLong(), anyInt())).thenReturn(BigDecimal.TEN);
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

    OrderDto result = orderService.createOrderByCustomerId(customerId);

    assertNotNull(result);
    assertEquals(OrderStatus.COMPLETED, result.orderStatus());
    verify(cartRepository, times(1)).findByCustomerId(customerId);
    verify(momentClient, times(1)).bookTickets(anyLong(), anyInt());
    verify(orderRepository, times(1)).save(any(Order.class));
  }

  @Test
  void shouldCancelOrderWhenTicketBookingFails() {
    Long customerId = 1L;
    Cart cart = new Cart();
    cart.setCartItems(List.of(new CartItem(1L,cart, 1L, 2)));

    Order order = new Order();
    order.setOrderStatus(OrderStatus.CANCELLED);

    OrderDto orderDto = new OrderDto(1L, customerId, OrderStatus.CANCELLED, BigDecimal.ZERO, List.of());

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
    doThrow(new RuntimeException("Booking failed")).when(momentClient).bookTickets(anyLong(), anyInt());
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

    OrderDto result = orderService.createOrderByCustomerId(customerId);

    assertNotNull(result);
    assertEquals(OrderStatus.CANCELLED, result.orderStatus());
    verify(momentClient, times(1)).bookTickets(anyLong(), anyInt());
    verify(momentClient, times(1)).cancelTicketBooking(anyLong(), anyInt());
  }

  @Test
  void shouldCancelOrderWhenPaymentFails() {
    Long customerId = 1L;
    Cart cart = new Cart();
    cart.setCartItems(List.of(new CartItem(1L, cart, 1L, 2)));

    Order order = new Order();
    order.setOrderStatus(OrderStatus.CANCELLED);

    OrderDto orderDto = new OrderDto(1L, customerId, OrderStatus.CANCELLED, BigDecimal.ZERO, List.of());

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
    when(momentClient.bookTickets(anyLong(), anyInt())).thenReturn(BigDecimal.TEN);

    when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
    when(momentClient.bookTickets(anyLong(), anyInt())).thenReturn(BigDecimal.TEN);
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

    OrderDto result = orderService.createOrderByCustomerId(customerId);

    assertNotNull(result);
    assertEquals(OrderStatus.CANCELLED, result.orderStatus());
    verify(paymentRepository, times(1)).save(any(Payment.class));
  }
}