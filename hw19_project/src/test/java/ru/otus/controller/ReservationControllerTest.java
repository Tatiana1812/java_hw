package ru.otus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.controller.handler.ReservationExceptionHandler;
import ru.otus.dto.request.ReservationRequest;
import ru.otus.dto.response.ReservationResponse;
import ru.otus.enums.ReservationStatus;
import ru.otus.service.ReservationService;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReservationController reservationController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController)
                .setControllerAdvice(new ReservationExceptionHandler())
                .build();
    }

    @Test
    void createShouldReturn201AndBody() throws Exception {
        ReservationRequest request = new ReservationRequest(
                "Anna", "+79962398519", 2, LocalDateTime.of(2026, 3, 27, 13, 0)
        );
        ReservationResponse response = new ReservationResponse(
                "Anna", "+79962398519", 2, 101L, 5L, ReservationStatus.CONFIRMED,
                LocalDateTime.of(2030, 3, 27, 13, 0),
                LocalDateTime.of(2030, 3, 27, 16, 0)
        );

        when(reservationService.createReservation(eq(request), eq(authentication))).thenReturn(response);

        mockMvc.perform(post("/api/reservation")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reservationId").value(101L))
                .andExpect(jsonPath("$.tableId").value(5L))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));


        verify(reservationService).createReservation(eq(request), eq(authentication));
    }

    @Test
    void listShouldReturn200AndReservations() throws Exception {
        List<ReservationResponse> response = List.of(
                new ReservationResponse("Anna", "+111111", 2, 101L, 5L, ReservationStatus.CONFIRMED,
                        LocalDateTime.of(2030, 3, 27, 13, 0),
                        LocalDateTime.of(2030, 3, 27, 16, 0)),
                new ReservationResponse("Bob", "+222222", 4, 102L, 7L, ReservationStatus.CANCELLED,
                        LocalDateTime.of(2030, 3, 28, 14, 0),
                        LocalDateTime.of(2030, 3, 28, 17, 0))
        );
        when(reservationService.getAllReservations(authentication)).thenReturn(response);

        mockMvc.perform(get("/api/reservation").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationId").value(101L))
                .andExpect(jsonPath("$[1].status").value("CANCELLED"));

        verify(reservationService).getAllReservations(authentication);
    }

    @Test
    void cancelShouldReturn200() throws Exception {
        mockMvc.perform(post("/api/reservation/77/cancel").principal(authentication))
                .andExpect(status().isOk());

        verify(reservationService).cancelReservation(77L, authentication);
    }
}
