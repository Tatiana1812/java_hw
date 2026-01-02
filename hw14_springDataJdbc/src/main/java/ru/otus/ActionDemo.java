package ru.otus;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.services.DBServiceClient;

import java.util.List;

@Component("actionDemo")
@RequiredArgsConstructor
public class ActionDemo implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ActionDemo.class);
    private final DBServiceClient dbServiceClient;

    @Override
    public void run(String... args) {
        var client1 = new Client(null,
                "Vasya",
                new Address(null, "Ukhtomskogo"),
                List.of(new Phone(null, null,1,"88005553535"),
                        new Phone(null, null,2,"788199")), true);
        var client2 = new Client(null,
                "Katya",
                new Address(null, "Tikhomirnova"),
                List.of(new Phone(null, null,1,"89159987654")),true);
        var client3 = new Client(null,
                "Olga",
                new Address(null, "Zelinskogo"),
                List.of(new Phone(null, null,1,"89209127711")),true);

        checkAndCreateClient(client1);

        checkAndCreateClient(client2);

        checkAndCreateClient(client3);

    }

    private void checkAndCreateClient(Client client) {
        var clientName = client.getName();
        if (dbServiceClient.findByName(clientName).isEmpty()) {
            log.info("Create new Client {}", clientName);
            dbServiceClient.saveClient(new Client(null, clientName, client.getAddress(), client.getPhones(), true));
        }
    }
}
