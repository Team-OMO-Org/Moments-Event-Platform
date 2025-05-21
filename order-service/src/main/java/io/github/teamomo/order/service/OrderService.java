package io.github.teamomo.order.service;

import io.github.teamomo.order.client.MomentClient;
import io.github.teamomo.order.dto.OrderDto;
import io.github.teamomo.order.entity.*;
import io.github.teamomo.order.exception.CartIsEmptyException;
import io.github.teamomo.order.exception.PaymentProcessingException;
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

  private final MomentClient momentClient;
  private final CartService cartService;

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  private final PaymentRepository paymentRepository;

  @Transactional
  public OrderDto createOrderByCustomerId(Long customerId) {

     Cart cart = orderMapper.toCartEntity(cartService.findCartByCustomerId(customerId));

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

    cartService.deleteCart(customerId);
    order.setOrderStatus(OrderStatus.COMPLETED);

    // TODO: Add notification logic here

    return orderMapper.toDto(orderRepository.save(order));
  }

}
