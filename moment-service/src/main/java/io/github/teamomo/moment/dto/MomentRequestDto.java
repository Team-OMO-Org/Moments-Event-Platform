package io.github.teamomo.moment.dto;

import io.github.teamomo.moment.entity.Recurrence;
import io.github.teamomo.moment.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * MomentRequestDto is a Data Transfer Object (DTO) used for transferring moment-related data
 * between the client and server.
 *
 * <p>This DTO contains various fields that can be used to filter or search for moments based on
 * different criteria.
 */
@Schema(description = "Data Transfer Object for filtering or searching moments based on various criteria.")
public record MomentRequestDto(
    @Schema(description = "Filter by category (e.g., Music, Art)", example = "Music", maxLength = 50)
    @Size(max = 50, message = "Category must not exceed 50 characters")
    String category,

    @Schema(description = "Filter by location (e.g., New York, Los Angeles)", example = "New York", maxLength = 100)
    @Size(max = 100, message = "Location must not exceed 100 characters")
    String location,

    @Schema(description = "Filter by minimum price", example = "10.00", minimum = "0.0")
    @DecimalMin(value = "0.0", message = "Minimum price must be greater than or equal to 0")
    BigDecimal priceFrom,

    @Schema(description = "Filter by maximum price", example = "100.00", minimum = "0.0", exclusiveMinimum = true)
    @DecimalMin(value = "0.0", inclusive = false, message = "Maximum price must be greater than 0")
    BigDecimal priceTo,

    @Schema(description = "Filter by start date (from)", example = "2023-01-01T00:00:00", format = "date-time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startDateFrom,

    @Schema(description = "Filter by start date (to)", example = "2025-12-31T23:59:59", format = "date-time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startDateTo,

    @Schema(description = "Filter by recurrence type (e.g., ONETIME, REGULAR)", example = "ONETIME", allowableValues = {"ONETIME", "REGULAR"})
    @Pattern(regexp = "ONETIME|REGULAR", message = "Recurrence must be either 'ONETIME' or 'REGULAR'")
    Recurrence recurrence,

    @Schema(description = "Filter by status (e.g., LIVE, DRAFT, PAUSED)", example = "LIVE", allowableValues = {"LIVE", "DRAFT", "PAUSED"})
    @Pattern(regexp = "LIVE|DRAFT|PAUSED", message = "Status must be one of 'LIVE', 'DRAFT', or 'PAUSED'")
    Status status,

    @Schema(description = "Search by keyword in title, description, or short description", example = "concert", maxLength = 255)
    @Size(max = 255, message = "Search keyword must not exceed 255 characters")
    String search
) {}