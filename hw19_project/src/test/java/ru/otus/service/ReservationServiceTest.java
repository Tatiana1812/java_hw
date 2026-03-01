package ru.otus.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.config.PostgresContainerBase;
import ru.otus.controller.exception.NoAvailableTableException;
import ru.otus.controller.exception.ReservationNotFoundException;
import ru.otus.controller.exception.UnauthorizedException;
import ru.otus.dto.request.ReservationRequest;
import ru.otus.dto.response.ReservationResponse;
import ru.otus.repository.ReservationsRepository;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.enums.ReservationStatus.CANCELLED;
import static ru.otus.enums.ReservationStatus.CONFIRMED;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationServiceTest extends PostgresContainerBase {

    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationsRepository reservationsRepository;

    @BeforeAll
    void cleanDatabase() {
        reservationsRepository.deleteAll();
    }

    @Test
    @Order(1)
    void getAllReservationsEmptySuccess() {
        Authentication auth = auth("user1");

        List<ReservationResponse> response = reservationService.getAllReservations(auth);

        assertThat(response.size()).isEqualTo(0);

    }

    @Test
    @Order(2)
    void createReservationSuccess() {
        Authentication auth = auth("user1");

        LocalDateTime start = LocalDateTime.now()
                .plusDays(1)
                .withHour(12).withMinute(0).withSecond(0).withNano(0);

        var request = new ReservationRequest("Anna", "+79990000000", 7, start);

        ReservationResponse response = reservationService.createReservation(request, auth);

        assertThat(response.status()).isEqualTo(CONFIRMED);
        assertThat(response.reservationId()).isNotNull();
        assertThat(response.tableId()).isNotNull();

        assertThat(reservationsRepository.count()).isEqualTo(1);
    }

    @Test
    @Order(3)
    void createReservationSuccessOtherTable() {
        Authentication auth = auth("user1");
        LocalDateTime start = LocalDateTime.now()
                .plusDays(1)
                .withHour(12).withMinute(0).withSecond(0).withNano(0);

        ReservationRequest request = new ReservationRequest("Anna", "+79996661223", 2, start);

        ReservationResponse response = reservationService.createReservation(request, auth);

        assertThat(response.status()).isEqualTo(CONFIRMED);
        assertThat(response.reservationId()).isNotNull();
        assertThat(response.tableId()).isNotNull();

        assertThat(reservationsRepository.count()).isEqualTo(2);
    }

    @Test
    @Order(4)
    void createReservationFailedNotAvailableTable() {
        Authentication auth = auth("user1");
        LocalDateTime start = LocalDateTime.now()
                .plusDays(1)
                .withHour(12).withMinute(0).withSecond(0).withNano(0);
        ReservationRequest request = new ReservationRequest("Anna", "+79990000000", 9, start);

        assertThrows(NoAvailableTableException.class, () -> reservationService.createReservation(request, auth));
        assertThat(reservationsRepository.count()).isEqualTo(2);
    }

    @Test
    @Order(5)
    void createReservationFailedUnauthorized() {
        Authentication auth = auth("user7");
        LocalDateTime start = LocalDateTime.now()
                .plusDays(1)
                .withHour(12).withMinute(0).withSecond(0).withNano(0);
        ReservationRequest request = new ReservationRequest("Anna", "+79990000000", 8, start);

        assertThrows(UnauthorizedException.class, () -> reservationService.createReservation(request, auth));
    }

    @Test
    @Order(6)
    void cancelSuccess() {
        Authentication auth = auth("user1");

        List<ReservationResponse> response = reservationService.getAllReservations(auth);

        assertDoesNotThrow(() -> reservationService.cancelReservation(response.getFirst().reservationId(), auth));
    }
    @Test
    @Order(7)
    void cancelFailed() {
        Authentication auth = auth("user1");

        List<ReservationResponse> response = reservationService.getAllReservations(auth);

        assertThrows(ReservationNotFoundException.class, () -> reservationService.cancelReservation(response.getLast().reservationId() + 5, auth));

    }

    @Test
    @Order(8)
    void getAllReservationsSuccess() {
        Authentication auth = auth("user1");

        List<ReservationResponse> response = reservationService.getAllReservations(auth);

        assertThat(response.size()).isEqualTo(2);
        assertThat(response.getFirst().status()).isEqualTo(CONFIRMED);
        assertThat(response.getLast().status()).isEqualTo(CANCELLED);

    }


    private Authentication auth(String login) {
        var token = new TestingAuthenticationToken(login, "N/A", "ROLE_USER");
        token.setAuthenticated(true);
        return token;
    }
}