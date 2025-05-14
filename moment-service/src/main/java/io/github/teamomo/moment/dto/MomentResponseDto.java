package io.github.teamomo.moment.dto;

import io.github.teamomo.moment.entity.Recurrence;
import io.github.teamomo.moment.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Data Transfer Object for representing a moment's details in the response.
 *
 * @param id Unique identifier of the moment
 * @param title Title of the moment
 * @param category Category of the moment
 * @param location Location of the moment (city)
 * @param price Price of the moment
 * @param startDate Start date and time of the moment
 * @param recurrence Recurrence type of the moment (e.g., ONETIME, REGULAR)
 * @param status Status of the moment (e.g., LIVE, DRAFT)
 * @param shortDescription Short description of the moment
 */
@Schema(description = "Data Transfer Object for representing a moment's details in the response.")
public record MomentResponseDto(
    @Schema(
        description = "Unique identifier of the moment",
        example = "1",
        minimum = "1")
    Long id,

    @Schema(
        description = "Title of the moment",
        example = "Concert in the Park",
        maxLength = 100)
    String title,

    @Schema(
        description = "Category of the moment",
        example = "Music",
        maxLength = 50)
    String category,

    @Schema(
        description = "Location of the moment (city)",
        example = "New York",
        maxLength = 100)
    String location,

    @Schema(
        description = "Price of the moment",
        example = "50.00",
        minimum = "0.0")
    BigDecimal price,

    @Schema(
        description = "Start date and time of the moment",
        example = "2025-06-01T19:00:00")
    LocalDateTime startDate,

    @Schema(
        description = "Recurrence type of the moment (e.g., ONETIME, REGULAR)",
        example = "ONETIME",
        allowableValues = {"ONETIME", "REGULAR"})
    Recurrence recurrence,

    @Schema(
        description = "Status of the moment (e.g., LIVE, DRAFT)",
        example = "LIVE",
        allowableValues = {"LIVE", "DRAFT", "PAUSED"})
    Status status,

    @Schema(
        description = "Short description of the moment",
        example = "A live music concert in the park.",
        maxLength = 255)
    String shortDescription,

    @Schema(
        description = "Image URL",
        example = "https://images.unsplash.com/photo-1501281668745-f7f57925c3b4?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        maxLength = 255)
    String thumbnail
) {}