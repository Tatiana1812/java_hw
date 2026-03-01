package ru.otus.dto.response;

import ru.otus.enums.ReservationStatus;
import java.time.LocalDateTime;

public record ReservationResponse(
        String guestName,
        String guestPhone,
        Integer persons,
        Long reservationId,
        Long tableId,
        ReservationStatus status,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
