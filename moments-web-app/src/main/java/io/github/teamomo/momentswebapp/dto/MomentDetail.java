package io.github.teamomo.momentswebapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MomentDetail {

 private Long id;

  @Size(max = 1000, message = "Description must be at most 1000 characters long")
  private String description;
}