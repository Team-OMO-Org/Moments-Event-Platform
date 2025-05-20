package io.github.teamomo.order.service;

import io.github.teamomo.moment.exception.ResourceNotFoundException;
import io.github.teamomo.order.client.MomentClient;
import io.github.teamomo.order.dto.CartDto;
import io.github.teamomo.order.dto.CartItemInfoDto;
import io.github.teamomo.order.dto.OrderDto;
import io.github.teamomo.order.entity.*;
import io.github.teamomo.order.exception.CartIsEmptyException;
import io.github.teamomo.order.exception.PaymentProcessingException;
import io.github.teamomo.order.exception.ResourceAlreadyExistsException;
import io.github.teamomo.order.exception.TicketsBookingFailedException;
import io.github.teamomo.order.mapper.OrderMapper;
import io.github.teamomo.order.repository.*;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final PaymentRepository paymentRepository;
  private final OrderMapper orderMapper;
  private final MomentClient momentClient;

  // --- Cart Management ---

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

  public CartDto updateCart(Long customerId, CartDto cartDto) {
    Cart cart = cartRepository.findByCustomerId(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart", "customerId", customerId.toString()));
    Cart updatedCart = orderMapper.toCartEntity(cartDto);
    updatedCart.setId(cart.getId());
    Cart savedCart = cartRepository.save(updatedCart);
    return mapToCartDtoWithUpdatedAvailability(savedCart);
  }

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

  public CartItemInfoDto createCartItem(Long customerId, CartItemInfoDto cartItemDto) {
    Cart cart = cartRepository.findByCustomerId(customerId)
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


  // --- Order creation and processing ---

  @Transactional
  public OrderDto createOrderByCustomerId(Long customerId) {
    Cart cart = cartRepository.findByCustomerId(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart", "ID", customerId.toString()));

    if (cart.getCartItems().isEmpty()) {
      log.error("Cart is empty for customer ID: {}", customerId);
      throw new CartIsEmptyException(customerId);
    }

    Order order = new Order();
    order.setCustomerId(customerId);
    order.setOrderStatus(OrderStatus.PENDING);
    order.setTotalPrice(BigDecimal.ZERO);

    List<OrderItem> orderItems = new ArrayList<>();
    boolean allBooked = true;

    for (CartItem cartItem : cart.getCartItems()) {
      try {
        BigDecimal ticketsPrice = momentClient.bookTickets(cartItem.getMomentId(), cartItem.getQuantity());
        OrderItem orderItem = new OrderItem();
        orderItem.setMomentId(cartItem.getMomentId());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(ticketsPrice);
        orderItems.add(orderItem);
      } catch (Exception e) {
        log.error("Failed to book tickets for moment ID: {}", cartItem.getMomentId(), e);
        OrderItem failedOrderItem = new OrderItem();
        failedOrderItem.setMomentId(cartItem.getMomentId());
        failedOrderItem.setQuantity(cartItem.getQuantity());
        orderItems.add(failedOrderItem);
        allBooked = false;
        break;
      }
    }

    if (!allBooked) {
      orderItems.forEach(item -> momentClient.cancelTicketBooking(item.getMomentId(), item.getQuantity()));
      order.setOrderStatus(OrderStatus.CANCELLED);
      return orderMapper.toDto(orderRepository.save(order));
    }

    BigDecimal totalPrice = orderItems.stream()
        .map(OrderItem::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    order.setOrderItems(orderItems);
    order.setTotalPrice(totalPrice);

    Payment payment = new Payment();
    payment.setOrder(order);
    payment.setAmount(totalPrice);
    payment.setPaymentStatus(PaymentStatus.PENDING);

    PaymentStatus paymentStatus = PaymentStatus.PENDING;

    try {
      // TODO: Add Stripe or payment gateway integration here
      paymentStatus = PaymentStatus.SUCCEEDED;
    } catch (PaymentProcessingException e) {
      log.error("Payment processing failed for order ID: {}", order.getId(), e);
      paymentStatus = PaymentStatus.FAILED;
    } catch (Exception e) {
      log.error("Unexpected error during payment processing for order ID: {}", order.getId(), e);
      paymentStatus = PaymentStatus.FAILED;
    }

    payment.setPaymentStatus(paymentStatus);
    payment.setProcessedAt(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    paymentRepository.save(payment);

    if (paymentStatus == PaymentStatus.FAILED) {
      order.setOrderStatus(OrderStatus.CANCELLED);
      return orderMapper.toDto(orderRepository.save(order));
    }

    cartRepository.delete(cart);
    order.setOrderStatus(OrderStatus.COMPLETED);

    // TODO: Add notification logic here

    return orderMapper.toDto(orderRepository.save(order));
  }

}
