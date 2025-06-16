package io.github.teamomo.customer.service;

import io.github.teamomo.customer.dto.CustomerDto;
import io.github.teamomo.customer.entity.Customer;
import io.github.teamomo.customer.exception.KeycloakUserIdMismatchException;
import io.github.teamomo.customer.exception.ResourceNotFoundException;
import io.github.teamomo.customer.exception.UserNotAuthenticatedException;
import io.github.teamomo.customer.mapper.CustomerMapper;
import io.github.teamomo.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  // Checks if the user exists in the database by their Keycloak user ID and creates a new user if not.
 @Transactional
  public Long checkUserByKeycloakId(String keycloakUserId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
      Jwt jwt = (Jwt) authentication.getPrincipal();

      // Extract user details from JWT
      String jwtKeycloakUserId = jwt.getClaimAsString("sub");

      if (!jwtKeycloakUserId.equals(keycloakUserId)) {
        throw new KeycloakUserIdMismatchException("Keycloak user ID does not match the authenticated user");
      }

      String email = jwt.getClaimAsString("email");
      String name = jwt.getClaimAsString("name");

      Customer customer = customerRepository
          .findByKeycloakUserId(keycloakUserId)
          .orElseGet(() -> {
            Customer newUser = new Customer();
            newUser.setKeycloakUserId(keycloakUserId);
            newUser.setProfileEmail(email);
            newUser.setProfileName(name);
            return customerRepository.save(newUser);
          });

      // User already exists, update their details if necessary
      if (!customer.getProfileName().equalsIgnoreCase(name) || !customer.getProfileEmail().equalsIgnoreCase(email)) {
        customer.setProfileEmail(email);
        customer.setProfileName(name);
        customerRepository.save(customer);
      }
      return customer.getId();
    } else {
      throw new UserNotAuthenticatedException("User is not authenticated");
    }
  }

  // Gets a customer by ID
  public CustomerDto getCustomerById(Long id) {
    Customer customer = customerRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Customer with ID " + id + " not found", "id", id.toString()));

    return customerMapper.toDto(customer);
  }

  // Updates the customer details based on the provided ID and DTO.
  @Transactional
  public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
    Customer existingCustomer = customerRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Customer with ID " + id + " not found", "id", id.toString()));

    existingCustomer.setKeycloakUserId(customerDto.keycloakUserId());
    existingCustomer.setProfileName(customerDto.profileName());
    existingCustomer.setProfileEmail(customerDto.profileEmail());
    existingCustomer.setProfilePicture(customerDto.profilePicture());
    existingCustomer.setProfileSiteUrl(customerDto.profileSiteUrl());
    existingCustomer.setProfileDescription(customerDto.profileDescription());
    existingCustomer.setActive(customerDto.active());

    Customer updatedCustomer = customerRepository.save(existingCustomer);

    return customerMapper.toDto(updatedCustomer);
  }

  // Updates the active status of a customer by ID
  @Transactional
  public void updateCustomerActiveStatus(Long id, Boolean active) {
    Customer customer = customerRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Customer with ID " + id + " not found", "id", id.toString()));

    customer.setActive(active);
    customerRepository.save(customer);
  }

  // Deletes a customer by ID
  @Transactional
  public void deleteCustomer(Long id) {
    Customer customer = customerRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Customer with ID " + id + " not found", "id", id.toString()));

    customerRepository.delete(customer);
  }
}
