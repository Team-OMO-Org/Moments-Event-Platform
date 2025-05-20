package io.github.teamomo.order.service;

import io.github.teamomo.order.client.MomentClient;
import io.github.teamomo.order.dto.OrderDto;
import io.github.teamomo.order.entity.Cart;
import io.github.teamomo.order.entity.CartItem;
import io.github.teamomo.order.entity.Order;
import io.github.teamomo.order.entity.OrderItem;
import io.github.teamomo.order.entity.OrderStatus;
import io.github.teamomo.order.entity.Payment;
import io.github.teamomo.order.entity.PaymentStatus;
import io.github.teamomo.order.exception.CartIsEmptyException;
import io.github.teamomo.order.exception.PaymentProcessingException;
import io.github.teamomo.order.exception.ResourceNotFoundException;
import io.github.teamomo.order.exception.TicketsBookingFailedException;
import io.github.teamomo.order.mapper.OrderMapper;
import io.github.teamomo.order.repository.CartRepository;
import io.github.teamomo.order.repository.OrderRepository;
import io.github.teamomo.order.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final OrderRepository orderRepository;
  private final CartRepository cartRepository;
  private final PaymentRepository paymentRepository;
  private final MomentClient momentClient;
  private final OrderMapper orderMapper;

  @Transactional
  public OrderDto createOrderByCustomerId(Long customerId) {

    Cart cart = cartRepository.findByCustomerId(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart", "ID", customerId.toString()));

    if (cart.getCartItems().isEmpty()) {
      log.error("Cart is empty for customer ID: {}", customerId);
      throw new CartIsEmptyException(customerId);
    }

    // Create an order
    Order order = new Order();
    order.setCustomerId(customerId);
    order.setOrderStatus(OrderStatus.PENDING);
    order.setTotalPrice(BigDecimal.ZERO);

    List<OrderItem> orderItems = new ArrayList<>();
    boolean allBooked = true;

    // Check if all items in the cart are available,
    // copy them to order items and book tickets
    for (CartItem cartItem : cart.getCartItems()) {
      try {
        BigDecimal ticketsPrice =  momentClient.bookTickets(cartItem.getMomentId(), cartItem.getQuantity());
        OrderItem orderItem = new OrderItem();
        orderItem.setMomentId(cartItem.getMomentId());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(ticketsPrice);
        orderItems.add(orderItem);
  //      order.setTotalPrice(order.getTotalPrice().add(ticketsPrice));
      } catch (Exception e) {
        log.error("Failed to book tickets for moment ID: {}", cartItem.getMomentId(), e);
        OrderItem failedOrderItem = new OrderItem();
        failedOrderItem.setMomentId(cartItem.getMomentId());
        failedOrderItem.setQuantity(cartItem.getQuantity());
        orderItems.add(failedOrderItem); // Add the failed item
        allBooked = false;
        break;
        //throw new TicketsBookingFailedException(cartItem.getMomentId(), e); // throw exception if any booking failed?
      }
    }

    // If any booking failed, cancel all booked tickets
    // set order status to 'CANCELLED' and exit
    if (!allBooked) {
      orderItems.forEach(item -> momentClient.cancelTicketBooking(item.getMomentId(), item.getQuantity()));
      order.setOrderStatus(OrderStatus.CANCELLED);
      //save orderItems?
     return orderMapper.toDto(orderRepository.save(order));
    }

    BigDecimal totalPrice = order.getOrderItems()
        .stream()
        .map(OrderItem::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    order.setOrderItems(orderItems);
    order.setTotalPrice(totalPrice);

    // Create a payment with initial status 'PENDING'
    PaymentStatus paymentStatus = PaymentStatus.PENDING;

    Payment payment = new Payment();
    payment.setOrder(order);
    payment.setAmount(order.getTotalPrice());
    payment.setPaymentStatus(paymentStatus);


    // Process payment
     try {
      // Simulate payment processing (Stripe integration)
      //todo : add Stripe payment processing here
      paymentStatus = PaymentStatus.SUCCEEDED;
    } catch (PaymentProcessingException e) {
      log.error("Payment processing failed for order ID: {}", order.getId(), e);
      paymentStatus = PaymentStatus.FAILED;
    } catch (Exception e) {
      log.error("Unexpected error during payment processing for order ID: {}", order.getId(), e);
      paymentStatus = PaymentStatus.FAILED;
    }

    // Save payment details
    payment.setProcessedAt(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    paymentRepository.save(payment);


    // If payment failed, set order status to 'CANCELLED' and exit
    if (paymentStatus == PaymentStatus.FAILED) {

      order.setOrderStatus(OrderStatus.CANCELLED);
      return orderMapper.toDto(orderRepository.save(order));
    }

    // Clear the cart after successful order
    cartRepository.delete(cart);

    //todo: add sending notification here

    // Set order status to 'COMPLETED'
    order.setOrderStatus(OrderStatus.COMPLETED);

    return orderMapper.toDto(orderRepository.save(order));
  }
}
