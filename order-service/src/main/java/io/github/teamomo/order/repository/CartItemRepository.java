package io.github.teamomo.order.repository;

import io.github.teamomo.order.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  CartItem findByCartIdAndMomentId(Long cartId, Long momentId);
}
