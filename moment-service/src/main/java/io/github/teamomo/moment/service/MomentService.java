package io.github.teamomo.moment.service;

import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.entity.Moment;
import io.github.teamomo.moment.exception.MomentAlreadyExistsException;
import io.github.teamomo.moment.exception.ResourceNotFoundException;
import io.github.teamomo.moment.mapper.MomentMapper;
import io.github.teamomo.moment.repository.MomentRepository;
import java.time.LocalDateTime;
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


   //Method to create Moment with example of checking if the moment already exists and
   // throwing our custom MomentAlreadyExistsException
  public void createMoment(MomentDto momentDto) {
    Moment moment = momentMapper.toEntity(momentDto);
    // Check if the moment already exists in the database
    if (momentRepository.findByTitle(moment.getTitle()).isPresent()) {
      throw new MomentAlreadyExistsException("Moment already exists with given Title "
          + moment.getTitle());
    }
    momentRepository.save(moment); //should we return the saved moment as some ResponseMomentDto?
  }

   public MomentDto getMomentById(Long id) {
     return momentRepository.findById(id)
         .map(momentMapper::toDto)
         .orElseThrow(() -> new ResourceNotFoundException("Moment", "Id", id.toString()));
   }
}