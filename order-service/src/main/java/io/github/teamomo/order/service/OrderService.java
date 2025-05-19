package io.github.teamomo.order.service;

import io.github.teamomo.order.dto.CartDto;
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


}
