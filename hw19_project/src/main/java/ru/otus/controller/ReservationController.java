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

/**
 * REST-контроллер для операций с бронированиями.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Создаёт бронь на указанный интервал (по умолчанию 3 часа).
     *
     * @param request - параметры брони (имя/телефон/кол-во гостей/startTime)
     * @param auth - текущая аутентификация пользователя
     * @return созданная бронь
     */
    @PostMapping("/reservation")
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody ReservationRequest request, Authentication auth) {
        ReservationResponse response = reservationService.createReservation(request, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Возвращает список броней текущего пользователя.
     *
     * @param auth - текущая аутентификация пользователя
     * @return список броней
     */
    @GetMapping("/reservation")
    public ResponseEntity<List<ReservationResponse>> reservationList(Authentication auth) {
        List<ReservationResponse> response = reservationService.getAllReservations(auth);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Отменяет бронь (переводит статус в CANCELLED).
     *
     * @param id - идентификатор брони
     * @param auth - текущая аутентификация пользователя
     * @return статус 200
     */
    @PostMapping("/reservation/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id, Authentication auth) {
        reservationService.cancelReservation(id, auth);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
