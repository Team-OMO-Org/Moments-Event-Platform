package io.github.teamomo.moment.repository;

import io.github.teamomo.moment.entity.Moment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
      String recurrence,
      String status,
      String search,
      Pageable pageable
  ) {
    StringBuilder queryBuilder = new StringBuilder("SELECT m FROM Moment m WHERE 1=1");
    List<Object> parameters = new ArrayList<>();

    if (category != null) {
      queryBuilder.append(" AND m.category.name = ?1");
      parameters.add(category);
    }
    if (location != null) {
      queryBuilder.append(" AND m.location.name = ?2");
      parameters.add(location);
    }
    if (priceFrom != null) {
      queryBuilder.append(" AND m.price >= ?3");
      parameters.add(priceFrom);
    }
    if (priceTo != null) {
      queryBuilder.append(" AND m.price <= ?4");
      parameters.add(priceTo);
    }
    if (startDateFrom != null) {
      queryBuilder.append(" AND m.startDate >= ?5");
      parameters.add(startDateFrom);
    }
    if (startDateTo != null) {
      queryBuilder.append(" AND m.startDate <= ?6");
      parameters.add(startDateTo);
    }
    if (recurrence != null) {
      queryBuilder.append(" AND m.recurrence = ?7");
      parameters.add(recurrence);
    }
    if (status != null) {
      queryBuilder.append(" AND m.status = ?8");
      parameters.add(status);
    }
    if (search != null) {
      queryBuilder.append(" AND (m.title LIKE ?9 OR m.description LIKE ?9)");
      parameters.add("%" + search + "%");
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
