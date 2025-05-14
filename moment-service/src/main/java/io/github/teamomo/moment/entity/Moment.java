package io.github.teamomo.moment.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import java.time.LocalDateTime;
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

  @OneToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "location_id")
  private Location location;

  @Size(max = 100)
  @NotNull
  @Column(name = "title", nullable = false, length = 100)
  private String title;

  @Size(max = 255)
  @Column(name = "short_description", length = 255)
  private String shortDescription;

  @Size(max = 255)
  @Column(name = "thumbnail")
  private String thumbnail;

  @NotNull
  @Column(name = "start_date", nullable = false)
  private LocalDateTime startDate;

  @NotNull
  @Column(name = "recurrence", nullable = false)
  @Enumerated(EnumType.STRING)
  private Recurrence recurrence;

  @NotNull
  @Column(name = "price", nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @NotNull
  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;

  @NotNull
  @Column(name = "ticket_count", nullable = false)
  private Integer ticketCount;

  @OneToOne(mappedBy = "moment", cascade = CascadeType.ALL)
  private MomentDetail momentDetails;
}