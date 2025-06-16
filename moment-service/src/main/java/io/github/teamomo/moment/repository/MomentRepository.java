package io.github.teamomo.moment.repository;

import io.github.teamomo.moment.entity.Moment;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MomentRepository extends JpaRepository<Moment, Long>
, MomentCustomRepository
{

  Page<Moment> findAllByStartDateAfter(Instant startDate, Pageable pageable);

  Page<Moment> findByTitleAndStartDateAfter(String title, Instant start, Pageable pageable);

  Page<Moment> findByCategoryIdAndStartDateAfter(Long categoryId, Instant start, Pageable pageable);

  Page<Moment> findByStatusAndStartDateAfter(String status, Instant start, Pageable pageable);

  Page<Moment> findByHostIdAndStartDateAfter(Long hostId, Instant start, Pageable pageable);

  Page<Moment> findByLocationIdAndStartDateAfter(Long locationId, Instant start, Pageable pageable);

  Optional<Moment> findByTitleAndStartDate(String title, LocalDateTime startDate);

  List<Moment> findByHostId(Long id);

  List<Moment> findByHostIdOrderByIdDesc(Long id);
}
