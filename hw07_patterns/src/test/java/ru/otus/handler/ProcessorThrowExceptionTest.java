package ru.otus.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.DateTimeProvider;
import ru.otus.processor.ProcessorThrowException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class ProcessorThrowExceptionTest {
    @Test
    @DisplayName("Тестируем чётную секунду и выброс исключения")
    void handleExceptionTest() {
        // given
        var message = new Message.Builder(1L).field8("field8").build();

        var dateTimeProvider = mock(DateTimeProvider.class);
        var processor = new ProcessorThrowException(dateTimeProvider);
        when(dateTimeProvider.getDate()).thenReturn(LocalDateTime.of(2023, 8, 17, 12, 0, 2));

        // when
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> processor.process(message));
    }
    @Test
    @DisplayName("Тестируем нечётную секунду")
    void handleWithoutExceptionTest() {
        // given
        var message = new Message.Builder(1L).field8("field8").build();

        var dateTimeProvider = mock(DateTimeProvider.class);
        var processor = new ProcessorThrowException(dateTimeProvider);
        when(dateTimeProvider.getDate()).thenReturn(LocalDateTime.of(2023, 8, 17, 12, 0, 3));

        // when
        assertThat(processor.process(message)).isEqualTo(message);
    }
}
