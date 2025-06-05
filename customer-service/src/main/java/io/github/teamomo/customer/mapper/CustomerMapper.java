package io.github.teamomo.customer.mapper;

import io.github.teamomo.customer.dto.CustomerDto;
import io.github.teamomo.customer.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
  // Maps fields one-to-one from Customer to CustomerDto and vice versa
  @Mapping(target = "id", source = "id")
  @Mapping(target = "keycloakUserId", source = "keycloakUserId")
  @Mapping(target = "profileName", source = "profileName")
  @Mapping(target = "profileEmail", source = "profileEmail")
  @Mapping(target = "profilePicture", source = "profilePicture")
  @Mapping(target = "profileSiteUrl", source = "profileSiteUrl")
  @Mapping(target = "profileDescription", source = "profileDescription")
  @Mapping(target = "active", source = "active")
  CustomerDto toDto(Customer customer);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "keycloakUserId", source = "keycloakUserId")
  @Mapping(target = "profileName", source = "profileName")
  @Mapping(target = "profileEmail", source = "profileEmail")
  @Mapping(target = "profilePicture", source = "profilePicture")
  @Mapping(target = "profileSiteUrl", source = "profileSiteUrl")
  @Mapping(target = "profileDescription", source = "profileDescription")
  @Mapping(target = "active", source = "active")
  Customer toEntity(CustomerDto customerDto);
}