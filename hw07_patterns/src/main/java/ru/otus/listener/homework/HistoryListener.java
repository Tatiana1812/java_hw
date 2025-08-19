package ru.otus.listener.homework;

import java.util.*;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {
    private final List<Message> lst = new ArrayList<>();

    @Override
    public void onUpdated(Message msg) {
        ObjectForMessage oldField13 = msg.getField13();
        if (oldField13 != null) {
            List<String> field13Old = oldField13.getData();

            List<String> field13New = new ArrayList<>();
            field13New.addAll(field13Old);

            var field13 = new ObjectForMessage();
            field13.setData(field13New);

            lst.add(msg.toBuilder().field13(field13).build());
        } else {
            lst.add(msg.toBuilder().build());
        }
    }
    @Override
    public Optional<Message> findMessageById(long id) {
        return lst.stream()
                .filter(m -> m.getId() == id)
                .findFirst();
    }

}
