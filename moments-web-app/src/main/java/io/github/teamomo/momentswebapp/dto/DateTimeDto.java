package io.github.teamomo.momentswebapp.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * This class is used to represent a date and time in a format suitable for the frontend.
 * It allows for easy conversion between a LocalDateTime object and a string representation
 * of the date and time.
 */
public record DateTimeDto(
    @NotNull(message = "Start date cannot be null")
    String date,

    @NotNull(message = "Start time cannot be null")
    String time) {

  public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
  public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  public static DateTimeDto from(LocalDateTime dateTime) {
    return new DateTimeDto(
        dateTime.format(dateFormatter),
        dateTime.format(timeFormatter)
    );
  }

  public LocalDateTime toLocalDateTime() {
    return LocalDateTime.parse(date + "T" + time, dateTimeFormatter);
  }
}
