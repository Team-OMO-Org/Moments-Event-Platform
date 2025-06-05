package io.github.teamomo.customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Size(max = 255)
  @NotNull
  @Column(name = "keycloak_user_id", nullable = false)
  private String keycloakUserId;

  @Size(max = 100)
  @Column(name = "profile_name", length = 100)
  private String profileName;

  @Size(max = 100)
  @Column(name = "profile_email", length = 100)
  private String profileEmail;

  @Size(max = 255)
  @Column(name = "profile_picture")
  private String profilePicture;

  @Size(max = 255)
  @Column(name = "profile_site_url")
  private String profileSiteUrl;

  @Size(max = 500)
  @Column(name = "profile_description")
  private String profileDescription;

  @NotNull
  @ColumnDefault("1")
  @Column(name = "active", nullable = false)
  private Boolean active = true;

}