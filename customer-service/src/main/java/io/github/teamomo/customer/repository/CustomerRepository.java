package io.github.teamomo.customer.repository;

import io.github.teamomo.customer.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Find a customer by their Keycloak user ID.
     *
     * @param keycloakUserId the Keycloak user ID
     * @return the customer with the given Keycloak user ID, or null if not found
     */
    Optional<Customer> findByKeycloakUserId(String keycloakUserId);

}
