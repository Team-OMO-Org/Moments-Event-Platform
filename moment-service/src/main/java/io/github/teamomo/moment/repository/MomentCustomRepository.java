package io.github.teamomo.moment.repository;

import io.github.teamomo.moment.entity.Moment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface MomentCustomRepository {
  Page<Moment> findByFilters(
      String category,
      String location,
      BigDecimal priceFrom,
      BigDecimal priceTo,
      LocalDateTime startDateFrom,
      LocalDateTime startDateTo,
      String recurrence,
      String status,
      String search,
      Pageable pageable
  );
}
