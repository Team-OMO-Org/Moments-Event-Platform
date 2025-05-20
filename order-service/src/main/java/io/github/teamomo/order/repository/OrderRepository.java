package io.github.teamomo.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.github.teamomo.order.entity.Order;

@Repository

public interface OrderRepository extends JpaRepository<Order, Long> {}
