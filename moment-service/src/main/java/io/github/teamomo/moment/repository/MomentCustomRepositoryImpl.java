package io.github.teamomo.moment.repository;

import io.github.teamomo.moment.entity.Moment;
import io.github.teamomo.moment.entity.Recurrence;
import io.github.teamomo.moment.entity.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.Instant;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MomentCustomRepositoryImpl implements MomentCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Page<Moment> findByFilters(
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
  ) {
    StringBuilder queryBuilder = new StringBuilder("SELECT m FROM Moment m WHERE 1=1");
    List<Object> parameters = new ArrayList<>();

    if (category != null) {
      queryBuilder.append(" AND m.category.name = ?").append(parameters.size() + 1);
      parameters.add(category);
    }
    if (location != null) {
      queryBuilder.append(" AND m.location.city = ?").append(parameters.size() + 1);
      parameters.add(location);
    }
    if (priceFrom != null) {
      queryBuilder.append(" AND m.price >= ?").append(parameters.size() + 1);
      parameters.add(priceFrom);
    }
    if (priceTo != null) {
      queryBuilder.append(" AND m.price <= ?").append(parameters.size() + 1);
      parameters.add(priceTo);
    }
    if (startDateFrom != null) {
      queryBuilder.append(" AND m.startDate >= ?").append(parameters.size() + 1);
      parameters.add(startDateFrom);
    }
    if (startDateTo != null) {
      queryBuilder.append(" AND m.startDate <= ?").append(parameters.size() + 1);
      parameters.add(startDateTo);
    }
    if (recurrence != null) {
      queryBuilder.append(" AND m.recurrence = ?").append(parameters.size() + 1);
      parameters.add(recurrence);
    }
    if (status != null) {
      queryBuilder.append(" AND m.status = ?").append(parameters.size() + 1);
      parameters.add(status);
    }
    if (search != null) {
      queryBuilder.append(" AND (m.title LIKE ?").append(parameters.size() + 1)
          .append(" OR m.momentDetails.description LIKE ?").append(parameters.size() + 1)
          .append(" OR m.shortDescription LIKE ?").append(parameters.size() + 1)
          .append(")");
      parameters.add("%" + search + "%");
    }

    // Sorting logic
    if (pageable.getSort().isSorted()) {
      queryBuilder.append(" ORDER BY ");
      pageable.getSort().forEach(order -> {
        queryBuilder.append("m.").append(order.getProperty())
            .append(" ").append(order.getDirection().name()).append(", ");
      });
      // Remove trailing comma and space
      queryBuilder.setLength(queryBuilder.length() - 2);
    }

    TypedQuery<Moment> query = entityManager.createQuery(queryBuilder.toString(), Moment.class);
    for (int i = 0; i < parameters.size(); i++) {
      query.setParameter(i + 1, parameters.get(i));
    }

    int totalRows = query.getResultList().size();
    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());

    List<Moment> moments = query.getResultList();
    return new PageImpl<>(moments, pageable, totalRows);
  }

}
