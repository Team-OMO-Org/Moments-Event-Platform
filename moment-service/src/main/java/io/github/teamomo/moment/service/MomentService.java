package io.github.teamomo.moment.service;

import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.entity.Moment;
import io.github.teamomo.moment.mapper.MomentMapper;
import io.github.teamomo.moment.repository.MomentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MomentService {
  private final MomentRepository momentRepository;
  private final MomentMapper momentMapper;

   public List<MomentDto> getAllMoments() {

     return  momentRepository.findAll()
         .stream()
         .map(momentMapper::toDto)
         .toList();

   }
}