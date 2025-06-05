package io.github.teamomo.moment.mapper;

import io.github.teamomo.moment.dto.CartItemDto;
import io.github.teamomo.moment.dto.CategoryDto;
import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.dto.MomentResponseDto;
import io.github.teamomo.moment.entity.Category;
import io.github.teamomo.moment.entity.Moment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MomentMapper {

    @Mapping(target = "categoryId", source = "category.id")
    MomentDto toDto(Moment moment);

    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory")
    Moment toEntity(MomentDto momentDto);

    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "location", source = "location.city")
    MomentResponseDto toFilterResponseDto(Moment moment);

    @Named("mapCategory")
    default Category mapCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    @Mapping(target = "momentId", source = "id")
    CartItemDto toCartItemDto(Moment moment);

    @Mapping(target = "categoryId", source = "id")
    @Mapping(target = "categoryName", source = "name")
    CategoryDto toCategoryDto(Category category);
}