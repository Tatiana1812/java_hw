package ru.otus.listener.homework;

import java.util.*;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {
    private final List<Message> lst = new ArrayList<>();

    @Override
    public void onUpdated(Message msg) {
        List<String> field13Old = msg.getField13().getData();
        List<String> field13New = new ArrayList<>(field13Old.size());
        field13New.addAll(field13Old);
        var field13 = new ObjectForMessage();
        field13.setData(field13New);
        lst.add(new Message.Builder(msg.getId())
                        .field1(msg.getField1())
                        .field2(msg.getField2())
                        .field3(msg.getField3())
                        .field4(msg.getField4())
                        .field5(msg.getField5())
                        .field6(msg.getField6())
                        .field7(msg.getField7())
                        .field8(msg.getField8())
                        .field9(msg.getField9())
                        .field10(msg.getField10())
                        .field11(msg.getField11())
                        .field12(msg.getField12())
                        .field13(field13).build());
    }
    @Override
    public Optional<Message> findMessageById(long id) {
        return lst.stream()
                .filter(m -> m.getId() == id)
                .findFirst();
    }

}
