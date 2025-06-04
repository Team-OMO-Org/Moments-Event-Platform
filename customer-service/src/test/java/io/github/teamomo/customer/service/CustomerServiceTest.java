package io.github.teamomo.customer.service;

import io.github.teamomo.customer.dto.CustomerDto;
import io.github.teamomo.customer.entity.Customer;
import io.github.teamomo.customer.exception.KeycloakUserIdMismatchException;
import io.github.teamomo.customer.exception.ResourceNotFoundException;
import io.github.teamomo.customer.exception.UserNotAuthenticatedException;
import io.github.teamomo.customer.mapper.CustomerMapper;
import io.github.teamomo.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private CustomerMapper customerMapper;

  @InjectMocks
  private CustomerService customerService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCheckUserByKeycloakId_UserExists() {
    String keycloakUserId = "existingUserId";
    String email = "test@example.com";
    String name = "Test User";

    Customer existingCustomer = new Customer();
    existingCustomer.setId(1L);
    existingCustomer.setKeycloakUserId(keycloakUserId);
    existingCustomer.setProfileEmail(email);
    existingCustomer.setProfileName(name);

    Jwt jwt = mock(Jwt.class);
    when(jwt.getClaimAsString("sub")).thenReturn(keycloakUserId);
    when(jwt.getClaimAsString("email")).thenReturn(email);
    when(jwt.getClaimAsString("name")).thenReturn(name);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(jwt);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    when(customerRepository.findByKeycloakUserId(keycloakUserId)).thenReturn(Optional.of(existingCustomer));

    Long result = customerService.checkUserByKeycloakId(keycloakUserId);

    assertNotNull(result);
    assertEquals(existingCustomer.getId(), result);
    verify(customerRepository, times(1)).findByKeycloakUserId(keycloakUserId);
  }

  @Test
  void testCheckUserByKeycloakId_UserDoesNotExist() {
    String keycloakUserId = "newUserId";
    String email = "new@example.com";
    String name = "New User";

    Jwt jwt = mock(Jwt.class);
    when(jwt.getClaimAsString("sub")).thenReturn(keycloakUserId);
    when(jwt.getClaimAsString("email")).thenReturn(email);
    when(jwt.getClaimAsString("name")).thenReturn(name);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(jwt);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    when(customerRepository.findByKeycloakUserId(keycloakUserId)).thenReturn(Optional.empty());

    Customer newCustomer = new Customer();
    newCustomer.setId(1L);
    newCustomer.setKeycloakUserId(keycloakUserId);
    newCustomer.setProfileEmail(email);
    newCustomer.setProfileName(name);

    when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

    Long result = customerService.checkUserByKeycloakId(keycloakUserId);

    assertNotNull(result);
    assertEquals(newCustomer.getId(), result);
    verify(customerRepository, times(1)).findByKeycloakUserId(keycloakUserId);
    verify(customerRepository, times(1)).save(any(Customer.class));
  }

  @Test
  void testCheckUserByKeycloakId_KeycloakIdMismatch() {
    String keycloakUserId = "mismatchedUserId";

    Jwt jwt = mock(Jwt.class);
    when(jwt.getClaimAsString("sub")).thenReturn("differentUserId");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(jwt);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    assertThrows(KeycloakUserIdMismatchException.class, () -> customerService.checkUserByKeycloakId(keycloakUserId));
    verify(customerRepository, never()).findByKeycloakUserId(anyString());
  }

  @Test
  void testCheckUserByKeycloakId_UserNotAuthenticated() {
    SecurityContextHolder.getContext().setAuthentication(null);

    assertThrows(UserNotAuthenticatedException.class, () -> customerService.checkUserByKeycloakId("anyUserId"));
    verify(customerRepository, never()).findByKeycloakUserId(anyString());
  }

  @Test
  void testGetCustomerById_Success() {
    Long customerId = 1L;
    Customer customer = new Customer();
    customer.setId(customerId);
    CustomerDto customerDto = new CustomerDto(customerId, "keycloakId", "name", "email", "picture", "url", "description", true);

    when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
    when(customerMapper.toDto(customer)).thenReturn(customerDto);

    CustomerDto result = customerService.getCustomerById(customerId);

    assertNotNull(result);
    assertEquals(customerId, result.id());
    verify(customerRepository, times(1)).findById(customerId);
    verify(customerMapper, times(1)).toDto(customer);
  }

  @Test
  void testGetCustomerById_NotFound() {
    Long customerId = 1L;

    when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(customerId));
    verify(customerRepository, times(1)).findById(customerId);
  }

  @Test
  void testUpdateCustomer_Success() {
    Long customerId = 1L;
    CustomerDto customerDto = new CustomerDto(customerId, "keycloakId", "name", "email", "picture", "url", "description", true);
    Customer customer = new Customer();
    customer.setId(customerId);

    when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
    when(customerRepository.save(customer)).thenReturn(customer);
    when(customerMapper.toDto(customer)).thenReturn(customerDto);

    CustomerDto result = customerService.updateCustomer(customerId, customerDto);

    assertNotNull(result);
    assertEquals(customerId, result.id());
    verify(customerRepository, times(1)).findById(customerId);
    verify(customerRepository, times(1)).save(customer);
    verify(customerMapper, times(1)).toDto(customer);
  }

  @Test
  void testUpdateCustomer_NotFound() {
    Long customerId = 1L;
    CustomerDto customerDto = new CustomerDto(customerId, "keycloakId", "name", "email", "picture", "url", "description", true);

    when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomer(customerId, customerDto));
    verify(customerRepository, times(1)).findById(customerId);
  }

  @Test
  void testUpdateCustomerActiveStatus_Success() {
    Long customerId = 1L;
    Boolean active = false;
    Customer customer = new Customer();
    customer.setId(customerId);

    when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

    customerService.updateCustomerActiveStatus(customerId, active);

    assertEquals(active, customer.getActive());
    verify(customerRepository, times(1)).findById(customerId);
    verify(customerRepository, times(1)).save(customer);
  }

  @Test
  void testUpdateCustomerActiveStatus_NotFound() {
    Long customerId = 1L;
    Boolean active = false;

    when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomerActiveStatus(customerId, active));
    verify(customerRepository, times(1)).findById(customerId);
  }

  @Test
  void testDeleteCustomer_Success() {
    Long customerId = 1L;
    Customer customer = new Customer();
    customer.setId(customerId);

    when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

    customerService.deleteCustomer(customerId);

    verify(customerRepository, times(1)).findById(customerId);
    verify(customerRepository, times(1)).delete(customer);
  }

  @Test
  void testDeleteCustomer_NotFound() {
    Long customerId = 1L;

    when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(customerId));
    verify(customerRepository, times(1)).findById(customerId);
  }
}