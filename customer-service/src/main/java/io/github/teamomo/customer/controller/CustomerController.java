package io.github.teamomo.customer.controller;

import io.github.teamomo.customer.dto.CustomerDto;
import io.github.teamomo.customer.dto.ErrorResponseDto;
import io.github.teamomo.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

  private final CustomerService customerService;

  @Operation(
      summary = "Check if a customer exists by Keycloak user ID",
      description = "This endpoint checks if a customer exists in the database by their Keycloak user ID. If not, it creates a new customer.",
      tags = {"customers"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - customer check completed successfully"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "HTTP Status Internal Server Error",
          content = @Content(
              schema = @Schema(implementation = ErrorResponseDto.class)
          )
      )
  })
  @PostMapping("/check")
  public Long checkUserByKeycloakId(@RequestBody String keycloakUserId) {
    log.info("Checking customer by Keycloak user ID: {}", keycloakUserId);
    Long customerId = customerService.checkUserByKeycloakId(keycloakUserId);
    log.info("Customer check completed for Keycloak user ID: {}, customer ID {}:  ", keycloakUserId, customerId);
    return customerId;
  }

  @Operation(
      summary = "Retrieve a customer by its ID",
      description = "This endpoint retrieves the details of a specific customer by its unique ID.",
      tags = {"customers"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - customer retrieved successfully"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "HTTP Status Not Found - customer not found for the given ID",
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
  public CustomerDto getCustomerById(@PathVariable Long id) {

    log.info("Fetching customerDto with ID: {}", id);
    CustomerDto customerDto = customerService.getCustomerById(id);
    log.info("Successfully fetched CustomerDto: {}", customerDto);

    return customerDto;
  }

  @Operation(
      summary = "Update a customer by ID",
      description = "This endpoint updates the details of a specific customer by their unique ID.",
      tags = {"customers"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - customer updated successfully"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "HTTP Status Not Found - customer not found for the given ID",
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
  public CustomerDto updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDto customerDto) {


    log.info("Updating customer with ID: {}", id);
    CustomerDto updatedCustomerDto = customerService.updateCustomer(id, customerDto);
    log.info("Successfully updated customer with ID: {}", id);

    return updatedCustomerDto;
  }

  @Operation(
      summary = "Update the active status of a customer",
      description = "This endpoint updates the active status of a specific customer by their unique ID.",
      tags = {"customers"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - active status updated successfully"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "HTTP Status Not Found - customer not found for the given ID",
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
  @PatchMapping("/{id}/active")
  @ResponseStatus(HttpStatus.OK)
  public void updateCustomerActiveStatus(@PathVariable Long id, @RequestBody Boolean active) {
    log.info("Updating active status for customer with ID: {}", id);
    customerService.updateCustomerActiveStatus(id, active);
    log.info("Successfully updated active status for customer with ID: {}", id);
  }

  @Operation(
      summary = "Delete a customer by ID",
      description = "This endpoint deletes a specific customer by their unique ID.",
      tags = {"customers"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - customer deleted successfully"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "HTTP Status Not Found - customer not found for the given ID",
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
  public void deleteCustomer(@PathVariable Long id) {

    log.info("Deleting customer with ID: {}", id);
    customerService.deleteCustomer(id);
    log.info("Successfully deleted customer with ID: {}", id);
  }
}
