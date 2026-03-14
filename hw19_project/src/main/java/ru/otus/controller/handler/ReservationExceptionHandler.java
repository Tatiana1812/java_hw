package ru.otus.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.controller.exception.NoAvailableTableException;
import ru.otus.controller.exception.ReservationNotFoundException;
import ru.otus.dto.response.AuthErrorResponse;

@RestControllerAdvice
public class ReservationExceptionHandler {

    @ExceptionHandler(NoAvailableTableException.class)
    public ResponseEntity<ApiError> handleNoAvailable(NoAvailableTableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("NO_AVAILABLE_TABLE", ex.getMessage()));
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<AuthErrorResponse> handleReservationNotFound(ReservationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AuthErrorResponse("RESERVATION_NOT_FOUND", "Бронь не найдена"));
    }
    public record ApiError(String code, String message) {}
}
