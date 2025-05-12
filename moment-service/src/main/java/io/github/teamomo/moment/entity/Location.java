package io.github.teamomo.moment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @NotNull(message = "City name cannot be null")
  @Size(min = 1, max = 50, message = "City name must be between 1 and 50 characters")
  @Column(name = "city", nullable = false, length = 50)
  private String city;

  @NotNull(message = "Address cannot be null")
  @Size(min = 1, max = 100, message = "Address must be between 1 and 50 characters")
  @Column(name = "address", length = 100)
  private String address;
}