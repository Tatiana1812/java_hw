package ru.otus.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.otus.dto.request.ReservationRequest;
import ru.otus.dto.response.ReservationResponse;
import ru.otus.service.ReservationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reservation")
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody ReservationRequest request, Authentication auth) {
        ReservationResponse response = reservationService.createReservation(request, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/reservation")
    public ResponseEntity<List<ReservationResponse>> reservationList(Authentication auth) {
        List<ReservationResponse> response = reservationService.getAllReservations(auth);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/reservation/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id, Authentication auth) {
        reservationService.cancelReservation(id, auth);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
