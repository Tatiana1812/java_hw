package ru.otus.service;

import org.springframework.security.core.Authentication;
import ru.otus.dto.request.ReservationRequest;
import ru.otus.dto.response.ReservationResponse;
import java.util.List;

public interface ReservationService {
    ReservationResponse createReservation(ReservationRequest req, Authentication auth);
    List<ReservationResponse> getAllReservations(Authentication auth);

    void cancelReservation(Long reservationId, Authentication auth);
}
