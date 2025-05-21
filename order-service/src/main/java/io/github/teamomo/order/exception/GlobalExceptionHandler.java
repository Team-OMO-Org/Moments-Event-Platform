package io.github.teamomo.order.exception;


import io.github.teamomo.moment.dto.ErrorResponseDto;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status,
      WebRequest request) {
    Map<String, String> validationErrors = new HashMap<>();
    List<ObjectError> validationErrorList = exception.getBindingResult().getAllErrors();

    validationErrorList.forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String validationMsg = error.getDefaultMessage();
      validationErrors.put(fieldName, validationMsg);
    });

    ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
        request.getDescription(false),
        HttpStatus.BAD_REQUEST,
        validationErrors.toString(),
        LocalDateTime.now()
    );

    logger.error("Validation failed for request: {}. Errors: {}", request.getDescription(false), validationErrors, exception);

    return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<ErrorResponseDto> handleResourceAlreadyExistsException(
      ResourceAlreadyExistsException exception,
      WebRequest webRequest) {
    ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
        webRequest.getDescription(false),
        HttpStatus.BAD_REQUEST,
        exception.getMessage(),
        LocalDateTime.now()
    );

    logger.error("Resource already exists: {}. Request details: {}", exception.getMessage(), webRequest.getDescription(false), exception);

    return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(
      ResourceNotFoundException exception,
      WebRequest webRequest) {
    ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
        webRequest.getDescription(false),
        HttpStatus.NOT_FOUND,
        exception.getMessage(),
        LocalDateTime.now()
    );

    logger.error("Resource not found: {}. Request details: {}", exception.getMessage(), webRequest.getDescription(false), exception);

    return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CartIsEmptyException.class)
  public ResponseEntity<ErrorResponseDto> handleCartIsEmptyException(
      CartIsEmptyException exception, WebRequest webRequest) {
    ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
        webRequest.getDescription(false),
        HttpStatus.BAD_REQUEST,
        exception.getMessage(),
        LocalDateTime.now()
    );

    logger.error("Cart is empty: {}. Request details: {}", exception.getMessage(), webRequest.getDescription(false), exception);

    return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PaymentProcessingException.class)
  public ResponseEntity<ErrorResponseDto> handlePaymentProcessingException(
      PaymentProcessingException exception, WebRequest webRequest) {
    ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
        webRequest.getDescription(false),
        HttpStatus.INTERNAL_SERVER_ERROR,
        exception.getMessage(),
        LocalDateTime.now()
    );

    logger.error("Payment processing error: {}. Request details: {}", exception.getMessage(), webRequest.getDescription(false), exception);

    return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception,
      WebRequest webRequest) {
    ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
        webRequest.getDescription(false),
        HttpStatus.INTERNAL_SERVER_ERROR,
        exception.getMessage(),
        LocalDateTime.now()
    );

    logger.error("Unexpected error occurred: {}. Request details: {}", exception.getMessage(), webRequest.getDescription(false), exception);

    return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
