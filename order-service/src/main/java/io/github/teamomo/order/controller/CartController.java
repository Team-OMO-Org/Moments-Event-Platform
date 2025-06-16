package io.github.teamomo.order.controller;

import io.github.teamomo.moment.dto.ErrorResponseDto;
import io.github.teamomo.order.dto.CartDto;
import io.github.teamomo.order.dto.CartItemInfoDto;
import io.github.teamomo.order.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "REST APIs for Cart Service in MomentsPlatform",
    description = "REST APIs in MomentsPlatform to FETCH, CREATE, UPDATE, and DELETE carts and their items as part of the Order Service"
)
@RestController
@RequestMapping("/api/v1/orders/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {

  private final CartService cartService;

  @Operation(
      summary = "Retrieve a cart by customer ID",
      description = "This endpoint retrieves the details of a cart associated with a specific customer ID. If no cart exists for the given customer ID, a new cart will be created and returned.",
      tags = {"Carts"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - Cart retrieved successfully"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "HTTP Status Not Found - Cart not found",
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
  @GetMapping("/{customerId}")
  public CartDto getCartByCustomerId(@PathVariable Long customerId) {
    log.info("Fetching cart for customer ID: {}", customerId);
    return cartService.findCartByCustomerId(customerId);
  }

  @Operation(
      summary = "Create a cart for a customer",
      description = "This endpoint creates a new cart for the specified customer ID. If a cart already exists for the customer, an error will be returned.",
      tags = {"Carts"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "201",
          description = "HTTP Status Created - Cart created successfully"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "HTTP Status Bad Request - Cart already exists for the customer with provided ID",
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
  @PostMapping("/{customerId}")
  @ResponseStatus(HttpStatus.CREATED)
  public CartDto createCart(@PathVariable Long customerId) {
    log.info("Creating cart for customer ID: {}", customerId);
    return cartService.createCart(customerId);
  }


  @Operation(
      summary = "Update a cart for a customer",
      description = "This endpoint updates the cart for the specified customer ID with the provided cart details.",
      tags = {"Carts"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - Cart updated successfully"
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
          description = "HTTP Status Not Found - Cart not found for the given customer ID",
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
  @PutMapping("/{customerId}")
  @ResponseStatus(HttpStatus.OK)
  public CartDto updateCart(@PathVariable Long customerId, @Valid @RequestBody CartDto cartDto) {

    log.info("Updating cart for customer ID: {}", cartDto.cartItems().toString());
    return cartService.updateCart(customerId, cartDto);
  }

  @Operation(
      summary = "Delete a cart by customer ID",
      description = "This endpoint deletes the cart associated with the specified customer ID. If no cart exists for the given customer ID, an error will be returned.",
      tags = {"Carts"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - Cart deleted successfully"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "HTTP Status Not Found - Cart not found for the given customer ID",
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
  @DeleteMapping("/{customerId}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteCart(@PathVariable Long customerId) {
    log.info("Deleting cart for customer ID: {}", customerId);
    cartService.deleteCart(customerId);
  }

  @Operation(
      summary = "Retrieve all cart items for a customer",
      description = "This endpoint retrieves a list of all cart items associated with the specified customer ID.",
      tags = {"Cart Items"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - Cart items retrieved successfully"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "HTTP Status Not Found - Customer or cart not found",
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
  @GetMapping("/{customerId}/items")
  public List<CartItemInfoDto> getAllCartItems(@PathVariable Long customerId) {
    log.info("Getting all cart items for customer ID: {}", customerId);
    return cartService.getAllCartItems(customerId);
  }

  @Operation(
      summary = "Create a cart item for a customer",
      description = "This endpoint creates a new cart item for the specified customer ID with the provided cart item details.",
      tags = {"Cart Items"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "201",
          description = "HTTP Status Created - Cart item created successfully"
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
          description = "HTTP Status Not Found - Customer or cart not found",
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
  @PostMapping("/{customerId}/items")
  @ResponseStatus(HttpStatus.CREATED)
  public CartItemInfoDto createCartItem(@PathVariable Long customerId, @Valid @RequestBody CartItemInfoDto cartItemDto) {
    log.info("Creating cart item for customer ID: {}", customerId);
    return cartService.createCartItem(customerId, cartItemDto);
  }


  @Operation(
      summary = "Update a cart item by ID for a specific customer",
      description = "This endpoint updates the details of a specific cart item identified by its ID for the given customer ID.",
      tags = {"Cart Items"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - Cart item updated successfully"
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
          description = "HTTP Status Not Found - Cart item not found for the given customer ID and item ID",
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
  @PutMapping("/{customerId}/items/{itemId}")
  @ResponseStatus(HttpStatus.OK)
  public CartItemInfoDto updateCartItem(@PathVariable Long customerId, @PathVariable Long itemId, @Valid @RequestBody CartItemInfoDto cartItemDto) {
    log.info("Updating cart item with ID: {}", itemId);
    return cartService.updateCartItem(itemId, cartItemDto);
  }

  @Operation(
      summary = "Delete a cart item by ID for a specific customer",
      description = "This endpoint deletes a specific cart item identified by its ID for the given customer ID.",
      tags = {"Cart Items"}
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "HTTP Status OK - Cart item deleted successfully"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "HTTP Status Not Found - Cart item not found for the given customer ID and item ID",
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
  @DeleteMapping("/{customerId}/items/{itemId}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteCartItem(@PathVariable Long customerId, @PathVariable Long itemId) {
    log.info("Deleting cart item with ID: {}", itemId);
    cartService.deleteCartItem(itemId);
  }
}
