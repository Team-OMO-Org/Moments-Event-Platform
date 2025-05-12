package io.github.teamomo.moment.mapper;

import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.dto.MomentFilterResponseDto;
import io.github.teamomo.moment.entity.Category;
import io.github.teamomo.moment.entity.Location;
import io.github.teamomo.moment.entity.Moment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MomentMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "locationId", source = "location.id")
    MomentDto toDto(Moment moment);

    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory")
    @Mapping(target = "location", source = "locationId", qualifiedByName = "mapLocation")
    Moment toEntity(MomentDto momentDto);

    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "location", source = "location.city")
    @Mapping(target = "description", source = "momentDetails.description")
    MomentFilterResponseDto toFilterResponseDto(Moment moment);

    @Named("mapCategory")
    default Category mapCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    @Named("mapLocation")
    default Location mapLocation(Long locationId) {
        if (locationId == null) {
            return null;
        }
        Location location = new Location();
        location.setId(locationId);
        return location;
    }
}