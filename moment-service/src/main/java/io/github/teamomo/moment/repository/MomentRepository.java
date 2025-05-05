package io.github.teamomo.moment.repository;

import io.github.teamomo.moment.entity.Moment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MomentRepository extends JpaRepository<Moment, Long> {
  List<Moment> findByHostId(Long hostId);
  Optional<Moment> findByTitle(String title);
}
