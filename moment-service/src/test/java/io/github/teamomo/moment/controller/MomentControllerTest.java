package io.github.teamomo.moment.controller;


import io.github.teamomo.moment.config.TestConfig;
import io.github.teamomo.moment.dto.MomentDto;
import io.github.teamomo.moment.dto.MomentRequestDto;
import io.github.teamomo.moment.dto.MomentResponseDto;
import io.github.teamomo.moment.entity.Location;
import io.github.teamomo.moment.entity.Recurrence;
import io.github.teamomo.moment.entity.Status;
import io.github.teamomo.moment.service.MomentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the MomentController class.
 */
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class MomentControllerTest {

  private static final Logger logger = LoggerFactory.getLogger(MomentControllerTest.class);

  private MockMvc mockMvc;

  @Mock
  private MomentService momentService;

  @InjectMocks
  private MomentController momentController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(momentController)
        .setMessageConverters(new MappingJackson2HttpMessageConverter())
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .build();
  }

  @Test
  void getAllMoments_ShouldReturnPaginatedMoments() throws Exception {
    MomentResponseDto moment = new MomentResponseDto(
        1L,
        "Concert in the Park",
        "Music",
        "New York",
        BigDecimal.valueOf(50.00),
        LocalDateTime.of(2025, 6, 1, 19, 0),
        Recurrence.ONETIME,
        Status.LIVE,
        "A live music concert in the park.",
        ""
    );

    // Use an actual Pageable instance instead of Unpaged
    Pageable pageable = PageRequest.of(0, 12, Sort.by("startDate").ascending());

    // Return a PageImpl with a proper pageable to avoid serialization issues
    Mockito.when(momentService.getAllMoments(any(MomentRequestDto.class), any()))
        .thenReturn(new PageImpl<>(List.of(moment), pageable, 1));

      mockMvc.perform(get("/api/v1/moments")
              .param("page", "0")
              .param("size", "12")
              .param("sort", "startDate,asc")
              .contentType(MediaType.APPLICATION_JSON))
          .andDo(result -> {
            System.out.println("Response: " + result.getResponse().getContentAsString());
            if (result.getResolvedException() != null) {
              result.getResolvedException().printStackTrace();
              logger.debug("Response: " + result.getResponse().getContentAsString());
            }
          })
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content[0].id").value(1))
          .andExpect(jsonPath("$.content[0].title").value("Concert in the Park"));

  }

  @Test
  void getMomentById_ShouldReturnMomentDetails() throws Exception {
    // Mock response
    Location location = new Location(1L, "New York", "123 Broadway, New York, NY 10006");// Mock location
    MomentDto momentDto = new MomentDto(
        1L, // id
        1L, // hostId
        1L, // categoryId
        location, // location
        "Concert in the Park", // title
        "A live music concert in the park.", // shortDescription
        "thumbnail_url", // thumbnail
        LocalDateTime.of(2025, 6, 1, 19, 0), // startDate
        Recurrence.ONETIME, // recurrence
        BigDecimal.valueOf(50.00), // price
        Status.LIVE, // status
        100 ,
        null// ticketCount
    );

    Mockito.when(momentService.getMomentById(1L)).thenReturn(momentDto);

    // Perform GET request
    mockMvc.perform(get("/api/v1/moments/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.location.city").value("New York"))
        .andExpect(jsonPath("$.title").value("Concert in the Park"))
        .andExpect(jsonPath("$.shortDescription").value("A live music concert in the park."))
        .andExpect(jsonPath("$.price").value(50.00))
        .andExpect(jsonPath("$.recurrence").value("ONETIME"))
        .andExpect(jsonPath("$.status").value("LIVE"));
  }
}