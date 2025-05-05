package io.github.teamomo.moment.mapper;

import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.entity.Moment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MomentMapper {
  MomentDto toDto(Moment Moment);
  Moment toEntity(MomentDto MomentDto);
}