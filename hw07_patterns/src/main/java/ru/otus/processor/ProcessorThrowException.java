package ru.otus.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Message;

public class ProcessorThrowException implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(LoggerProcessor.class);

    private final DateTimeProvider dateTimeProvider;

    public ProcessorThrowException(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        var curDate = dateTimeProvider.getDate();

        logger.info("date: {}", curDate);
        logger.info("seconds: {}", curDate.getSecond());

        if (curDate.getSecond() % 2 == 0) {
            throw new RuntimeException("Ошибка обработки в чётную секунду");
        }

        return message;
    }
}
