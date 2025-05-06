package io.github.teamomo.moment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "moments")
public class Moment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "host_id")
  private Long hostId;

  @NotNull
  @Column(name = "category_id", nullable = false)
  private Long categoryId;

  @NotNull
  @Column(name = "location_id", nullable = false)
  private Long locationId;

  @Size(max = 100)
  @NotNull
  @Column(name = "title", nullable = false, length = 100)
  private String title;

  @Column(name = "thumbnail")
  @Lob
  private byte[] thumbnail;

  @NotNull
  @Column(name = "start_date", nullable = false)
  private Instant startDate;

  @NotNull
  @Column(name = "recurrence", nullable = false)
  private String recurrence;

  @NotNull
  @Column(name = "price", nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @NotNull
  @Column(name = "status", nullable = false)
  private String status;

  @NotNull
  @Column(name = "ticket_count", nullable = false)
  private Integer ticketCount;

  @OneToOne(mappedBy = "moment")
  private MomentDetail momentDetails;
}