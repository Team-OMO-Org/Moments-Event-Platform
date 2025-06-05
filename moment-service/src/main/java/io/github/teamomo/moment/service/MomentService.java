package io.github.teamomo.moment.service;

import io.github.teamomo.moment.dto.CartItemDto;
import io.github.teamomo.moment.dto.CategoryDto;
import io.github.teamomo.moment.dto.CityDto;
import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.dto.MomentRequestDto;
import io.github.teamomo.moment.dto.MomentResponseDto;
import io.github.teamomo.moment.entity.Category;
import io.github.teamomo.moment.entity.Moment;
import io.github.teamomo.moment.entity.MomentDetail;
import io.github.teamomo.moment.exception.InsufficientTicketsException;
import io.github.teamomo.moment.exception.MomentAlreadyExistsException;
import io.github.teamomo.moment.exception.ResourceNotFoundException;
import io.github.teamomo.moment.mapper.MomentMapper;
import io.github.teamomo.moment.repository.CategoryRepository;
import io.github.teamomo.moment.repository.LocationRepository;
import io.github.teamomo.moment.repository.MomentDetailRepository;
import io.github.teamomo.moment.repository.MomentRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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
    Moment moment = getMoment(id);

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

  //should add here DeleteMomentDto(message, id) as a return?
  public void deleteMoment(Long id) {
    Moment moment = getMoment(id);

    momentRepository.delete(moment);
    //todo: add logging with message

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

  public List<CategoryDto> getAllCategoriesByMomentsCount(){
    return categoryRepository.findAllByMomentsCount();
  }

  public CategoryDto getCategoryById(Long id){
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", id.toString()));
    return momentMapper.toCategoryDto(category);
  }

  public List<CityDto> getAllCitiesByMomentsCount(){
    return locationRepository.findAllByMomentsCount();
  }

  public List<CartItemDto> getCartItems(List<Long> momentIds) {
    List<CartItemDto> cartItemDtos = momentRepository.findAllById(momentIds)
        .stream()
        .map(momentMapper::toCartItemDto)
        .toList();
    return cartItemDtos;
  }

  public boolean checkTicketAvailability(Long momentId, int requiredTickets) {
    Moment moment = getMoment(momentId);

    return moment.getTicketCount() >= requiredTickets;
  }

  public BigDecimal bookTickets(Long momentId, int requiredTickets) {
    Moment moment = getMoment(momentId);
    BigDecimal ticketPrice = moment.getPrice();
    if (moment.getTicketCount() < requiredTickets) {
      throw new InsufficientTicketsException("Not enough tickets available for moment ID: " + momentId);
    }

    // Proceed with booking logic
    moment.setTicketCount(moment.getTicketCount() - requiredTickets);
    momentRepository.save(moment);
    return ticketPrice.multiply(BigDecimal.valueOf(requiredTickets));
  }

  public void cancelTicketBooking(Long momentId, int ticketsToCancel) {
    Moment moment = getMoment(momentId);
    moment.setTicketCount(moment.getTicketCount() + ticketsToCancel);
    momentRepository.save(moment);
  }

  private Moment getMoment(Long momentId) {
    return momentRepository.findById(momentId)
        .orElseThrow(() -> new ResourceNotFoundException("Moment", "Id", momentId.toString()));
  }

  public List<MomentDto> getMomentsByHostId(Long id) {
    List<Moment> moments = momentRepository.findByHostIdOrderByIdDesc(id);
    if (moments.isEmpty()) {
      throw new ResourceNotFoundException("Moments list", "Host_Id" , id.toString());
    }
    return moments.stream()
        .map(momentMapper::toDto)
        .toList();
  }
}