package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HomeWork {

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);
    public static void main(String[] args) {
        var processors = List.of(new ProcessorChangeFields(), new ProcessorThrowException(LocalDateTime::now));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var objectForMessage = new ObjectForMessage();
        objectForMessage.setData(Stream.of("elem1", "elem2", "elem3")
                .collect(Collectors.toList()));

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field11("field11")
                .field12("field12")
                .field13(objectForMessage)
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
