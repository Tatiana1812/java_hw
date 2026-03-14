package ru.otus.dto.response;

import ru.otus.enums.ReservationStatus;
import java.time.LocalDateTime;

/**
 * DTO ответа по бронированию (для создания и для списка).
 *
 * @param guestName - имя гостя для конкретной брони
 * @param guestPhone - телефон гостя для конкретной брони
 * @param persons - количество гостей(1-8)
 * @param reservationId - идентификатор брони
 * @param tableId - номер столика
 * @param status - текущий статус брони
 * @param startTime - дата и время начала брони
 * @param endTime - дата и время окончания брони
 *
 */
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
