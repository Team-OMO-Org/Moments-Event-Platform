package io.github.teamomo.moment.service;

import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.entity.Moment;
import io.github.teamomo.moment.exception.ResourceNotFoundException;
import io.github.teamomo.moment.mapper.MomentMapper;
import io.github.teamomo.moment.repository.CategoryRepository;
import io.github.teamomo.moment.repository.LocationRepository;
import io.github.teamomo.moment.repository.MomentDetailRepository;
import io.github.teamomo.moment.repository.MomentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MomentService {
  private final MomentRepository momentRepository;
  private final MomentMapper momentMapper;
  private final MomentDetailRepository momentDetailRepository;  // ToDo: check if we need this
  private final LocationRepository locationRepository;
  private final CategoryRepository categoryRepository;

   public Page<MomentDto> getAllMoments(Pageable pageable) {
     return  momentRepository.findAll(pageable)
         .map(momentMapper::toDto);
   }

   // Method to create a Moment with example of checking if the moment already exists and
   // throwing our custom MomentAlreadyExistsException
  public void createMoment(MomentDto momentDto) {
    Moment moment = momentMapper.toEntity(momentDto);
    // Check if the moment already exists in the database
//    if (momentRepository.findByTitle(moment.getTitle()).isPresent()) {
//      throw new MomentAlreadyExistsException("Moment already exists with given Title "
//          + moment.getTitle());
//    }
    momentRepository.save(moment); //should we return the saved moment as some ResponseMomentDto?
  }

   public MomentDto getMomentById(Long id) {
     return momentRepository.findById(id)
         .map(momentMapper::toDto)
         .orElseThrow(() -> new ResourceNotFoundException("Moment", "Id", id.toString()));
   }
}