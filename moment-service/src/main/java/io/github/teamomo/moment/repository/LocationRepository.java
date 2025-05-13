package io.github.teamomo.moment.repository;

import io.github.teamomo.moment.dto.CityDto;
import io.github.teamomo.moment.entity.Location;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LocationRepository extends JpaRepository<Location, Long> {

  @Query("SELECT new io.github.teamomo.moment.dto.CityDto(l.city, COUNT(m)) " +
      "FROM Location l " +
      "LEFT JOIN Moment m ON m.location.id = l.id " +
      "GROUP BY l.city " +
      "ORDER BY COUNT(m) DESC")
  List<CityDto> findAllByMomentsCount();

}
