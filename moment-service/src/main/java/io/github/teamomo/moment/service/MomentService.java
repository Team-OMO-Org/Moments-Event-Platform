package io.github.teamomo.moment.service;

import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.dto.MomentRequestDto;
import io.github.teamomo.moment.dto.MomentResponseDto;
import io.github.teamomo.moment.entity.Category;
import io.github.teamomo.moment.entity.Moment;
import io.github.teamomo.moment.entity.MomentDetail;
import io.github.teamomo.moment.exception.MomentAlreadyExistsException;
import io.github.teamomo.moment.exception.ResourceNotFoundException;
import io.github.teamomo.moment.mapper.MomentMapper;
import io.github.teamomo.moment.repository.CategoryRepository;
import io.github.teamomo.moment.repository.LocationRepository;
import io.github.teamomo.moment.repository.MomentDetailRepository;
import io.github.teamomo.moment.repository.MomentRepository;
import java.time.Instant;
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

  public Page<Moment> getAllMoments(Instant startDate, Pageable pageable) {
    return momentRepository.findAllByStartDateAfter(startDate, pageable);
//         .map(momentMapper::toDto);
  }

//todo: maybe findByHostIdAndTitleAndStartDate will be better
  public MomentDto createMoment(MomentDto momentDto) {

    //check if moment with the same title and day already exists
    if(momentRepository.findByTitleAndStartDate(momentDto.title(), momentDto.startDate()).isPresent()){
      throw new MomentAlreadyExistsException("Moment already exists with given Title '"
          + momentDto.title() + "' and start date: " + momentDto.startDate());
    }
    Category category = categoryRepository.findById(momentDto.categoryId())
        .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", momentDto.categoryId().toString()));

    Moment moment = momentMapper.toEntity(momentDto);
    moment.setCategory(category);
    MomentDetail momentDetails = momentDto.momentDetails();

    if (momentDetails == null) {
      momentDetails = new MomentDetail();
      momentDetails.setDescription("");
    }
      momentDetails.setMoment(moment);
      moment.setMomentDetails(momentDetails);
    Moment savedMoment = momentRepository.save(moment);


    return momentMapper.toDto(savedMoment);
  }

  public MomentDto updateMoment(Long id, MomentDto momentDto) {
    Moment moment = momentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Moment", "Id", id.toString()));

    Moment updatedMoment = momentMapper.toEntity(momentDto);
    updatedMoment.setId(moment.getId());

    if(momentDto.categoryId() != null){
      Category category = categoryRepository.findById(momentDto.categoryId())
          .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", momentDto.categoryId().toString()));
      updatedMoment.setCategory(category);
    }

    MomentDetail existMomentDetails = moment.getMomentDetails();

    if(momentDto.momentDetails() != null){
      existMomentDetails.setDescription(momentDto.momentDetails().getDescription());
    }

    updatedMoment.setMomentDetails(existMomentDetails);

    Moment savedMoment = momentRepository.save(updatedMoment);

    return momentMapper.toDto(savedMoment);
  }


  public MomentDto getMomentById(Long id) {
    return momentRepository.findById(id)
        .map(momentMapper::toDto)
        .orElseThrow(() -> new ResourceNotFoundException("Moment", "Id", id.toString()));
  }

  public Page<MomentResponseDto> getAllMoments(MomentRequestDto momentRequestDto,
      Pageable pageable) {

    Page<Moment> moments = momentRepository.findByFilters(
        momentRequestDto.category(),
        momentRequestDto.location(),
        momentRequestDto.priceFrom(),
        momentRequestDto.priceTo(),
        momentRequestDto.startDateFrom(),
        momentRequestDto.startDateTo(),
        momentRequestDto.recurrence(),
        momentRequestDto.status(),
        momentRequestDto.search(),
        pageable
    );
    return moments.map(momentMapper::toFilterResponseDto);
  }
}