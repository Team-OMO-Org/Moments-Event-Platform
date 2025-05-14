package io.github.teamomo.moment.service;

import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.dto.MomentRequestDto;
import io.github.teamomo.moment.dto.MomentResponseDto;
import io.github.teamomo.moment.entity.Category;
import io.github.teamomo.moment.entity.Moment;
import io.github.teamomo.moment.entity.Recurrence;
import io.github.teamomo.moment.entity.Status;
import io.github.teamomo.moment.exception.ResourceNotFoundException;
import io.github.teamomo.moment.mapper.MomentMapper;
import io.github.teamomo.moment.repository.CategoryRepository;
import io.github.teamomo.moment.repository.LocationRepository;
import io.github.teamomo.moment.repository.MomentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MomentServiceTest {

  @Mock
  private MomentRepository momentRepository;

  @Mock
  private MomentMapper momentMapper;

  @Mock
  private LocationRepository locationRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private MomentService momentService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }


  @Test
  void createMoment_ShouldSaveMoment() {
    MomentDto momentDto = new MomentDto(
        1L,
        1L,
        1L,
        null,
        "Concert in the Park",
        "A live music concert in the park.",
        null,
        LocalDateTime.now(),
        null,
        BigDecimal.valueOf(50.00),
        null,
        100,
        null
    );

    Category category = new Category();
    category.setId(1L);
    category.setName("Music");

    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(momentRepository.findByTitleAndStartDate(any(), any())).thenReturn(Optional.empty());
    when(categoryRepository.findById(any())).thenReturn(Optional.of(category));


    Moment moment = new Moment();
    when(momentMapper.toEntity(any(MomentDto.class))).thenReturn(moment);
    when(momentRepository.save(any(Moment.class))).thenReturn(moment);

    assertDoesNotThrow(() -> momentService.createMoment(momentDto));
    verify(momentRepository, times(1)).save(any(Moment.class));
  }

  @Test
  void getMomentById_ShouldReturnMomentDto() {
    Moment moment = new Moment();
    moment.setId(1L);
    moment.setTitle("Concert in the Park");

    MomentDto momentDto = new MomentDto(1L,
        1L,
        1L,
        null,
        "Concert in the Park",
        "A live music concert in the park.",
        null,
        LocalDateTime.now(),
        null,
        BigDecimal.valueOf(50.00),
        null,
        100,
        null);

    when(momentRepository.findById(1L)).thenReturn(Optional.of(moment));
    when(momentMapper.toDto(any(Moment.class))).thenReturn(momentDto);

    MomentDto result = momentService.getMomentById(1L);

    assertNotNull(result);
    assertEquals(1L, result.id());
    assertEquals("Concert in the Park", result.title());
    verify(momentRepository, times(1)).findById(1L);
  }

  @Test
  void getMomentById_ShouldThrowResourceNotFoundException() {
    when(momentRepository.findById(1L)).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> momentService.getMomentById(1L));

    assertEquals("Moment not found with the given input data Id : '1'", exception.getMessage());
    verify(momentRepository, times(1)).findById(1L);
  }

//  @Test
//  void createMoment_ShouldThrowException_WhenLocationNotFound() {
//    // Arrange
//    MomentDto momentDto = new MomentDto(
//        1L, 1L, 1L, null, "Concert in the Park", "Description", null,
//        LocalDateTime.now(), null, BigDecimal.valueOf(50.00), null, 100, null
//    );
//
//    when(locationRepository.findById(1L)).thenReturn(Optional.empty());
//
//    // Act & Assert
//    ResourceNotFoundException exception = assertThrows(
//        ResourceNotFoundException.class,
//        () -> momentService.createMoment(momentDto)
//    );
//
//    assertEquals("Location not found with the given input data Id : '1'", exception.getMessage());
//    verify(locationRepository, times(1)).findById(1L);
//  }

  @Test
  void getAllMoments_WithFilters_ShouldReturnPaginatedResponseDtos() {
    Moment moment = new Moment();
    moment.setId(1L);
    moment.setTitle("Concert in the Park");

    MomentResponseDto responseDto = new MomentResponseDto(
        1L,
        "Concert in the Park",
        "Music",
        "New York",
        BigDecimal.valueOf(50.00),
        LocalDateTime.now(),
        null,
        null,
        "A live music concert in the park.",
        "thumbnail_url"
    );

    Page<Moment> momentPage = new PageImpl<>(List.of(moment));
    when(momentRepository.findByFilters(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
        .thenReturn(momentPage);
    when(momentMapper.toFilterResponseDto(any(Moment.class))).thenReturn(responseDto);

    MomentRequestDto momentRequestDto = new MomentRequestDto(
        "1",// categoryId
        "1", // locationId
        BigDecimal.valueOf(10.00), // priceFrom
        BigDecimal.valueOf(100.00), // priceTo
        LocalDateTime.of(2025, 1, 1, 10, 0), // startDateFrom
        LocalDateTime.of(2025, 12, 31, 23, 59), // startDateTo
        Recurrence.ONETIME, // recurrence
        Status.LIVE, // status
        "concert"
    );

    Page<MomentResponseDto> result = momentService.getAllMoments(momentRequestDto, PageRequest.of(0, 10));

    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals("Concert in the Park", result.getContent().get(0).title());
    verify(momentRepository, times(1)).findByFilters(any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
  }
}