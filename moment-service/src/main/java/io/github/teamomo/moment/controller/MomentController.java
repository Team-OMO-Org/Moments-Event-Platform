package io.github.teamomo.moment.controller;

import io.github.teamomo.moment.dto.CartItemDto;
import io.github.teamomo.moment.dto.CategoryDto;
import io.github.teamomo.moment.dto.CityDto;
import io.github.teamomo.moment.dto.ErrorResponseDto;
import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.dto.MomentRequestDto;
import io.github.teamomo.moment.dto.MomentResponseDto;
import io.github.teamomo.moment.service.MomentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "REST APIs for Moments Service in MomentsPlatform",
    description = "REST APIs in MomentsPlatform to FETCH, CREATE, UPDATE moments and their details, " +
        "FETCH categories and locations"
)
@RestController
@RequestMapping("/api/v1/moments")
@RequiredArgsConstructor
public class MomentController {

  private static final Logger logger = LoggerFactory.getLogger(MomentController.class);

  private final MomentService momentService;

  @Operation(
      summary = "Retrieve all moments with optional filters",
      description = "This endpoint retrieves a paginated list of moments. You can apply various filters such as:\n\n" +
          "- **Default Request (No Filters):** [http://localhost:8081/api/v1/moments?page=0&size=10&sort=startDate,asc](http://localhost:8081/api/v1/moments?page=0&size=10&sort=startDate,asc)\n" +
          "- **Filter by Category and Location:** [http://localhost:8081/api/v1/moments?category=Music&location=New%20York&page=0&size=5](http://localhost:8081/api/v1/moments?category=Music&location=New%20York&page=0&size=5)\n" +
          "- **Filter by Price Range:** [http://localhost:8081/api/v1/moments?priceFrom=10&priceTo=100&page=0&size=10](http://localhost:8081/api/v1/moments?priceFrom=10&priceTo=100&page=0&size=10)\n" +
          "- **Filter by Date Range:** [http://localhost:8081/api/v1/moments?startDateFrom=2023-01-01T00:00:00&startDateTo=2025-12-31T23:59:59&page=0&size=10](http://localhost:8081/api/v1/moments?startDateFrom=2023-01-01T00:00:00&startDateTo=2025-12-31T23:59:59&page=0&size=10)\n" +
          "- **Combined Filters:** [http://localhost:8081/api/v1/moments?category=Art&location=Los%20Angeles&priceFrom=20&priceTo=200&startDateFrom=2023-06-01T00:00:00&startDateTo=2025-07-30T23:59:59&page=0&size=10&sort=startDate,desc](http://localhost:8081/api/v1/moments?category=Art&location=Los%20Angeles&priceFrom=20&priceTo=200&startDateFrom=2023-06-01T00:00:00&startDateTo=2025-07-30T23:59:59&page=0&size=10&sort=startDate,desc)\n" +
          "- **Search by Keyword:** [http://localhost:8081/api/v1/moments?search=concert&page=0&size=10](http://localhost:8081/api/v1/moments?search=concert&page=0&size=10)",
      tags = {"Moments"},
      parameters = {
          @Parameter(name = "category", description = "Filter by category (e.g., Music, Art)", example = "Music"),
          @Parameter(name = "location", description = "Filter by location (e.g., New York, Los Angeles)", example = "New York"),
          @Parameter(name = "priceFrom", description = "Filter by minimum price", example = "10"),
          @Parameter(name = "priceTo", description = "Filter by maximum price", example = "100"),
          @Parameter(name = "startDateFrom", description = "Filter by start date (from)", example = "2023-01-01T00:00:00"),
          @Parameter(name = "startDateTo", description = "Filter by start date (to)", example = "2025-12-31T23:59:59"),
          @Parameter(name = "search", description = "Search by keyword in title, description, or short description", example = "concert"),
          @Parameter(name = "page", description = "Page number for pagination", example = "0"),
          @Parameter(name = "size", description = "Page size for pagination", example = "10"),
          @Parameter(name = "sort", description = "Sorting criteria (e.g., startDate,asc)", example = "startDate,asc")
      }
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "HTTP Status Internal Server Error",
          content = @Content(
              schema = @Schema(implementation = ErrorResponseDto.class)
          )
      )
  }
  )
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<MomentResponseDto> getAllMoments(
      MomentRequestDto momentRequestDto,
      @PageableDefault(size = 12, sort = "startDate") Pageable pageable
  ) {
    // ToDo: Default filtering: LIVE, FUTURE
    logger.info("Fetching all moments with filters: {}", momentRequestDto);
    Page<MomentResponseDto> momentsResponseDto = momentService.getAllMoments(momentRequestDto, pageable);
    logger.info("Successfully fetched {} moments", momentsResponseDto.getTotalElements());

    return momentsResponseDto;

  }

  @Operation(
      summary = "Create a new moment",
      description = "This endpoint allows you to create a new moment by providing the necessary details in the request body.",
      tags = {"Moments"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "201",
          description = "HTTP Status Created - Moment created successfully"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "HTTP Status Bad Request - Invalid input data",
          content = @Content(
              schema = @Schema(implementation = ErrorResponseDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "500",
          description = "HTTP Status Internal Server Error",
          content = @Content(
              schema = @Schema(implementation = ErrorResponseDto.class)
          )
      )
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MomentDto createMoment(@Valid @RequestBody MomentDto momentDto) {

    logger.info("Creating new moment with details: {}", momentDto);
    MomentDto createdMomentDto = momentService.createMoment(momentDto);
    logger.info("Successfully created moment with ID: {}", createdMomentDto.id());

    return createdMomentDto;
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MomentDto updateMoment(@PathVariable Long id, @Valid @RequestBody MomentDto momentDto) {


    logger.info("Updating moment with ID: {}", id);
    MomentDto updatedMomentDto = momentService.updateMoment(id, momentDto);
    logger.info("Successfully updated moment with ID: {}", id);

    return updatedMomentDto;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteMoment(@PathVariable Long id) {

    logger.info("Deleting moment with ID: {}", id);
    momentService.deleteMoment(id);
    logger.info("Successfully deleted moment with ID: {}", id);
  }

  @Operation(
      summary = "Retrieve a moment by its ID",
      description = "This endpoint retrieves the details of a specific moment by its unique ID.",
      tags = {"Moments"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - Moment retrieved successfully"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "HTTP Status Not Found - Moment not found for the given ID",
          content = @Content(
              schema = @Schema(implementation = ErrorResponseDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "500",
          description = "HTTP Status Internal Server Error",
          content = @Content(
              schema = @Schema(implementation = ErrorResponseDto.class)
          )
      )
  })
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MomentDto getMomentById(@PathVariable Long id) {

    logger.info("Fetching momentDto with ID: {}", id);
    MomentDto momentDto = momentService.getMomentById(id);
    logger.info("Successfully fetched momentDto: {}", momentDto);

    return momentDto;
  }

  @GetMapping("/categories")
  @ResponseStatus(HttpStatus.OK)
  public List<CategoryDto> getAllCategoriesByMomentsCount(){

    logger.info("Fetching all categories by moments count");
    List<CategoryDto> allCategoriesByMomentsCount = momentService.getAllCategoriesByMomentsCount();
    logger.info("Successfully fetched all categories by moments count: {}", allCategoriesByMomentsCount.size());

    return allCategoriesByMomentsCount;
  }

  @GetMapping("/cities")
  @ResponseStatus(HttpStatus.OK)
  public List<CityDto> getAllCitiesByMomentsCount(){

    logger.info("Fetching all cities by moments count");
    List<CityDto> allCitiesByMomentsCount = momentService.getAllCitiesByMomentsCount();
    logger.info("Successfully fetched all cities by moments count: {}", allCitiesByMomentsCount.size());

    return allCitiesByMomentsCount;
  }

  @PostMapping("/cart-items")
  @ResponseStatus(HttpStatus.OK)
  public List<CartItemDto> getCartItems(@RequestBody List<Long> momentIds){

    logger.info("Fetching all cart items by moment ids");
    List<CartItemDto> cartItems = momentService.getCartItems(momentIds);
    logger.info("Successfully fetched all cart items by moment ids: {}", cartItems.size());

    return cartItems;
  }
}
