package io.github.teamomo.moment.repository;

import io.github.teamomo.moment.entity.Moment;
import io.github.teamomo.moment.entity.Recurrence;
import io.github.teamomo.moment.entity.Status;
import java.time.Instant;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

public interface MomentCustomRepository {
  Page<Moment> findByFilters(
      String category,
      String location,
      BigDecimal priceFrom,
      BigDecimal priceTo,
      LocalDateTime startDateFrom,
      LocalDateTime startDateTo,
      Recurrence recurrence,
      Status status,
      String search,
      Pageable pageable
  );
}
