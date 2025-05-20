package io.github.teamomo.order.repository;

import io.github.teamomo.order.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
  Optional<Cart> findByCustomerId(Long customerId);
}
