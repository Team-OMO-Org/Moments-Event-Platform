package io.github.teamomo.moment.repository;

import io.github.teamomo.moment.entity.Moment;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MomentRepository extends JpaRepository<Moment, Long> {

  // Pagination, sorting, and filtering
  Page<Moment> findByTitleAndStartDateAfter(String title, Instant start, Pageable pageable);

  Page<Moment> findByCategoryIdAndStartDateAfter(Long categoryId, Instant start, Pageable pageable);

  Page<Moment> findByStatusAndStartDateAfter(String status, Instant start, Pageable pageable);

  Page<Moment> findByHostIdAndStartDateAfter(Long hostId, Instant start, Pageable pageable);

  Page<Moment> findByLocationIdAndStartDateAfter(Long locationId, Instant start, Pageable pageable);
}
