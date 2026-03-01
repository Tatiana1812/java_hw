package ru.otus.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservationRequest(
        @NotBlank @Size(min = 2, max = 50)
        String guestName,
        @NotBlank @Size(min = 5, max = 64)
        String guestPhone,
        @Max(value = 8)
        @Positive
        int persons,
        @NotNull
        @Future(message = "Дата и время бронирвоания должны быть позднее текущего времени")
        LocalDateTime startTime
) {
        @AssertTrue(message = "Заведение работает с 12:00 до 24:00. Последнее доступное время начала брони — 21:00")
        public boolean isWithinWorkingHours() {
                if (startTime == null) {
                        return true;
                }

                LocalTime time = startTime.toLocalTime();
                LocalTime openTime = LocalTime.of(12, 0);
                LocalTime lastStartTime = LocalTime.of(21, 0);

                return !time.isBefore(openTime) && !time.isAfter(lastStartTime);
        }
}