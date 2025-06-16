package io.github.teamomo.moment.controller;

import io.github.teamomo.moment.dto.CartItemDto;
import io.github.teamomo.moment.dto.CategoryDto;
import io.github.teamomo.moment.dto.CityDto;
import io.github.teamomo.moment.dto.ErrorResponseDto;
import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.dto.MomentRequestDto;
import io.github.teamomo.moment.dto.MomentResponseDto;
import io.github.teamomo.moment.entity.Category;
import io.github.teamomo.moment.service.MomentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    logger.info("Fetching all moments with filters: {}", momentRequestDto);
    Page<MomentResponseDto> momentsResponseDto = momentService.getAllMoments(momentRequestDto, pageable);
    logger.info("Successfully fetched {} moments", momentsResponseDto.getTotalElements());

    return momentsResponseDto;

  }

  @Operation(
      summary = "Retrieve all moments by host ID",
      description = "This endpoint retrieves a list of moments associated with a specific host ID.",
      tags = {"Moments"},
      parameters = {
          @Parameter(
              name = "id",
              description = "The ID of the host whose moments are to be retrieved",
              required = true,
              example = "1"
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "HTTP Status OK - Moments retrieved successfully"
          ),
          @ApiResponse(
              responseCode = "404",
              description = "HTTP Status Not Found - No moments found for the given host ID",
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
      }
  )
  @GetMapping("/host/{id}")
  @ResponseStatus(HttpStatus.OK)
  public List<MomentDto> getMomentsByHostId(@PathVariable Long id)
  {
    logger.info("Fetching all moments for host_id: {}", id);
    List<MomentDto> momentsDto = momentService.getMomentsByHostId(id);
    logger.info("Successfully fetched {} moments for host_id: {}", momentsDto.size(), id);

    return momentsDto;

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

  @Operation(
      summary = "Update a moment by ID",
      description = "This endpoint updates the details of a specific moment by its unique ID.",
      tags = {"Moments"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - Moment updated successfully"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "HTTP Status Bad Request - Invalid input data",
          content = @Content(
              schema = @Schema(implementation = ErrorResponseDto.class)
          )
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
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MomentDto updateMoment(@PathVariable Long id, @Valid @RequestBody MomentDto momentDto) {


    logger.info("Updating moment with ID: {}", id);
    MomentDto updatedMomentDto = momentService.updateMoment(id, momentDto);
    logger.info("Successfully updated moment with ID: {}", id);

    return updatedMomentDto;
  }

  @Operation(
      summary = "Delete a moment by ID",
      description = "This endpoint deletes a specific moment by its unique ID.",
      tags = {"Moments"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - Moment deleted successfully"
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

  @Operation(
      summary = "Retrieve all categories by moments count",
      description = "This endpoint retrieves a list of all categories, sorted by the number of moments associated with each category.",
      tags = {"Categories"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - Categories retrieved successfully"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "HTTP Status Internal Server Error",
          content = @Content(
              schema = @Schema(implementation = ErrorResponseDto.class)
          )
      )
  })
  @GetMapping("/categories")
  @ResponseStatus(HttpStatus.OK)
  public List<CategoryDto> getAllCategoriesByMomentsCount(){

    logger.info("Fetching all categories by moments count");
    List<CategoryDto> allCategoriesByMomentsCount = momentService.getAllCategoriesByMomentsCount();
    logger.info("Successfully fetched all categories by moments count: {}", allCategoriesByMomentsCount.size());

    return allCategoriesByMomentsCount;
  }

  @Operation(
      summary = "Retrieve a category by ID",
      description = "This endpoint retrieves the details of a specific category by its unique ID.",
      tags = {"Categories"}
  )
  @ApiResponses({
          @ApiResponse(
              responseCode = "200",
              description = "HTTP Status OK - Category retrieved successfully"
          ),
          @ApiResponse(
              responseCode = "404",
              description = "HTTP Status Not Found - Category not found for the given ID",
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
      }
  )
  @GetMapping("/categories/{id}")
  @ResponseStatus(HttpStatus.OK)
  public CategoryDto getCategoryById(@PathVariable Long id){

    logger.info("Fetching category by id: {}", id);
    CategoryDto categoryDto = momentService.getCategoryById(id);
    logger.info("Successfully fetched category by id: {}", id);

    return categoryDto;
  }

  @Operation(
      summary = "Retrieve all cities by moments count",
      description = "This endpoint retrieves a list of all cities, sorted by the number of moments associated with each city.",
      tags = {"Cities"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - Cities retrieved successfully"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "HTTP Status Internal Server Error",
          content = @Content(
              schema = @Schema(implementation = ErrorResponseDto.class)
          )
      )
  })
  @GetMapping("/cities")
  @ResponseStatus(HttpStatus.OK)
  public List<CityDto> getAllCitiesByMomentsCount(){

    logger.info("Fetching all cities by moments count");
    List<CityDto> allCitiesByMomentsCount = momentService.getAllCitiesByMomentsCount();
    logger.info("Successfully fetched all cities by moments count: {}", allCitiesByMomentsCount.size());

    return allCitiesByMomentsCount;
  }


  @Operation(
      summary = "Retrieve cart items by moment IDs",
      description = "This endpoint retrieves a list of cart items based on the provided list of moment IDs.",
      tags = {"Cart Items"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - Cart items retrieved successfully"
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
  @PostMapping("/cart-items")
  @ResponseStatus(HttpStatus.OK)
  public List<CartItemDto> getCartItems(@RequestBody List<Long> momentIds){

    logger.info("Fetching all cart items by moment ids");
    List<CartItemDto> cartItems = momentService.getCartItems(momentIds);
    logger.info("Successfully fetched all cart items by moment ids: {}", cartItems.size());

    return cartItems;
  }

  @Operation(
      summary = "Check ticket availability for a specific moment",
      description = "This endpoint checks if the required number of tickets are available for a specific moment by its ID.",
      tags = {"Moments"},
      parameters = {
          @Parameter(
              name = "id",
              description = "The ID of the moment to check ticket availability for",
              required = true,
              example = "1"
          ),
          @Parameter(
              name = "requiredTickets",
              description = "The number of tickets required",
              required = true,
              example = "5"
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "HTTP Status OK - Returns true if tickets are available, false otherwise"
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
      }
  )
  @GetMapping("/{id}/check-availability")
  @ResponseStatus(HttpStatus.OK)
  public boolean checkTicketAvailability(
      @PathVariable Long id,
      @RequestParam int requiredTickets) {

    logger.info("Checking ticket availability for moment ID: {} with required tickets: {}", id, requiredTickets);
    boolean isAvailable = momentService.checkTicketAvailability(id, requiredTickets);
    logger.info("Ticket availability for moment ID {}: {}", id, isAvailable);

    return isAvailable;
  }

  @Operation(
      summary = "Book tickets for a specific moment",
      description = "This endpoint allows booking a specified number of tickets for a moment by its ID.",
      tags = {"Moments"},
      parameters = {
          @Parameter(
              name = "id",
              description = "The ID of the moment to book tickets for",
              required = true,
              example = "1"
          ),
          @Parameter(
              name = "requiredTickets",
              description = "The number of tickets to book",
              required = true,
              example = "5"
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "HTTP Status OK - Tickets booked successfully"
          ),
          @ApiResponse(
              responseCode = "400",
              description = "HTTP Status Bad Request - Invalid ticket count or insufficient tickets",
              content = @Content(
                  schema = @Schema(implementation = ErrorResponseDto.class)
              )
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
      }
  )
  @PostMapping("/{id}/book-tickets")
  @ResponseStatus(HttpStatus.OK)
  public BigDecimal bookTickets(@PathVariable Long id, @RequestParam int requiredTickets) {
    logger.info("Booking {} tickets for moment ID: {}", requiredTickets, id);
    BigDecimal totalSum =  momentService.bookTickets(id, requiredTickets);
    logger.info("Successfully booked {} tickets for moment ID: {}", requiredTickets, id);
    return totalSum;
  }

  @Operation(
      summary = "Cancel ticket booking if Payment fails for a specific moment",
      description = "This endpoint allows canceling a specified number of tickets if Payment fails for a moment by its ID.",
      tags = {"Moments"},
      parameters = {
          @Parameter(
              name = "id",
              description = "The ID of the moment to cancel tickets for",
              required = true,
              example = "1"
          ),
          @Parameter(
              name = "ticketsToCancel",
              description = "The number of tickets to cancel",
              required = true,
              example = "2"
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "HTTP Status OK - Tickets canceled successfully"
          ),
          @ApiResponse(
              responseCode = "400",
              description = "HTTP Status Bad Request - Invalid ticket count",
              content = @Content(
                  schema = @Schema(implementation = ErrorResponseDto.class)
              )
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
      }
  )
  @PostMapping("/{id}/cancel-tickets")
  @ResponseStatus(HttpStatus.OK)
  public void cancelTicketBooking(
      @PathVariable Long id,
      @RequestParam int ticketsToCancel) {

    logger.info("Cancelling {} tickets for moment ID: {}", ticketsToCancel, id);
    momentService.cancelTicketBooking(id, ticketsToCancel);
    logger.info("Successfully cancelled {} tickets for moment ID: {}", ticketsToCancel, id);
  }
}
