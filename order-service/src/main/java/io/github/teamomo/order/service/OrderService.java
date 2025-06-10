package io.github.teamomo.order.service;

import io.github.teamomo.order.client.CustomerClient;
import io.github.teamomo.order.client.MomentClient;
import io.github.teamomo.order.dto.CustomerDto;
import io.github.teamomo.order.dto.OrderDto;
import io.github.teamomo.order.dto.OrderInfoDto;
import io.github.teamomo.order.entity.*;
import io.github.teamomo.order.event.OrderPlacedEvent;
import io.github.teamomo.order.exception.CartIsEmptyException;
import io.github.teamomo.order.exception.PaymentProcessingException;
import io.github.teamomo.order.exception.ResourceNotFoundException;
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
import org.springframework.kafka.core.KafkaTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final MomentClient momentClient;
  private final CustomerClient customerClient;
  private final CartService cartService;

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  private final PaymentRepository paymentRepository;
  private final KafkaTemplate<String, OrderPlacedEvent>
      kafkaTemplate; // key: Topic name, value: Event

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
    Order storedOrder = orderRepository.save(order);

    List<OrderItem> orderItems = new ArrayList<>();
    boolean allBooked = true;

    for (CartItem cartItem : cart.getCartItems()) {
      try {
        BigDecimal ticketsPrice =
            momentClient.bookTickets(cartItem.getMomentId(), cartItem.getQuantity());
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(storedOrder);
        orderItem.setMomentId(cartItem.getMomentId());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(ticketsPrice);
        orderItem.setCreatedAt(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        orderItems.add(orderItem);
      } catch (Exception e) {
        log.error("Failed to book tickets for moment ID: {}", cartItem.getMomentId(), e);
        OrderItem failedOrderItem = new OrderItem();
        failedOrderItem.setOrder(storedOrder);
        failedOrderItem.setMomentId(cartItem.getMomentId());
        failedOrderItem.setQuantity(cartItem.getQuantity());
        failedOrderItem.setCreatedAt(
            LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        orderItems.add(failedOrderItem);
        allBooked = false;
        break;
      }
    }

    if (!allBooked) {
      orderItems.forEach(
          item -> momentClient.cancelTicketBooking(item.getMomentId(), item.getQuantity()));
      order.setOrderStatus(OrderStatus.CANCELLED);
      return orderMapper.toDto(orderRepository.save(order));
    }

    BigDecimal totalPrice =
        orderItems.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

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

    return orderMapper.toDto(orderRepository.save(order));
  }

  public void sendOrderNotification(OrderDto orderDto) {
    try {
      CustomerDto customer = customerClient.getCustomerById(orderDto.customerId());
      if (customer != null) {
        String[] nameParts =
            customer.profileName().split(" ", 2); // Split into first name and last name
        String firstName = nameParts.length > 0 ? nameParts[0] : "Dear";
        String lastName = nameParts.length > 1 ? nameParts[1] : "customer";

        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
        orderPlacedEvent.setOrderNumber(orderDto.id().toString());
        orderPlacedEvent.setEmail(customer.profileEmail());
        orderPlacedEvent.setFirstName(firstName);
        orderPlacedEvent.setLastName(lastName);

        log.info(
            "Start - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
        kafkaTemplate.send("order-placed", orderPlacedEvent);
        log.info("End - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
      }
    } catch (Exception e) {
      log.error("Failed to fetch customer details for customer ID: {}", orderDto.customerId(), e);
    }
  }

  public void testKafka() {
    // ToDo: remove, just for testing
    // send the message to Kafka Topic -> email service, sending out email
    // Create OrderPlacedEvent
    OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
    orderPlacedEvent.setOrderNumber("12345"); // Replace with actual order number
    orderPlacedEvent.setEmail("user@email.com"); // Replace with actual email from user details
    orderPlacedEvent.setFirstName("John"); // Replace with actual first name from user details
    orderPlacedEvent.setLastName("Doe"); // Replace with actual last name from user details

    log.info("Start - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
    kafkaTemplate.send("order-placed", orderPlacedEvent);
    log.info("End - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
  }

  public OrderInfoDto getOrderById(Long orderId) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Order", "orderId", orderId.toString()));

    return orderMapper.toOrderInfoDto(order);
  }
}
