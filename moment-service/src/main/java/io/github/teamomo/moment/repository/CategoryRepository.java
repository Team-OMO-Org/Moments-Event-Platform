package io.github.teamomo.moment.repository;

import io.github.teamomo.moment.dto.CategoryDto;
import io.github.teamomo.moment.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query("SELECT new io.github.teamomo.moment.dto.CategoryDto(c.id, c.name, COUNT(m)) " +
      "FROM Category c " +
      "LEFT JOIN Moment m ON m.category.id = c.id " +
      "GROUP BY c.id, c.name " +
      "ORDER BY COUNT(m) DESC")
  List<CategoryDto> findAllByMomentsCount();
}
